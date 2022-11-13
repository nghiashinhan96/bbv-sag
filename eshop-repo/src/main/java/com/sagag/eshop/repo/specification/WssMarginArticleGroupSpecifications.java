package com.sagag.eshop.repo.specification;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.criteria.WssMarginArticleGroupSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Utilities specifications for WSS Margin Article Group
 */

@UtilityClass
public class WssMarginArticleGroupSpecifications {

  private static final String SAG_ARTICLE_GROUP = "sagArtGroup";
  private static final String SAG_ARTICLE_GROUP_DESC = "sagArticleGroupDesc";
  private static final String CUSTOM_ARTICLE_GROUP = "customArticleGroup";
  private static final String CUSTOM_ARTICLE_GROUP_DESC = "customArticleGroupDesc";

  private static final String ORG_ID = "orgId";
  private static final String IS_DEFAULT = "isDefault";

  public static Specification<WssMarginsArticleGroup> searchByCriteria(
      final WssMarginArticleGroupSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = buildSearchPredicates(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchOrders(root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchPredicates(
      final WssMarginArticleGroupSearchCriteria criteria, final Root<WssMarginsArticleGroup> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getOrgId())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(ORG_ID), value)));

    Optional.ofNullable(criteria.getSagArticleGroup()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.like(root.get(SAG_ARTICLE_GROUP).as(String.class),
            SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getSagArticleGroupDesc()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.like(root.get(SAG_ARTICLE_GROUP_DESC).as(String.class),
                SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getCustomArticleGroup()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.like(root.get(CUSTOM_ARTICLE_GROUP).as(String.class),
                SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getCustomArticleGroupDesc()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.like(root.get(CUSTOM_ARTICLE_GROUP_DESC).as(String.class),
                SpecificationUtils.appendLikeText(value.trim()))));

    predicates.add(criteriaBuilder.isFalse(root.get(IS_DEFAULT).as(Boolean.class)));

    return predicates;
  }

  private static List<Order> buildSearchOrders(final Root<WssMarginsArticleGroup> root,
      final CriteriaBuilder criteriaBuilder) {
    return Lists.newArrayList(
        SpecificationUtils.defaultOrder(criteriaBuilder, root.get(SAG_ARTICLE_GROUP), false));
  }
}
