package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.WssDeliveryProfile;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.criteria.WssDeliveryProfileSearchRequestCriteria;

import lombok.experimental.UtilityClass;

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
 * Utilities specifications for WSS Delivery Profile
 */

@UtilityClass
public class WssDeliveryProfileSpecifications {

  private static final String PROFILE_NAME = "name";

  private static final String PROFILE_DESCRIPTION = "description";

  private static final String BRANCH_CODE = "branchCode";

  private static final String WSS_BRANCH = "wssBranch";

  private static final String ORG_ID = "orgId";

  public static Specification<WssDeliveryProfile> searchDeliveryProfileByCriteria(
      final WssDeliveryProfileSearchRequestCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchDeliveryProfilePredicates(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchDeliveryProfileOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchDeliveryProfilePredicates(
      final WssDeliveryProfileSearchRequestCriteria criteria, final Root<WssDeliveryProfile> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getOrgId())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(ORG_ID), value)));

    return predicates;
  }

  private static List<Order> buildSearchDeliveryProfileOrders(
      final WssDeliveryProfileSearchRequestCriteria criteria, final Root<WssDeliveryProfile> root,
      final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByProfileName()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(PROFILE_NAME), order)));

    Optional.ofNullable(criteria.getOrderDescByProfileDescription()).filter(Objects::nonNull)
        .ifPresent(order -> orders.add(SpecificationUtils.defaultOrder(criteriaBuilder,
            root.get(PROFILE_DESCRIPTION), order)));

    Optional.ofNullable(criteria.getOrderDescByBranchCode()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(WSS_BRANCH).get(BRANCH_CODE), order)));

    return orders;
  }
}
