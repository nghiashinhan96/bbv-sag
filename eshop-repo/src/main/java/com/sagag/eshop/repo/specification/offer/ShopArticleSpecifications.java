package com.sagag.eshop.repo.specification.offer;

import com.sagag.eshop.repo.criteria.offer.ShopArticleSearchCriteria;
import com.sagag.eshop.repo.entity.offer.ShopArticle;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.enums.offer.OfferTecStateType;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Utilities specifications for Shop article.
 */
@UtilityClass
public final class ShopArticleSpecifications {

  private static final String ID = "id";

  private static final String ORG_ID = "organisationId";

  private static final String TYPE = "type";

  private static final String TECSTATE = "tecstate";

  private static final String ART_NR = "articleNumber";

  private static final String NAME = "name";

  private static final String DESC = "description";

  private static final String AMOUNT = "amount";

  private static final String PRICE = "price";

  public static Specification<ShopArticle> searchShopArticlesByCriteria(
      final ShopArticleSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {

      final List<Predicate> predicates =
          buildSearchShopArticlesPredicates(criteria, root, criteriaBuilder);

      // Order by criteria
      query.orderBy(buildSearchShopArticlesOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchShopArticlesPredicates(
      ShopArticleSearchCriteria criteria, Root<ShopArticle> root, CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();

    predicates.add(criteriaBuilder.equal(root.get(ORG_ID), criteria.getOrganisationId()));
    predicates.add(criteriaBuilder.equal(root.get(TYPE), criteria.getType()));
    predicates.add(criteriaBuilder.equal(root.get(TECSTATE), OfferTecStateType.ACTIVE.name()));

    // Search article number with like mode
    if (StringUtils.isNotBlank(criteria.getArticleNumber())) {
      predicates.add(criteriaBuilder.like(root.get(ART_NR),
          SpecificationUtils.appendLikeText(criteria.getArticleNumber())));
    }

    // Search article name with like mode
    if (StringUtils.isNotBlank(criteria.getName())) {
      predicates.add(criteriaBuilder.like(root.get(NAME),
          SpecificationUtils.appendLikeText(criteria.getName())));
    }

    // Search article description with like mode
    if (StringUtils.isNotBlank(criteria.getDescription())) {
      predicates.add(criteriaBuilder.like(root.get(DESC),
          SpecificationUtils.appendLikeText(criteria.getDescription())));
    }

    // Search article amount with greater than or equal to mode
    if (Objects.nonNull(criteria.getAmount())) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(AMOUNT), criteria.getAmount()));
    }

    // Search article price with greater than or equal to mode
    if (Objects.nonNull(criteria.getPrice())) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), criteria.getPrice()));
    }

    return predicates;
  }

  private static List<Order> buildSearchShopArticlesOrders(ShopArticleSearchCriteria criteria,
      Root<ShopArticle> root, CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();
    if (criteria.getOrderDescByNumber() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(ART_NR),
          criteria.getOrderDescByNumber()));
    }

    if (criteria.getOrderDescByName() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(NAME),
          criteria.getOrderDescByName()));
    }

    if (criteria.getOrderDescByDescription() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DESC),
          criteria.getOrderDescByDescription()));
    }

    if (criteria.getOrderDescByAmount() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(AMOUNT),
          criteria.getOrderDescByAmount()));
    }

    if (criteria.getOrderDescByPrice() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(PRICE),
          criteria.getOrderDescByPrice()));
    }

    orders.add(criteriaBuilder.desc(root.get(ID)));

    return orders;
  }

}
