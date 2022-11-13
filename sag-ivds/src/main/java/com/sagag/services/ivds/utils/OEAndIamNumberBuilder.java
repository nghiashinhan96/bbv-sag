package com.sagag.services.ivds.utils;

import com.sagag.services.domain.article.ArticlePartDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Original Equipment And Independent After Market Number Builder.
 *
 */
@Component
public class OEAndIamNumberBuilder {

  public Map<String, List<String>> build(final List<ArticlePartDto> articleParts,
      final Map<String, String> branchNames) {
    if (CollectionUtils.isEmpty(articleParts) || MapUtils.isEmpty(branchNames)) {
      return Collections.emptyMap();
    }
    return articleParts.stream().filter(existedBrandIdFilter(branchNames)).distinct()
        .collect(Collectors.groupingBy(collectBrandNameKey(branchNames),
            sortedNumberTreeMapSupplier(), numbersMapping()));
  }

  private static Predicate<ArticlePartDto> existedBrandIdFilter(final Map<String, String> branchNames) {
    return part -> branchNames.containsKey(part.getBrandid());
  }

  private static Supplier<Map<String, List<String>>> sortedNumberTreeMapSupplier() {
    return () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
  }

  private static Function<ArticlePartDto, String> collectBrandNameKey(
      final Map<String, String> branchNames) {
    return part -> branchNames.get(part.getBrandid());
  }

  private static Collector<ArticlePartDto, ?, List<String>> numbersMapping() {
    return Collectors.mapping(ArticlePartDto::getPnrn, Collectors.toList());
  }
}
