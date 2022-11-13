package com.sagag.services.ivds.filter.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleAccessoryCriteriaDto;
import com.sagag.services.domain.article.ArticleAccessoryDto;
import com.sagag.services.domain.article.ArticleAccessoryItemDto;
import com.sagag.services.domain.article.ArticleCriteriaDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.article.GenArtTxtDto;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.hazelcast.api.MakeCacheService;
import com.sagag.services.hazelcast.api.ModelCacheService;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Order(4)
@Slf4j
public class NonCachedAccessoryListFilterContext extends ArticleFilterContext {

  private static final int LINK_TYPE_MAKE = 1;

  private static final int LINK_TYPE_MODEL = 2;

  private static final int LINK_TYPE_VEHICLE = 3;

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private GenArtCacheService genArtCacheService;

  @Autowired
  private MakeCacheService makeCacheService;

  @Autowired
  private ModelCacheService modelCacheService;

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {
    final FilteredArticleAndAggregationResponse response =
        getArticleFilterResponse(user, filterMode, request, pageable);
    final Page<ArticleDocDto> articles = response.getArticles();

    List<ArticleDocDto> resultErp = ivdsArticleTaskExecutors.executeTaskWithErpArticle(user,
        articles.getContent(), Optional.empty(), additional);

    Map<String, ArticleDocDto> articleMap =
        resultErp.stream().collect(Collectors.toMap(ArticleDocDto::getArtid, Function.identity()));

    Map<String, GenArtTxt> genArtMap = getCacheGenArt(request);

    List<ArticleDocDto> result = mapArticleRequest(request, articleMap, genArtMap);

    return ofResult(new PageImpl<>(result, pageable, result.size()),
        buildArticleFilterItems(response.getAggregations(), filterMode));
  }

  private Map<String, GenArtTxt> getCacheGenArt(ArticleFilterRequest request) {
    List<String> gaIds = request.getAccessorySearchRequest().getAccessoryList().stream()
        .map(ArticleAccessoryDto::getAccesoryListItems)
        .flatMap(List::stream)
        .map(ArticleAccessoryItemDto::getGaid).collect(Collectors.toList());
    return genArtCacheService.searchGenArtByIds(gaIds);
  }

  private List<ArticleDocDto> mapArticleRequest(ArticleFilterRequest request,
      Map<String, ArticleDocDto> articleMap, Map<String, GenArtTxt> genArtTxtMap) {

    Map<Integer, VehicleDoc> vehicleKtypeMap = getVehicleByLinkVal(request);
    Map<Integer, String> linkTextMap =
        request.getAccessorySearchRequest().getAccessoryList().stream().map(accessory -> {
          accessory.setAccesoryLinkText(
              getLinkText(accessory.getLinkType(), accessory.getLinkVal(), vehicleKtypeMap));
          return accessory;
        }).collect(Collectors.toMap(ArticleAccessoryDto::getSeqNo,
            ArticleAccessoryDto::getAccesoryLinkText));

    Map<Integer, String> listTextMap = request.getAccessorySearchRequest().getAccessoryList()
        .stream().collect(Collectors.toMap(ArticleAccessoryDto::getSeqNo,
            artAccessory -> StringUtils.defaultIfBlank(artAccessory.getAccesoryListsText(),
              StringUtils.EMPTY)));

    Map<Integer, List<ArticleAccessoryItemDto>> mapSeqAccessoryItems =
        request.getAccessorySearchRequest().getAccessoryList().stream().collect(Collectors
            .toMap(ArticleAccessoryDto::getSeqNo,
              artAccessory -> ListUtils.emptyIfNull(artAccessory.getAccesoryListItems())));

    List<ArticleDocDto> result = new LinkedList<>();
    mapSeqAccessoryItems.forEach(
        accessoryListBindingProcessor(articleMap, genArtTxtMap, linkTextMap, listTextMap, result));
    return result;
  }

