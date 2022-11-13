package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.WssTour;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.tour.dto.WssTourSearchRequestCriteria;

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
 * Utilities specifications for WSS Tour
 */

@UtilityClass
public class WssTourSpecifications {

  private static final String TOUR_NAME = "name";

  private static final String ORG_ID = "orgId";

  public static Specification<WssTour> searchTourByCriteria(
      final WssTourSearchRequestCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchToursPredicates(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchToursOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchToursPredicates(
      final WssTourSearchRequestCriteria criteria, final Root<WssTour> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getOrgId())
        .ifPresent(value -> predicates.add(criteriaBuilder.equal(root.get(ORG_ID), value)));

    return predicates;
  }

  private static List<Order> buildSearchToursOrders(final WssTourSearchRequestCriteria criteria,
      final Root<WssTour> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByTourName()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(TOUR_NAME), order)));

    return orders;
  }
}
