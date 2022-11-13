package com.sagag.services.elasticsearch.utils;

import com.google.common.collect.Lists;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.enums.ArticleField;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class UniversalPartQueryUtils {

  private static final int ARTCLE_GROUP_2_LENGTH = 1;
  private static final int ARTICLE_GROUP_3_LENGTH = 2;

  public static Map<ArticleField, List<String>> extractArticleGroupByLevel(
      List<String> articleGroups) {
    final List<String> group1 = articleGroups.stream()
        .filter(articleGroup -> !StringUtils.contains(articleGroup, SagConstants.HYPHEN))
        .collect(Collectors.toList());

    Map<ArticleField, List<String>> productGroupMap = new HashMap<>();
    productGroupMap.put(ArticleField.PRODUCT_GROUP, group1);
    productGroupMap.put(ArticleField.PRODUCT_GROUP_2, Lists.newArrayList());
    productGroupMap.put(ArticleField.PRODUCT_GROUP_3, Lists.newArrayList());
    productGroupMap.put(ArticleField.PRODUCT_GROUP_4, Lists.newArrayList());

    final List<String> otherGroup = articleGroups.stream()
        .filter(articleGroup -> StringUtils.contains(articleGroup, SagConstants.HYPHEN))
        .collect(Collectors.toList());
    otherGroup.parallelStream().forEach(articleGroup -> {
      int subProductGroupLength = StringUtils.split(articleGroup, SagConstants.HYPHEN)[1].length();
      if (subProductGroupLength == ARTCLE_GROUP_2_LENGTH) {
        productGroupMap.get(ArticleField.PRODUCT_GROUP_2).add(articleGroup);
      }
      if (subProductGroupLength == ARTICLE_GROUP_3_LENGTH) {
        productGroupMap.get(ArticleField.PRODUCT_GROUP_3).add(articleGroup);
      }
      if (subProductGroupLength > ARTICLE_GROUP_3_LENGTH) {
        productGroupMap.get(ArticleField.PRODUCT_GROUP_4).add(articleGroup);
      }
    });
    return productGroupMap;
  }
}
