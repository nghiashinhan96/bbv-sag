package com.sagag.services.ivds.filter.gtmotive;

import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticlePartDto;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.gtmotive.domain.response.GtmotiveSimpleOperation;
import com.sagag.services.gtmotive.utils.GtmotiveConstant;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;
import com.sagag.services.hazelcast.api.CategoryCacheService;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NonMatchedOperationsFilter {

  @Autowired
  private CategoryCacheService cateCacheService;

  public List<GtmotiveSimpleOperation> filter(final String vehId,
      final List<ArticleDocDto> displayedArticles, final List<GtmotiveOperationItem> operations,
      final List<String> partNumbers) {

    // Get all gaids from vehicle categories
    final Set<String> allGaids = cateCacheService.findCategoriesByVehicle(vehId).stream()
        .flatMap(cate -> cate.getGenarts().stream()).map(GenArt::getGaid)
        .collect(Collectors.toSet());

    // Get non matched operations to display to client side
    final List<String> gtmotiveSelectedGaids = getGaIdsByPartList(displayedArticles);
    final Collection<CategoryDoc> nonMatchedCategories = cateCacheService.findCategoriesByGaids(
        GtmotiveUtils.nonMatchedGaIdsConverter().apply(gtmotiveSelectedGaids, allGaids));

    final List<GtmotiveSimpleOperation> nonMatchedOperations = new LinkedList<>();

    // Add non matched operations from non matched gaids without the categories of vehicle.
    nonMatchedOperations.addAll(getNonMatchedOperationsFromGaids(nonMatchedCategories));

    // Add non matched operations from gtmotive operations response
    nonMatchedOperations
        .addAll(getNonMatchedOperationsFromGtResponse(operations, partNumbers, displayedArticles));

    // Set the list of non match operations to non matched operations response
    log.debug("The list of non matched operations is {}", nonMatchedOperations);
    return nonMatchedOperations;
  }

  /**
   * Return the list of non matched operations from gtmotive response without ES articles.
   *
   * @param operations
   * @param partNumbers
   * @param articles
   * @return the list of {@link GtmotiveSimpleOperation}
   */
  private static List<GtmotiveSimpleOperation> getNonMatchedOperationsFromGtResponse(
      final List<GtmotiveOperationItem> operations, final List<String> partNumbers,
      final List<ArticleDocDto> articles) {

    if (CollectionUtils.isEmpty(operations) || CollectionUtils.isEmpty(partNumbers)) {
      return Collections.emptyList();
    }

    final Set<String> strippedPartNrs = partNumbers.stream()
      .map(GtmotiveUtils.stripStartPartNumberConverter())
      .collect(Collectors.toSet());

    // Get the set of matched part numbers from articles response by ES
    final Set<String> matchedPartNumbers = articles.stream()
        .map(art -> ListUtils.union(art.getParts(), art.getPartsExt()))
        .flatMap(Collection::stream)
        .distinct()
        .map(ArticlePartDto::getPnrn)
        .filter(strippedPartNrs::contains)
        .collect(Collectors.toSet());

    return operations.stream()
      .filter(operation -> !matchedPartNumbers.contains(StringUtils.stripStart(
        SagStringUtils.stripNonAlphaNumericChars(operation.getReference()),
        GtmotiveConstant.GT_MOTIVE_STRIP_PART_NUMBER_CHARACTER)))
      .map(operation -> GtmotiveSimpleOperation.builder().reference(operation.getReference())
        .partDescription(operation.getDescription()).build())
      .collect(Collectors.toList());
  }

  /**
   * Return the list of non matched operations from gaids without cache selected vehicle categories.
   *
   * @param nonMatchedCategories
   * @return the list of {@link GtmotiveSimpleOperation}
   */
  private static List<GtmotiveSimpleOperation> getNonMatchedOperationsFromGaids(
      final Collection<CategoryDoc> nonMatchedCategories) {
    if (CollectionUtils.isEmpty(nonMatchedCategories)) {
      return Collections.emptyList();
    }
    return nonMatchedCategories.stream().map(gtmotiveSimpleOperationConverter())
        .collect(Collectors.toList());
  }

  private static Function<CategoryDoc, GtmotiveSimpleOperation> gtmotiveSimpleOperationConverter() {
    return cateDoc -> {
      final Optional<String> firstGenArtTxt =
          ListUtils.emptyIfNull(cateDoc.getGenarts()).stream().map(GenArt::getGaid).findFirst();
      return GtmotiveSimpleOperation.builder().reference(firstGenArtTxt.orElse(StringUtils.EMPTY))
          .partDescription(Optional.of(cateDoc.getLeaftxt()).orElse(StringUtils.EMPTY)).build();
    };
  }

  /**
   * Get gaid list from the list of part result.
   *
   */
  private static List<String> getGaIdsByPartList(Collection<ArticleDocDto> partList) {
    if (CollectionUtils.isEmpty(partList)) {
      return Collections.emptyList();
    }
    return partList.stream().filter(part -> StringUtils.isNotBlank(part.getGaId()))
        .map(ArticleDocDto::getGaId).distinct().collect(Collectors.toList());
  }
}
