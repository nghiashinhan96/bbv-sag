package com.sagag.services.elasticsearch.query.unitrees;

import com.sagag.services.elasticsearch.enums.ArticleField;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class UnitreeQueryUtils {

  public static List<String> analyzeCvp(String cvp) {
    if (StringUtils.isBlank(cvp)) {
      return Collections.emptyList();
    }
    final String lowerCaseCvp = StringUtils.lowerCase(cvp);
    final String[] analyzedCvps = StringUtils.split(lowerCaseCvp, StringUtils.SPACE);
    return Stream.of(ArrayUtils.add(analyzedCvps, lowerCaseCvp)).distinct()
        .collect(Collectors.toList());
  }

  public static List<String> analyzeCvps(List<String> cvpList) {
    if (CollectionUtils.isEmpty(cvpList)) {
      return Collections.emptyList();
    }
    cvpList.replaceAll(String::toLowerCase);

    final Set<String> originalCvps = new HashSet<>(cvpList);
    final Set<String> analyzedCvps = cvpList.stream()
      .flatMap(cvp -> Stream.of(StringUtils.split(cvp, StringUtils.SPACE)))
      .collect(Collectors.toSet());
    return SetUtils.union(originalCvps, analyzedCvps).stream().collect(Collectors.toList());
  }

  public static NativeSearchQueryBuilder nativeQueryBuilder(final Pageable pageable,
    final String... indices) {
    return new NativeSearchQueryBuilder().withIndices(indices).withPageable(pageable);
  }

  public static BiFunction<Integer, String, NestedQueryBuilder> buildCvpQuery() {
    return (cid, cvp) -> QueryBuilders.nestedQuery(ArticleField.CRITERIA.value(),
        QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery(ArticleField.CRITERIA_CID.value(), cid))
        .must(QueryBuilders.termQuery(ArticleField.CRITERIA_CVP.value(), cvp)),
      ScoreMode.None);
  }

  public static BiFunction<Integer, List<String>, NestedQueryBuilder> buildCvpListQuery() {
    return (cid, cvpList) -> QueryBuilders.nestedQuery(ArticleField.CRITERIA.value(),
        QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery(ArticleField.CRITERIA_CID.value(), cid))
        .must(QueryBuilders.termsQuery(ArticleField.CRITERIA_CVP.value(), cvpList)),
      ScoreMode.None);
  }

  public static BiFunction<Integer, List<String>, NestedQueryBuilder> buildCvpRawListQuery() {
    return (cid, cvpList) -> QueryBuilders.nestedQuery(ArticleField.CRITERIA.value(),
        QueryBuilders.boolQuery()
        .must(QueryBuilders.termQuery(ArticleField.CRITERIA_CID.value(), cid))
        .must(QueryBuilders.termsQuery(ArticleField.CRITERIA_CVP_RAW.value(), cvpList)),
      ScoreMode.None);
  }

}
