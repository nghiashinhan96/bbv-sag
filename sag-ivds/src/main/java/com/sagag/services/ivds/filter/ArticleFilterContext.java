package com.sagag.services.ivds.filter;

import com.google.common.collect.Maps;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.domain.BrandSortingCriteria;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagCollectionUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.elasticsearch.api.ExternalPartsSearchService;
import com.sagag.services.elasticsearch.criteria.ExternalPartsSearchCriteria;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.ExternalPartsResponse;
import com.sagag.services.elasticsearch.extractor.SagBucket;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.ivds.api.impl.ArticleProcessor;
import com.sagag.services.ivds.converter.article.ExternalPartArticleConverter;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.aggregation.impl.GenArtAndSubAggregationResultBuilderImpl;
import com.sagag.services.ivds.filter.aggregation.impl.SupplierAggregationResultBuilderImpl;
import com.sagag.services.ivds.filter.articles.ArticleFilterFactory;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Some functions to support build article filtering.
 */
@Slf4j
public abstract class ArticleFilterContext extends ArticleProcessor {

  private static int DEFAULT_PRIORITY = 99;

  @Autowired
  protected IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private ArticleFilterFactory articleFilterFactory;

  @Autowired
  private GenArtAndSubAggregationResultBuilderImpl genArtAndSubAggResultBuilder;

  @Autowired
  private SupplierAggregationResultBuilderImpl supplierAggregationResultBuilder;

  @Autowired
  private BrandPriorityCacheService brandPriorityCacheService;

  @Autowired
  private ExternalPartArticleConverter externalPartConverter;

  @Autowired
  private ExternalPartsSearchService externalPartsSearchService;

  /**
   * Returns the filtered articles by search request.
   *
   * @param user       the current user login
   * @param filterMode the filter mode
   * @param request    the filtering request
   * @param pageable   the paging object
   * @param additional the additional object
   * @return the result of {@link ArticleFilteringResponseDto}
   */
  public abstract ArticleFilteringResponseDto execute(final UserInfo user,
      final FilterMode filterMode, final ArticleFilterRequest request, final Pageable pageable,
      final Optional<AdditionalSearchCriteria> additional);

  /**
   * Returns the flag of filter supported mode.
   *
   * @param filterMode
   * @return the flag result
   */
  public abstract boolean supportMode(FilterMode filterMode);

  protected FilteredArticleAndAggregationResponse getArticleFilterResponse(UserInfo user,
      FilterMode filterMode, ArticleFilterRequest request, Pageable pageable) {
    final ArticleFilteringResponse esResponse = articleFilterFactory.getArticleFilter(filterMode)
        .filterArticles(request, pageable, findEsAffilateNameLocks(user), user.isSaleOnBehalf());

    final FilteredArticleAndAggregationResponse response =
        new FilteredArticleAndAggregationResponse();
    response.setAggregations(esResponse.getAggregations());
    response.setArticles(esResponse.getArticles().map(articleConverter));

    return response;
  }

  protected List<ArticleDocDto> searchAndCombineExternalParts(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      FilteredArticleAndAggregationResponse directMatches,
      FilteredArticleAndAggregationResponse referenceMatches) {
    if (!user.isUsedExternalPart()) {
      return combineExternalParts(directMatches, referenceMatches,
          Collections.emptyList());
    }

    // Update has previous data flag.
    request.setHasPreviousData(directMatches.hasArticles() && referenceMatches.hasArticles());

    final FilteredArticleAndAggregationResponse externalPartsResponse =
        searchExternalParts(request);

    final List<ArticleDocDto> externalParts = new ArrayList<>(
        externalPartsResponse.getArticles().getContent());
    if (!user.isShownInReferenceGroup() || !filterMode.isExternalParts()) {
      return combineExternalParts(directMatches, referenceMatches, externalParts);
    }
    final List<ArticleDocDto> extPartsClone = externalParts
        .stream()
        .map(SerializationUtils::clone)
        .peek(extArt -> extArt.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH))
        .collect(Collectors.toList());

    if (referenceMatches.hasArticles()) {
      extPartsClone.addAll(referenceMatches.getArticles().getContent());
    }
    referenceMatches.setArticles(new PageImpl<>(extPartsClone, pageable, extPartsClone.size()));

