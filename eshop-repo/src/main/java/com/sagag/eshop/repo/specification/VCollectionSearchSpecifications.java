package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.VCollectionSearch;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.criteria.CollectionSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specifications for VCOLLECTION_SEARCH
 */

@UtilityClass
public class VCollectionSearchSpecifications {

  private static final String AFFILIATE = "affiliate";
  private static final String COLLECTION_NAME = "collectionName";

  public static Specification<VCollectionSearch> searchCollections(
      CollectionSearchCriteria criteria, List<String> collectionsByCustomerNr) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchCollectionConditions(criteria, root, criteriaBuilder, collectionsByCustomerNr);

      query.orderBy(buildSearchCollectionOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchCollectionConditions(CollectionSearchCriteria criteria,
      Root<VCollectionSearch> root, CriteriaBuilder criteriaBuilder,
      List<String> collectionsByCustomerNr) {
    final List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(criteria.getAffiliate())) {
      predicates.add(criteriaBuilder.equal(root.get(AFFILIATE), criteria.getAffiliate()));
    }
    if (StringUtils.isNotBlank(criteria.getCollectionName())) {
      predicates.add(criteriaBuilder.like(root.get(COLLECTION_NAME),
          SpecificationUtils.appendLikeText(criteria.getCollectionName())));
    }
    if (!CollectionUtils.isEmpty(collectionsByCustomerNr)) {
      predicates.add(root.get(COLLECTION_NAME).in(collectionsByCustomerNr));
    }

    return predicates;
  }

  private static List<Order> buildSearchCollectionOrders(CollectionSearchCriteria criteria,
      Root<VCollectionSearch> root, CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();
    if (Objects.isNull(criteria.getSorting())) {
      return Collections.emptyList();
    }
    if (!Objects.isNull(criteria.getSorting().getOrderByAffiliateNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(AFFILIATE),
          criteria.getSorting().getOrderByAffiliateNameDesc()));
    }
    if (!Objects.isNull(criteria.getSorting().getOrderByCollectionNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(COLLECTION_NAME),
          criteria.getSorting().getOrderByCollectionNameDesc()));
    }
    return orders;
  }
}
