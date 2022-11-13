package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.FitmentArticleDto;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.VehicleGenArtArtSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.ivds.api.IvdsArticleInVehicleContextService;
import com.sagag.services.ivds.api.IvdsOilSearchService;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.converter.fitment.DefaultFitmentArticleConverter;
import com.sagag.services.ivds.converter.genericarticle.GenericArticleIdInVehicleContextConverter;
import com.sagag.services.ivds.converter.genericarticle.GenericArticleIdInVehicleContextConverter.GenericArticleIdTypeEnum;
import com.sagag.services.ivds.executor.IvdsArticleVehicleInContextTaskExecutor;
import com.sagag.services.ivds.request.CategorySearchRequest;
import com.sagag.services.ivds.response.ArticleListSearchResponseDto;
import com.sagag.services.ivds.utils.FitmentAndArticleGaIdCombinator;
import com.sagag.services.ivds.utils.FitmentArticleCombinator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IvdsArticleInVehicleContextServiceImpl extends ArticleProcessor
    implements IvdsArticleInVehicleContextService {

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Autowired
  private ContextService contextService;

  @Autowired
  private FitmentAndArticleGaIdCombinator fitmentAndArticleGaIdCombinator;

  @Autowired
  private VehicleGenArtArtSearchService vehGenArtArtSearchService;

  @Autowired
  private IvdsOilSearchService ivdsOilSearchService;

  @Autowired
  private FitmentArticleCombinator fitmentArticleCombinator;

  @Autowired
  private DefaultFitmentArticleConverter defFitmentArtConverter;

  @Autowired
  private GenericArticleIdInVehicleContextConverter gaIdInVehContextConverter;

  @Autowired
  private IvdsArticleVehicleInContextTaskExecutor ivdsArticleVehInContextTaskExecutor;

  @Override
  public ArticleListSearchResponseDto searchArticlesInVehicleContext(UserInfo user,
      CategorySearchRequest body) throws ServiceException {
    ArticleListSearchResponseDto resultSearch = doSearchArticlesInVehicleContext(user, body);
    doUpdateFlagFavorite(user, resultSearch);
    return resultSearch;
  }

  private ArticleListSearchResponseDto doSearchArticlesInVehicleContext(UserInfo user,
      CategorySearchRequest body) throws ServiceException {
    log.debug("Returning the searched articles by body request = {}",
        SagJSONUtil.convertObjectToPrettyJson(body));
    Assert.notEmpty(body.getGenArtIds(), "The category Ids should not be empty");
    Assert.notEmpty(body.getVehIds(), "The vehicle Ids should not be empty");

    final String vehId = body.getVehIds().get(0); // Get first vehicle id
    final Optional<VehicleDto> vehInContextOpt =
        searchDefaultSelectedVehicleFromContext(user.key(), vehId);

    final List<String> genericArticleIds = new ArrayList<>();
    final List<String> olyslagerGaIds = new ArrayList<>();
    Optional.of(body.getGenArtIds()).map(gaIdInVehContextConverter).ifPresent(gaIdMap -> {
      genericArticleIds.addAll(gaIdMap.get(GenericArticleIdTypeEnum.OTHER_GENERIC_ARTICLE_ID));
      olyslagerGaIds.addAll(gaIdMap.get(GenericArticleIdTypeEnum.OIL_GENERIC_ARTICLE_ID));
    });

    final boolean usingVer2 = body.isUsingVersion2();
    final ArticleListSearchResponseDto response = new ArticleListSearchResponseDto();
    if (CollectionUtils.isEmpty(olyslagerGaIds)) {
      return searchArticlesWithoutOilArticles(response, user, vehInContextOpt, genericArticleIds,
          usingVer2);
    }

    // Check OATS type id selected
    final List<String> oilIds = body.getSelectedOilIds();
    final List<String> selectedCategoryIds = body.getSelectedCategoryIds();
    if (!CollectionUtils.isEmpty(oilIds)) {
      final List<ArticleDocDto> olyslagerArticles =
          ivdsOilSearchService.searchOilRecommendArticles(user, oilIds, olyslagerGaIds,
              vehInContextOpt, selectedCategoryIds).getContent();
      return searchArticles(response, user, vehInContextOpt, genericArticleIds, olyslagerArticles,
          usingVer2);
    }

    final List<OilTypeIdsDto> oilTypeIdsDtos = ivdsOilSearchService
        .getOilTypesByVehicleId(vehInContextOpt, olyslagerGaIds, selectedCategoryIds);
    if (CollectionUtils.isEmpty(oilTypeIdsDtos)) {
      return searchArticlesWithoutOilArticles(response, user, vehInContextOpt, genericArticleIds,
          usingVer2);
    }

    // #1713: CHUMARID: After selecting Motorenoel, user is directed in the Result List
    // 1. a) When the user clicks on Quick Click Motor�l and the system gets
    // more than one vehicle from Olyslager then the user has to select one of the vehicles .
    // There is no article search.
    // --> open oil popup
    // 1. b) When the user clicks on Quick Click Motor�l and the system gets
    // only one vehicle then nothing happens.
    // --> return the selected oil type ids
    // If user select from category tree, support return the article page
    if (body.isSelectedFromQuickClick()
        || CollectionUtils.size(oilTypeIdsDtos) > NumberUtils.INTEGER_ONE) {
      response.setOilTypeIds(oilTypeIdsDtos);
      return response;
    }
    final List<String> firstOilIds = Arrays.asList(oilTypeIdsDtos.get(0).getId());
    final List<ArticleDocDto> olyslagerArticles =
        ivdsOilSearchService.searchOilRecommendArticles(user, firstOilIds, olyslagerGaIds,
            vehInContextOpt, selectedCategoryIds).getContent();

    return searchArticles(response, user, vehInContextOpt, genericArticleIds, olyslagerArticles,
        usingVer2);
  }

  private Optional<VehicleDto> searchDefaultSelectedVehicleFromContext(String contextKey,
      String altVehicleId) {
    final Optional<VehicleDto> vehInContextOpt = contextService.getVehicleInContext(contextKey);
    if (vehInContextOpt.filter(veh -> StringUtils.equals(altVehicleId, veh.getVehId()))
        .isPresent()) {
      return vehInContextOpt;
    }
    return ivdsVehicleService.searchVehicleByVehId(altVehicleId);
  }

  private ArticleListSearchResponseDto searchArticlesWithoutOilArticles(
      final ArticleListSearchResponseDto response, final UserInfo user,
      final Optional<VehicleDto> vehInContextOpt, final List<String> genericArticleIds,
      final boolean usingVer2) {
    return searchArticles(response, user, vehInContextOpt, genericArticleIds,
        Collections.emptyList(), usingVer2);
  }

  private ArticleListSearchResponseDto searchArticles(final ArticleListSearchResponseDto response,
      final UserInfo user, final Optional<VehicleDto> vehInContextOpt,
      final List<String> genericArticleIds, final List<ArticleDocDto> olyslagerArticles,
      final boolean usingVer2) {
    log.debug("Found Generic Article Ids = {}", genericArticleIds);
    final List<ArticleDocDto> articleDocs = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(genericArticleIds)) {
      articleDocs.addAll(
          searchArticlesByVehIdAndGaIds(user, vehInContextOpt, genericArticleIds, usingVer2));
    }

    final List<ArticleDocDto> articles;
    if (CollectionUtils.isEmpty(olyslagerArticles)) {
      articles = articleDocs;
    } else {
      // extract only non oil gaids to search in elatic search to save time when call always
      articles = ListUtils.union(olyslagerArticles, articleDocs);
    }
    response.setArticles(new PageImpl<>(articles));
    return response;
  }

  private List<ArticleDocDto> searchArticlesByVehIdAndGaIds(UserInfo user,
      Optional<VehicleDto> vehInContextOpt, List<String> gaIds, final boolean usingVer2) {
    final String vehId = vehInContextOpt.map(VehicleDto::getVehId).orElse(StringUtils.EMPTY);
    if (StringUtils.isBlank(vehId) || CollectionUtils.isEmpty(gaIds)) {
      return Collections.emptyList();
    }

    final List<VehicleGenArtArtDoc> fitmentArticles =
        vehGenArtArtSearchService.searchFitments(vehId, gaIds);

    // Get List of articles Id
    final List<String> articleIds = defFitmentArtConverter.apply(fitmentArticles).stream()
        .map(FitmentArticleDto::getArtId).collect(Collectors.toList());

    final Page<ArticleDocDto> articles =
        new PageImpl<>(articleSearchService.searchArticlesByIdSagSyses(articleIds,
            user.isSaleOnBehalf(), findEsAffilateNameLocks(user))).map(articleConverter);
    if (!articles.hasContent()) {
      return Collections.emptyList();
    }

    // Get articles from ERP
    final List<ArticleDocDto> modifiableArticles = new ArrayList<>(articles.getContent());

    // Combine article with merged criteria
    final List<ArticleDocDto> articleDtos = fitmentArticleCombinator
        .combineArticlesByFitment(modifiableArticles, new HashMap<>(), vehId);

    fitmentAndArticleGaIdCombinator.combineGaIds(articleDtos, fitmentArticles);

    // Update article info
    final Optional<AdditionalSearchCriteria> vehAdditionalSearchCriteria = additionalCriBuilder
        .buildVehicleAdditional(vehInContextOpt.map(VehicleDto::getKTypeNrStr), usingVer2);
    return ivdsArticleVehInContextTaskExecutor.execute(user, articleDtos, vehInContextOpt,
        vehAdditionalSearchCriteria, usingVer2);
  }

}