  private BiConsumer<Integer, List<ArticleAccessoryItemDto>> accessoryListBindingProcessor(
      Map<String, ArticleDocDto> articleMap, Map<String, GenArtTxt> genArtTxtMap,
      Map<Integer, String> linkTextMap, Map<Integer, String> listTextMap,
      List<ArticleDocDto> result) {
    return (seqNo, accessoryItems) -> accessoryItems.forEach(accessoryItem -> {
      ArticleDocDto articleErp =
          articleMap.get(accessoryItem.getAccessoryArticleIdArt().toString());
      Optional.ofNullable(articleErp).map(artErp -> {
        ArticleDocDto articleClone = SagBeanUtils.map(artErp, ArticleDocDto.class);

        if (StringUtils.isNotEmpty(accessoryItem.getGaid())) {
          fillGenArtTxt(articleClone, genArtTxtMap, accessoryItem.getGaid());
        }

        articleClone.setSalesQuantity(accessoryItem.getQuantity());
        articleClone.setSeqNo(seqNo);
        articleClone.setAccessoryLinkText(linkTextMap.getOrDefault(seqNo, StringUtils.EMPTY));
        articleClone.setAccesoryListsText(listTextMap.getOrDefault(seqNo, StringUtils.EMPTY));

        if (CollectionUtils.isNotEmpty(accessoryItem.getCriteria())) {
          fillCriteria(articleClone, accessoryItem.getCriteria());
        }
        return articleClone;
      }).ifPresent(result::add);
    });
  }

  private Map<Integer, VehicleDoc> getVehicleByLinkVal(ArticleFilterRequest request) {
   final List<Integer> ktypeList = request.getAccessorySearchRequest().getAccessoryList().stream()
        .filter(item -> item.getLinkType().intValue() == LINK_TYPE_VEHICLE)
        .map(ArticleAccessoryDto::getLinkVal)
        .map(Integer::valueOf).distinct().collect(Collectors.toList());

    return vehicleSearchService
        .getTopResultVehicleByKtypes(ktypeList)
        .stream().collect(Collectors.toMap(VehicleDoc::getKtType, Function.identity()));
  }

  private String getLinkText(Integer linkType, String linkVal,
      Map<Integer, VehicleDoc> vehicleLinkValMap) {
    List<String> makeIds = new LinkedList<>();

    if (!Objects.isNull(linkType) && !StringUtils.isEmpty(linkVal)) {
      switch (linkType) {
        case LINK_TYPE_MAKE:
          makeIds.add(linkVal);
          return makeCacheService.findMakesByIds(makeIds).getOrDefault(linkVal, StringUtils.EMPTY);
        case LINK_TYPE_MODEL:
          Optional<ModelItem> modelOpt = modelCacheService.getModelById(linkVal);
          if (modelOpt.isPresent()) {
            makeIds.add(modelOpt.get().getIdMake().toString());
            return StringUtils.join(new String[] {
                makeCacheService.findMakesByIds(makeIds)
                    .getOrDefault(modelOpt.get().getIdMake().toString(), StringUtils.EMPTY),
                modelOpt.get().getModel(), }, SagConstants.SPACE);
          }
          break;
        case LINK_TYPE_VEHICLE:
          VehicleDoc vehicle = vehicleLinkValMap.get(Integer.valueOf(linkVal));
          if (vehicle == null) {
            log.warn("Not found vehicle = {}", linkVal);
          }
          if (!Objects.isNull(vehicle)) {
            return StringUtils.join(new String[] { vehicle.getVehicleBrand(),
                vehicle.getVehicleModel(), vehicle.getVehicleName() }, SagConstants.SPACE);
          }
          break;
        default:
          break;
      }
    }

   return  StringUtils.EMPTY;
  }

  private void fillCriteria(ArticleDocDto artilce, List<ArticleAccessoryCriteriaDto> criteriaDtos) {
    List<ArticleCriteriaDto> criteriaConverted = criteriaDtos.stream().map(criteria -> {
      ArticleCriteriaDto criteriaDto = SagBeanUtils.map(criteria, ArticleCriteriaDto.class);
      criteriaDto.setCndech(criteria.getCn());
      return criteriaDto;
    }).collect(Collectors.toList());

    artilce.setCriteria(criteriaConverted);
  }

  private void fillGenArtTxt(ArticleDocDto artilce, Map<String, GenArtTxt> genArtTxtMap,
      String gaId) {
    GenArtTxt artTxt = genArtTxtMap.get(gaId);
    if (!Objects.isNull(artTxt)) {
      List<GenArtTxtDto> genArtTxts = new LinkedList<>();
      GenArtTxtDto txtDto = SagBeanUtils.map(artTxt, GenArtTxtDto.class);
      genArtTxts.add(txtDto);
      artilce.setGenArtTxts(genArtTxts);
      artilce.setGenArtTxtEng(txtDto);
    }
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return filterMode.isNonStoredInCached() && filterMode.isAccessorySearch();
  }

}
