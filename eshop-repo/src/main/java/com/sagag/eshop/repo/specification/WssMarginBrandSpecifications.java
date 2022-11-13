package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.WssMarginsBrand;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.criteria.WssMarginBrandSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Utilities specifications for WSS Margin Brand
 */

@UtilityClass
public class WssMarginBrandSpecifications {

  private static final String BRAND_NAME = "brandName";

  private static final String ORG_ID = "orgId";

  private static final String IS_DEFAULT = "isDefault";

  public static Specification<WssMarginsBrand> searchByCriteria(
      final WssMarginBrandSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = buildSearchPredicates(criteria, root, criteriaBuilder);
      query.orderBy(buildSearchOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchPredicates(final WssMarginBrandSearchCriteria criteria,
      final Root<WssMarginsBrand> root, final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getOrgId())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(ORG_ID), value)));

    Optional.ofNullable(criteria.getBrandName()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.like(root.get(BRAND_NAME).as(String.class),
            SpecificationUtils.appendLikeText(value.trim()))));

    // #3213 default WSS margin brand is not include in search result
    predicates.add(criteriaBuilder.isFalse(root.get(IS_DEFAULT).as(Boolean.class)));

    return predicates;
  }

  private static List<Order> buildSearchOrders(final WssMarginBrandSearchCriteria criteria,
      final Root<WssMarginsBrand> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByBrandName()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(BRAND_NAME), order)));

    return orders;
  }

}