    return combineExternalParts(directMatches, referenceMatches, externalParts);
  }

  private static List<ArticleDocDto> combineExternalParts(
      FilteredArticleAndAggregationResponse directMatches,
      FilteredArticleAndAggregationResponse referenceMatches, List<ArticleDocDto> externalParts) {
    final Predicate<ArticleDocDto> distinctByKeys;
    if (CollectionUtils.isEmpty(externalParts)) {
      distinctByKeys = SagCollectionUtils.distinctByKeys(ArticleDocDto::getIdSagsys);
    } else {
      distinctByKeys = SagCollectionUtils.distinctByKeys(ArticleDocDto::getIdSagsys,
          ArticleDocDto::getRelevanceGroupType);
    }
    final List<ArticleDocDto> filteredDuplicatedArticles =
        filterDuplicatedDirectAndReferenceMatches(directMatches.getArticles().getContent(),
            referenceMatches.getArticles().getContent());

    final List<ArticleDocDto> directMatchedArticles = filteredDuplicatedArticles.stream()
        .filter(art -> RelevanceGroupType.DIRECT_MATCH.equals(art.getRelevanceGroupType()))
        .collect(Collectors.toList());

    final List<ArticleDocDto> referenceMatchedArticles = filteredDuplicatedArticles.stream()
        .filter(art -> RelevanceGroupType.REFERENCE_MATCH.equals(art.getRelevanceGroupType()))
        .collect(Collectors.toList());

    return Stream.of(directMatchedArticles, ListUtils.emptyIfNull(externalParts),
            referenceMatchedArticles)
        .flatMap(Collection::stream)
        .filter(distinctByKeys)
        .collect(Collectors.toList());
  }

  private static List<ArticleDocDto> filterDuplicatedDirectAndReferenceMatches(
      List<ArticleDocDto> directMatches, List<ArticleDocDto> referenceMatches) {
    return Stream.of(directMatches, referenceMatches)
        .flatMap(Collection::stream)
        .filter(SagCollectionUtils.distinctByKeys(ArticleDocDto::getIdSagsys))
        .collect(Collectors.toList());
  }

  private FilteredArticleAndAggregationResponse searchExternalParts(
      ArticleFilterRequest request) {
    final ExternalPartsSearchCriteria criteria =
        new ExternalPartsSearchCriteria(request.getKeyword());
    final ExternalPartsResponse esResponse = externalPartsSearchService.searchByCriteria(criteria);

    final FilteredArticleAndAggregationResponse response =
        new FilteredArticleAndAggregationResponse();
    response.setAggregations(esResponse.getAggregations());
    response.setArticles(esResponse.getExternalParts().map(externalPartConverter));
    return response;
  }

  public List<ArticleDocDto> findAndSortStock(UserInfo user, Page<ArticleDocDto> articles,
      Optional<AdditionalSearchCriteria> additional) {
    return ivdsArticleTaskExecutors
        .executeTaskStockOnlyWithoutVehicle(user, articles, additional).getContent();
  }

  public Map<String, List<ArticleFilterItem>> buildSubArticleFilterItems(
      Map<String, List<SagBucket>> aggregations) {
    log.debug("Building article filter items with builder implementation = {}",
        genArtAndSubAggResultBuilder);
    if (MapUtils.isEmpty(aggregations)) {
      return Maps.newHashMap();
    }
    final Map<String, List<ArticleFilterItem>> filters = Maps.newHashMap();
    genArtAndSubAggResultBuilder.buildAggregationResult(filters, aggregations);
    supplierAggregationResultBuilder.buildAggregationResult(filters, aggregations);
    return filters;
  }

  public Map<String, List<ArticleFilterItem>> buildArticleFilterItems(
      final Map<String, List<SagBucket>> aggregations, FilterMode mode) {
    log.debug("Building article filter items by default builder implementation");
    if (MapUtils.isEmpty(aggregations)) {
      return Collections.emptyMap();
    }
    final Map<String, List<ArticleFilterItem>> filters = new HashMap<>();
    articleFilterFactory.getAggregationResultBuilder(mode)
        .forEach(builder -> builder.buildAggregationResult(filters, aggregations));
    return filters;
  }

  public static ArticleFilteringResponseDto emptyResult(String contextKey) {
    return ArticleFilteringResponseDto.builder().contextKey(contextKey)
        .articles(Page.empty()).filters(Collections.emptyMap()).build();
  }

  public static ArticleFilteringResponseDto ofResult(Page<ArticleDocDto> articles,
      Map<String, List<ArticleFilterItem>> filters, String contextKey) {
    return ArticleFilteringResponseDto.builder().contextKey(contextKey).articles(articles)
        .availabilityCurrentPageNr(0).filters(filters).build();
  }

  public static ArticleFilteringResponseDto ofResult(Page<ArticleDocDto> articles,
      Map<String, List<ArticleFilterItem>> filters, String contextKey,
      int availabilityCurrentPageNr) {
    return ArticleFilteringResponseDto.builder().contextKey(contextKey).articles(articles)
        .availabilityCurrentPageNr(availabilityCurrentPageNr).filters(filters).build();
  }

  public static ArticleFilteringResponseDto ofResult(Page<ArticleDocDto> articles,
      Map<String, List<ArticleFilterItem>> filters) {
    return ArticleFilteringResponseDto.builder().articles(articles).filters(filters).build();
  }

  public static Page<ArticleDocDto> buildPaging(List<ArticleDocDto> articles, Pageable pageable) {
    if (CollectionUtils.isEmpty(articles)) {
      return Page.empty(pageable);
    }

    final List<ArticleDocDto> allArticles = new ArrayList<>();
    final int pageSize = pageable.getPageSize();
    final int size = articles.size();
    final int startIndex = pageable.getPageNumber() * pageSize;
    if (!CollectionUtils.isEmpty(articles) && startIndex < size) {
      int endIndex = Math.min(startIndex + pageSize, size);
      allArticles.addAll(articles.subList(startIndex, endIndex));
    }

    return new PageImpl<>(allArticles, pageable, articles.size());
  }

  /**
   * Sort articles by stock
   * @param source the articles
   * @return the sorted articles
   */
  public List<ArticleDocDto> sortArticleStockDesc(List<ArticleDocDto> source) {
    List<ArticleDocDto> directList = source.stream()
        .filter(art -> RelevanceGroupType.DIRECT_MATCH.equals(art.getRelevanceGroupType()))
        .collect(Collectors.toList());
    List<ArticleDocDto> unsortedList = source.stream()
        .filter(art -> Objects.isNull(art.getDeliverableStock())
            && !RelevanceGroupType.DIRECT_MATCH.equals(art.getRelevanceGroupType()))
        .collect(Collectors.toList());
    List<ArticleDocDto> sortedList = source.stream()
        .filter(art -> Objects.nonNull(art.getDeliverableStock())
            && !RelevanceGroupType.DIRECT_MATCH.equals(art.getRelevanceGroupType()))
        .sorted((art1, art2) -> Double.compare(art2.getDeliverableStock(),
            art1.getDeliverableStock()))
        .collect(Collectors.toList());
    return Stream.of(directList, sortedList, unsortedList)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * #7342 Add Brand (priority) sorting to article search results
   * @param user the user info
   * @param source the articles
   * @return sorted articles
   */
  public List<ArticleDocDto> sortArticlesBrandPriority(UserInfo user, List<ArticleDocDto> source) {
    List<ArticleDocDto> directList = source.stream()
        .filter(art -> RelevanceGroupType.DIRECT_MATCH.equals(art.getRelevanceGroupType()))
        .collect(Collectors.toList());
    List<ArticleDocDto> extPartList = source.stream()
        .filter(extPart -> RelevanceGroupType.ORIGINAL_PART.equals(extPart.getRelevanceGroupType()))
        .collect(Collectors.toList());
    // Only apply brand sorting for reference-matches
    List<ArticleDocDto> sortedList = sortBrands(user, source.stream()
        .filter(art -> RelevanceGroupType.REFERENCE_MATCH.equals(art.getRelevanceGroupType()))
        .collect(Collectors.toList()));
    return Stream.of(directList, extPartList, sortedList)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  protected List<ArticleDocDto> sortBrands(UserInfo user, List<ArticleDocDto> source) {
    /*
     * 1. Get brand priority information
     * 2. Group by GaId and sort ASC
     * 3. Loop each GaId, split all brands into 2 groups
     *    - Group 1: has stock brand
     *    - Group 2: no stock brand
     * 4. Execute brand sorting and combine into 1 list (has-stock and no-stock)
     */

    // 1. Get brand priority information
    Map<String, CachedBrandPriorityDto> brandPriorities = brandPriorityCacheService
        .findCachedBrandPriority(source.stream().map(ArticleDocDto::getGaId).collect(Collectors.toList()));
    SupportedAffiliate spAffiliate = SupportedAffiliate.fromCompanyName(user.getCompanyName());
    final String affiliate = Objects.nonNull(spAffiliate) ? spAffiliate.getEsShortName() : StringUtils.EMPTY;
    final String collection = user.getCollectionName();

    // 2. Group by GaId and sort ASC
    Map<String, List<ArticleDocDto>> gaIdMap = source.stream()
        .collect(Collectors.groupingBy(ArticleDocDto::getGaId)).entrySet()
        .stream().sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    // 3. Loop each GaId, split all brands into 2 groups
    List<ArticleDocDto> finalList = new ArrayList<>();
    for (Map.Entry<String, List<ArticleDocDto>> gaIdEntry : gaIdMap.entrySet()) {
      List<ArticleDocDto> gaIdList = gaIdEntry.getValue();
      CachedBrandPriorityDto brandPriorityDto = brandPriorities.get(gaIdEntry.getKey());
      Map<String, List<ArticleDocDto>> brandMap = gaIdList.stream()
          .collect(Collectors.groupingBy(ArticleDocDto::getIdProductBrand));

      List<List<ArticleDocDto>> hasStockList = new ArrayList<>();
      List<List<ArticleDocDto>> noStockList = new ArrayList<>();
      for (Map.Entry<String, List<ArticleDocDto>> brandEntry : brandMap.entrySet()) {
        List<ArticleDocDto> articleList = brandEntry.getValue()
            .stream().sorted(Comparator.comparing(ArticleDocDto::getDeliverableStock).reversed())
            .collect(Collectors.toList());
        if (articleList.stream().anyMatch(art -> art.getDeliverableStock() > 0)) {
          hasStockList.add(articleList);
        } else {
          noStockList.add(articleList);
        }
      }
      //  4. Execute brand sorting and combine into 1 list (has-stock and no-stock)
      finalList.addAll(hasStockList.stream().sorted(this.compareBrandGroup(affiliate, collection, brandPriorityDto))
          .flatMap(List::stream).collect(Collectors.toList()));
      finalList.addAll(noStockList.stream().sorted(this.compareBrandGroup(affiliate, collection, brandPriorityDto))
          .flatMap(List::stream).collect(Collectors.toList()));

    }
    return finalList;
  }

  private Comparator<List<ArticleDocDto>> compareBrandGroup(String affiliate, String collection,
      CachedBrandPriorityDto priority) {
    /*
     * Brand sorting criteria:
     * 1. Priority
     * 2. Sub-priority
     * 3. Stock
     */
    return (articles1, articles2) -> {
      if (CollectionUtils.isEmpty(articles1) && CollectionUtils.isEmpty(articles2)) {
        return 0;
      }
      if (CollectionUtils.isEmpty(articles1)) {
        return -1;
      }
      if (CollectionUtils.isEmpty(articles2)) {
        return 1;
      }

      // Build sorting criteria
      BrandSortingCriteria criteria1 = buildBrandCriteria(priority, articles1, affiliate, collection);
      BrandSortingCriteria criteria2 = buildBrandCriteria(priority, articles2, affiliate, collection);

      // Compare brand by criteria
      if (criteria1.getPriority() > criteria2.getPriority()) {
        return 1;
      } else if (criteria1.getPriority() < criteria2.getPriority()) {
        return -1;
      } else if (criteria1.getSubPriority() > criteria2.getSubPriority()) {
        return 1;
      } else if (criteria1.getSubPriority() < criteria2.getSubPriority()) {
        return -1;
      } else {
        return Double.compare(criteria2.getMaxStock(), criteria1.getMaxStock());
      }
    };
  }

  private BrandSortingCriteria buildBrandCriteria(CachedBrandPriorityDto brandPriority,
      List<ArticleDocDto> articles, String affiliate, String collection) {
    // Filter brand from brand priority
    String brandId = articles.stream().findFirst().orElse(new ArticleDocDto()).getIdProductBrand();
    BrandDto brand = Optional.ofNullable(brandPriority)
        .map(CachedBrandPriorityDto::getSorts).orElse(Lists.newArrayList()).stream()
        .flatMap(sortDto -> sortDto.getBrands().stream())
        .filter(brandDto -> brandDto.getBrand().equals(brandId)).filter(brandDto -> {
          if (collection.equals(brandDto.getCollection())) {
            return true;
          } else {
            return affiliate.equals(brandDto.getAffiliate());
          }
        }).findFirst().orElse(new BrandDto());

    // Calculate brand criteria
    int priority = DEFAULT_PRIORITY;
    int subPriority = DEFAULT_PRIORITY;
    if (Objects.nonNull(brand.getSorts())) {
      priority = brand.getSorts().getOrDefault(1, DEFAULT_PRIORITY);
      subPriority = brand.getSorts().getOrDefault(2, DEFAULT_PRIORITY);
    }
    double maxStock = articles.stream()
        .mapToDouble(ArticleDocDto::getDeliverableStock)
        .max().orElse(0.0);

    return BrandSortingCriteria.builder()
        .priority(priority)
        .subPriority(subPriority)
        .maxStock(maxStock).build();
  }
}
