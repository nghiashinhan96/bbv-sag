package com.sagag.services.ivds.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleInfoDto;
import com.sagag.services.domain.article.oil.OilProduct;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.hazelcast.api.CategoryCacheService;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.api.IvdsOatesSearchService;
import com.sagag.services.ivds.filter.additional_recommendation.AdditionalRecommendationFilter;
import com.sagag.services.ivds.oates.OatesCacheService;
import com.sagag.services.oates.api.OatesService;
import com.sagag.services.oates.config.OatesProfile;
import com.sagag.services.oates.dto.OatesApplicationDto;
import com.sagag.services.oates.dto.OatesChoiceDto;
import com.sagag.services.oates.dto.OatesChoiceOptionDto;
import com.sagag.services.oates.dto.OatesDecisionTreeDto;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;
import com.sagag.services.oates.dto.OatesVehicleDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@OatesProfile
public class IvdsOatesSearchServiceImpl implements IvdsOatesSearchService {

  private static final String OATES_EXT_SERVICE_CATEGORY = "oates";

  private static final int LENGTH_OF_CZ_AX_ARTICLE_ID_DIGIT = 10;

  private static final int LENGTH_OF_CZ_ST_ARTICLE_ID_DIGIT = 8;

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Autowired
  private OatesService oatesService;

  @Autowired
  private CategoryCacheService categoryCacheService;

  @Autowired
  private OatesCacheService oatesCacheService;

  @Autowired
  private AdditionalRecommendationFilter additionalRecommendationFilter;

  @Override
  public List<OilTypeIdsDto> getOilTypesByVehicleId(Optional<VehicleDto> vehicleOpt,
      List<String> oilGenericArticleIds, List<String> categoryIds) throws ServiceException {
    Assert.notEmpty(oilGenericArticleIds, "The given oil generic article ids must not be empty");

    final String vehicleId = vehicleOpt.map(VehicleDto::getVehId)
        .orElseThrow(() -> new IllegalArgumentException("The given vehicle must not be empty"));
    final Optional<OatesVehicleDto> oatesVehicleOpt = oatesService.searchOatesVehicle(vehicleId);
    if (!oatesVehicleOpt.isPresent()) {
      return Collections.emptyList();
    }

    final String href = oatesVehicleOpt.get().getHref();
    final OatesEquipmentProductsDto oatesEquipmentProductsDto =
        oatesService.searchOatesEquipment(href);
    final List<OatesApplicationDto> applications = oatesEquipmentProductsDto.getApplications();
    if (CollectionUtils.isEmpty(applications)) {
      return Collections.emptyList();
    }

    final Collection<CategoryDoc> categories = categoryCacheService
        .findCategoriesByGaIdsAndExternalService(oilGenericArticleIds, OATES_EXT_SERVICE_CATEGORY);

    oatesCacheService.saveOatesApplications(buildUserKeyForOatesCache(vehicleId), applications);

    List<OatesDecisionTreeDto> dTrees = oatesEquipmentProductsDto.getDecistionTree();
    return parseToOilTypeIds(categories, dTrees, ListUtils.emptyIfNull(categoryIds), applications);
  }

  private static List<OilTypeIdsDto> parseToOilTypeIds(final Collection<CategoryDoc> categories,
      final List<OatesDecisionTreeDto> dTrees, final List<String> categoryIds,
      final List<OatesApplicationDto> applications) {
    final Set<String> selectedCategoryIdSet = new HashSet<>(categoryIds);

    List<CategoryDoc> selectedCategories = categories.stream()
            .filter(cat -> selectedCategoryIdSet.contains(cat.getLeafid()))
        .collect(Collectors.toList());

    final List<OatesApplicationDto> validOatesApplicationDtos = applications.stream()
        .filter(app -> selectedCategories.stream().anyMatch(cat -> StringUtils
            .equalsIgnoreCase(app.getAppTypeOriginal(), cat.getExternalServiceAttribute())))
        .collect(Collectors.toList());

    Map<String, OatesApplicationDto> validOatesApplicationMap = validOatesApplicationDtos.stream()
        .collect(Collectors.toMap(OatesApplicationDto::getId, o -> o));

    filterDecisionTrees(dTrees, validOatesApplicationMap, selectedCategories);

    final List<OilTypeIdsDto> oilTypeIds = new ArrayList<>();
    dTrees.forEach(catTree -> {
      if (StringUtils.isNotBlank(catTree.getApplicationId())
          || CollectionUtils.isNotEmpty(catTree.getChoice().getOptions())) {
        oilTypeIds.add(catTree);
      }
    });

    return oilTypeIds;
  }

