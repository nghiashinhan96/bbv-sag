package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.branch.dto.WssBranchSearchRequestCriteria;

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
 * Utilities specifications for WSS Branch
 */

@UtilityClass
public class WssBranchSpecifications {

  private static final String BRANCH_NR = "branchNr";

  private static final String BRANCH_CODE = "branchCode";

  private static final String OPENING_TIME = "openingTime";

  private static final String CLOSING_TIME = "closingTime";

  private static final String LUNCH_START_TIME = "lunchStartTime";

  private static final String LUNCH_END_TIME = "lunchEndTime";

  private static final String ORG_ID = "orgId";

  public static Specification<WssBranch> searchBranchByCriteria(
      final WssBranchSearchRequestCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchBranchesPredicates(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchBranchesOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchBranchesPredicates(
      final WssBranchSearchRequestCriteria criteria, final Root<WssBranch> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getBranchNr()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, BRANCH_NR),
            SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getBranchCode()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, BRANCH_CODE),
            SpecificationUtils.appendLikeText(value.trim()))));

    Optional.ofNullable(criteria.getOpeningTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, OPENING_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    Optional.ofNullable(criteria.getClosingTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, CLOSING_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    Optional.ofNullable(criteria.getLunchStartTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, LUNCH_START_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    Optional.ofNullable(criteria.getLunchEndTime()).filter(StringUtils::isNotBlank).ifPresent(
        value -> predicates.add(criteriaBuilder.equal(buildStringExpr(root, LUNCH_END_TIME),
            DateUtils.formatTimeStr(value.trim(), DateUtils.TIME_PATTERN))));

    Optional.ofNullable(criteria.getOrgId())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(ORG_ID), value)));

    return predicates;
  }

  private static Expression<String> buildStringExpr(final Root<WssBranch> root, final String field) {
    return root.get(field).as(String.class);
  }

  private static List<Order> buildSearchBranchesOrders(final WssBranchSearchRequestCriteria criteria,
      final Root<WssBranch> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByBranchNr()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(BRANCH_NR), order)));

    Optional.ofNullable(criteria.getOrderDescByBranchCode()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(BRANCH_CODE), order)));

    Optional.ofNullable(criteria.getOrderDescByOpeningTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(OPENING_TIME), order)));

    Optional.ofNullable(criteria.getOrderDescByClosingTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CLOSING_TIME), order)));

    Optional.ofNullable(criteria.getOrderDescByLunchStartTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders.add(
            SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LUNCH_START_TIME), order)));

    Optional.ofNullable(criteria.getOrderDescByLunchEndTime()).filter(Objects::nonNull)
        .ifPresent(order -> orders.add(
            SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LUNCH_END_TIME), order)));

    return orders;
  }
}
