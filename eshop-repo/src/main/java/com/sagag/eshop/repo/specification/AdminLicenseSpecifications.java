package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.License;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.LicenseSearchCriteria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utilities specifications for admin license
 */

@UtilityClass
public class AdminLicenseSpecifications {

  private static final String CUSTOMER_NR = "customerNr";
  private static final String PACK_NAME = "packName";
  private static final String TYPE_OF_LICENSE = "typeOfLicense";
  private static final String BEGIN_DATE = "beginDate";
  private static final String END_DATE = "endDate";
  private static final String QUANTITY = "quantity";
  private static final String QUANTITY_USED = "quantityUsed";

  public static Specification<License> searchByCriteria(final LicenseSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = buildSearchLicensesPredicates(criteria, root, criteriaBuilder);
      // Order by criteria
      query.orderBy(buildSearchLicenseOrders(criteria, root, criteriaBuilder));
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  private static List<Predicate> buildSearchLicensesPredicates(final LicenseSearchCriteria criteria,
      final Root<License> root, final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();
    Optional.ofNullable(criteria.getCustomerNr()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, CUSTOMER_NR),
            SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getPackName()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, PACK_NAME),
            SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getTypeOfLicense()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, TYPE_OF_LICENSE),
            SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getBeginDate()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(BEGIN_DATE),
            DateUtils.toDate(criteria.getBeginDate(), DateUtils.DEFAULT_DATE_PATTERN))));

    Optional.ofNullable(criteria.getEndDate()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(END_DATE),
            DateUtils.toDate(criteria.getEndDate(), DateUtils.DEFAULT_DATE_PATTERN))));

    Optional.ofNullable(criteria.getQuantity())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(QUANTITY), criteria.getQuantity())));

    Optional.ofNullable(criteria.getQuantityUsed())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(QUANTITY_USED), criteria.getQuantityUsed())));

    return predicates;
  }

  private static Expression<String> buildStringExpr(final Root<License> root, final String field) {
    return root.get(field).as(String.class);
  }

  private static List<Order> buildSearchLicenseOrders(final LicenseSearchCriteria criteria, final Root<License> root,
      final CriteriaBuilder criteriaBuilder) {
    if (Objects.isNull(criteria.getSorting())) {
      return Collections.emptyList();
    }

    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getSorting().getOrderByCustomerNrDesc())
        .ifPresent(order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CUSTOMER_NR), order)));

    Optional.ofNullable(criteria.getSorting().getOrderByPackNameDesc())
        .ifPresent(order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(PACK_NAME), order)));

    Optional.ofNullable(criteria.getSorting().getOrderByTypeOfLicenseDesc()).ifPresent(
        order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(TYPE_OF_LICENSE), order)));

    Optional.ofNullable(criteria.getSorting().getOrderByQuantityDesc())
        .ifPresent(order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(QUANTITY), order)));

    Optional.ofNullable(criteria.getSorting().getOrderByQuantityUsedDesc()).ifPresent(
        order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(QUANTITY_USED), order)));

    Optional.ofNullable(criteria.getSorting().getOrderByBeginDateDesc())
        .ifPresent(order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(BEGIN_DATE), order)));

    Optional.ofNullable(criteria.getSorting().getOrderByEndDateDesc())
        .ifPresent(order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(END_DATE), order)));

    return orders;
  }
}