  private static void filterDecisionTrees(List<OatesDecisionTreeDto> dTrees,
      Map<String, OatesApplicationDto> validOatesApplicationMap, List<CategoryDoc> selectedCatTrees) {
    Iterator<OatesDecisionTreeDto> iDTrees = dTrees.iterator();
    while (iDTrees.hasNext()) {
      OatesDecisionTreeDto dTree = iDTrees.next();
      Set<String> validOatesApplicationIds = validOatesApplicationMap.keySet();
      String appId = dTree.getApplicationId();
      if (!StringUtils.isBlank(appId)) {
        updateCategoryInfoForRootTree(dTree, selectedCatTrees, validOatesApplicationMap.get(appId));
        if (!validOatesApplicationIds.contains(appId)) {
          iDTrees.remove();
        }
      } else {
        recursiveFilterChoice(dTree.getChoice(), validOatesApplicationMap, selectedCatTrees);
      }
    }
  }

  private static void updateCategoryInfoForRootTree(OatesDecisionTreeDto dTree,
      List<CategoryDoc> selectedCatTrees, OatesApplicationDto app) {
    if (app == null) {
      return;
    }
    dTree.setGuid(app.getGuid());
    selectedCatTrees
        .stream().filter(catTree -> StringUtils
            .equalsIgnoreCase(catTree.getExternalServiceAttribute(), app.getAppTypeOriginal()))
        .findFirst().ifPresent(x -> {
          dTree.setCateId(x.getLeafid());
          dTree.setCateName(x.getLeaftxt());
        });
  }

  private static void updateCategoryInfoForOption(OatesChoiceOptionDto option,
      List<CategoryDoc> selectedCategories, OatesApplicationDto app) {
    if (app == null) {
      return;
    }
    option.setGuid(app.getGuid());
    selectedCategories
        .stream().filter(catTree -> StringUtils
            .equalsIgnoreCase(catTree.getExternalServiceAttribute(), app.getAppTypeOriginal()))
        .findFirst().ifPresent(x -> {
          option.setCateId(x.getLeafid());
          option.setCateName(x.getLeaftxt());
        });
  }

  private static void recursiveFilterChoice(OatesChoiceDto oatesChoiceDto,
      Map<String, OatesApplicationDto> validOatesApplicationMap, List<CategoryDoc> selectedCategories) {
    if (Objects.isNull(oatesChoiceDto)) {
      return;
    }

    List<OatesChoiceOptionDto> options = oatesChoiceDto.getOptions();
    Iterator<OatesChoiceOptionDto> iOptions = options.iterator();
    while (iOptions.hasNext()) {
      OatesChoiceOptionDto option = iOptions.next();
      if (!StringUtils.isBlank(option.getApplicationId())) {
        updateCategoryInfoForOption(option, selectedCategories,
            validOatesApplicationMap.get(option.getApplicationId()));
        if (!validOatesApplicationMap.containsKey(option.getApplicationId())) {
          iOptions.remove();
        }
      } else {
        recursiveFilterChoice(option.getChoice(), validOatesApplicationMap, selectedCategories);
      }
    }
  }

