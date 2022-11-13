package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.branch.dto.BranchSearchRequestCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Utilities specifications for Branch
 */

@UtilityClass
public class BranchSpecifications {

  private static final String BRANCH_NR = "branchNr";

  private static final String BRANCH_CODE = "branchCode";

  private static final String OPENING_TIME = "openingTime";

  private static final String CLOSING_TIME = "closingTime";

  private static final String LUNCH_START_TIME = "lunchStartTime";

  private static final String LUNCH_END_TIME = "lunchEndTime";

  private static final String HIDE_FROM_CUSTOMERS = "hideFromCustomers";

  private static final String HIDE_FROM_SALES = "hideFromSales";

  public static Specification<Branch> searchBranchByCriteria(
      final BranchSearchRequestCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchBranchesPredicates(criteria, root, criteriaBuilder);

      // Order by criteria
      query.orderBy(buildSearchBranchesOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchBranchesPredicates(
      final BranchSearchRequestCriteria criteria, final Root<Branch> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    // Search branch number
    Optional.ofNullable(criteria.getBranchNr()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, BRANCH_NR),
            SpecificationUtils.appendLikeText(value.trim()))));

    // Search branch code
    Optional.ofNullable(criteria.getBranchCode()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, BRANCH_CODE),
            SpecificationUtils.appendLikeText(value.trim()))));

    // Search opening time
    Optional.ofNullable(criteria.getOpeningTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, OPENING_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    // Search closing time
    Optional.ofNullable(criteria.getClosingTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, CLOSING_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    // Search lunch start time
    Optional.ofNullable(criteria.getLunchStartTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, LUNCH_START_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    // Search lunch end time
    Optional.ofNullable(criteria.getLunchEndTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, LUNCH_END_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    // search hide from customers
    Optional.ofNullable(criteria.getHideFromCustomers()).filter(Boolean::booleanValue)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.isFalse(root.get(HIDE_FROM_CUSTOMERS).as(Boolean.class))));

    // search hide from sales
    Optional.ofNullable(criteria.getHideFromSales()).filter(Boolean::booleanValue)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.isFalse(root.get(HIDE_FROM_SALES).as(Boolean.class))));

    return predicates;
  }

  private static Expression<String> buildStringExpr(final Root<Branch> root, final String field) {
    return root.get(field).as(String.class);
  }

  private static List<Order> buildSearchBranchesOrders(final BranchSearchRequestCriteria criteria,
      final Root<Branch> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    // Order by branch number
    Optional.ofNullable(criteria.getOrderDescByBranchNr()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(BRANCH_NR), order)));

    // Order by branch code
    Optional.ofNullable(criteria.getOrderDescByBranchCode()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(BRANCH_CODE), order)));

    // Order by branch opening time
    Optional.ofNullable(criteria.getOrderDescByOpeningTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(OPENING_TIME), order)));

    // Order by branch closing time
    Optional.ofNullable(criteria.getOrderDescByClosingTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CLOSING_TIME), order)));

    // Order by branch lunch start time
    Optional.ofNullable(criteria.getOrderDescByLunchStartTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders.add(
            SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LUNCH_START_TIME), order)));

    // Order by branch lunch end time
    Optional.ofNullable(criteria.getOrderDescByLunchEndTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders.add(
            SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LUNCH_END_TIME), order)));

    return orders;
  }
}