  @Override
  public Page<ArticleDocDto> searchOilRecommendArticles(UserInfo user, List<String> guids,
      List<String> oilGenericArticleIds, Optional<VehicleDto> vehicleOpt, List<String> categoryIds)
      throws ServiceException {

    final List<OatesApplicationDto> cachedApplications =
        getOrRequestOatesApplications(categoryIds, oilGenericArticleIds, vehicleOpt);

    final List<OatesApplicationDto> filteredApplications = cachedApplications.stream().filter(
        application -> guids.stream().anyMatch(id -> StringUtils.equals(id, application.getGuid())))
        .collect(Collectors.toList());

    Set<String> axIds = filteredApplications.stream()
        .flatMap(application -> application.getAxIds().stream()).collect(Collectors.toSet());

    // #6280: [CZAX10]: OATS - filter ERP / AX_id's
    // OATS will send both 8-digit and 10-digit ID's to manage the migration.
    // In this phase we should add a filter in AX10 to ignore the 8-digit id's
    // #6282: [CZAX09]: OATS - filter ERP / AX_id's
    // OATS will send both 8-digit and 10-digit ID's to manage the migration.
    // In this phase we should add a filter in AX09 to ignore the 10-digit id's
    final SupportedAffiliate affiliate = user.getSupportedAffiliate();
    if (affiliate != null && affiliate.isCzBasedAffiliate()) {
      final int lengthOfCzBasedArticleIdDigit = affiliate.isSagCzAffiliate()
          ? LENGTH_OF_CZ_AX_ARTICLE_ID_DIGIT : LENGTH_OF_CZ_ST_ARTICLE_ID_DIGIT;
      final Predicate<String> onlyCzBasedDigitsPredicate =
          artId -> StringUtils.length(StringUtils.trim(artId)) == lengthOfCzBasedArticleIdDigit;
      axIds = axIds.stream().filter(onlyCzBasedDigitsPredicate).collect(Collectors.toSet());
    }

    if (CollectionUtils.isEmpty(axIds)) {
      return Page.empty();
    }

    List<String> additionalRecommendAxIds = Lists.newArrayList();
    if (user.getSettings().isAdditionalRecommendationEnabled()) {
      additionalRecommendAxIds = additionalRecommendationFilter
          .filterAdditionalRecommendationArticles(user, filteredApplications);
      axIds.addAll(additionalRecommendAxIds);
    }
    final List<OilProduct> oilProducts = filteredApplications.stream()
        .map(application -> SagBeanUtils.map(application, OilProduct.class))
        .collect(Collectors.toList());

    final Page<ArticleDocDto> articles = ivdsArticleService
        .searchAndFillOilProductArticlesByArticleIds(user, axIds, vehicleOpt, oilProducts);

    if (!articles.hasContent()) {
      return Page.empty();
    }

    // filter by checked oilGenericArticleIds
    List<ArticleDocDto> targetArticles = articles.getContent().stream()
        .filter(article -> oilGenericArticleIds.contains(article.getGaId()))
        .collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(additionalRecommendAxIds)) {
      targetArticles.stream()
          .forEach(filterAdditionalRecommendation(user, additionalRecommendAxIds));
    }
    return new PageImpl<>(targetArticles);
  }

  private Consumer<? super ArticleDocDto> filterAdditionalRecommendation(final UserInfo user,
      final List<String> additionalRecommendAxIds) {
    return article -> {
      if (additionalRecommendAxIds.contains(article.getArtid())) {
        Optional<ArticleInfoDto> additionalRecommendationOpt =
            additionalRecommendationFilter.filterAdditionalRecommendation(user);
        additionalRecommendationOpt.ifPresent(
            additionalRecommendation -> article.getInfos().add(additionalRecommendation));
      }
    };
  }

  private List<OatesApplicationDto> getOrRequestOatesApplications(List<String> categoryIds,
      List<String> oilGenericArticleIds, Optional<VehicleDto> vehicleOpt) throws ServiceException {
    final String vehId = vehicleOpt.map(VehicleDto::getVehId).orElse(StringUtils.EMPTY);
    final String userKey = buildUserKeyForOatesCache(vehId);
    final List<OatesApplicationDto> cachedApplications =
        oatesCacheService.getOatesApplicationsByUserKey(userKey);
    if (CollectionUtils.isEmpty(cachedApplications)) {
      // Force request OATES vehicle info and saving to hazelcast during 1 hour.
      getOilTypesByVehicleId(vehicleOpt, oilGenericArticleIds, categoryIds);
      return oatesCacheService.getOatesApplicationsByUserKey(userKey);
    }
    return cachedApplications;
  }

  @Override
  public List<String> extractOilGenericArticleIds(Collection<String> gaIds) {
    final List<String> originalGaIds =
        CollectionUtils.emptyIfNull(gaIds).stream().distinct().collect(Collectors.toList());
    final Collection<CategoryDoc> categories = categoryCacheService
        .findCategoriesByGaIdsAndExternalService(originalGaIds, OATES_EXT_SERVICE_CATEGORY);
    if (CollectionUtils.isEmpty(categories)) {
      return Collections.emptyList();
    }
    return categories.stream().flatMap(catDoc -> catDoc.getGenarts().stream()).map(GenArt::getGaid)
        .distinct().collect(Collectors.toList());
  }

}
