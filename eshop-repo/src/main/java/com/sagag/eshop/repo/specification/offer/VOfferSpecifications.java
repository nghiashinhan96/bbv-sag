package com.sagag.eshop.repo.specification.offer;

import com.sagag.eshop.repo.criteria.offer.OfferSearchCriteria;
import com.sagag.eshop.repo.entity.offer.ViewOffer;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specifications for ViewOffer.
 */
@UtilityClass
public final class VOfferSpecifications {

  private static final String ID = "id";

  private static final String ORG_ID = "organisationId";

  private static final String OFFER_NR = "offerNumber";

  private static final String CUSTOMER_NAME = "customerName";

  private static final String REMARK = "remark";

  private static final String VEHICLE_DECSCRIPTIONS = "vehicleDescriptions";

  private static final String OFFER_DATE = "offerDate";

  private static final String TOTAL_GROSS_PRICE = "totalGrossPrice";

  private static final String STATUS = "status";

  private static final String OWNER_USERNAME = "ownerUsername";

  private static final String DATE_DELIMITER = "\\.";

  public static Specification<ViewOffer> of(final OfferSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      query.orderBy(buildSearchOfferOrders(criteria, root, criteriaBuilder));
      final List<Predicate> predicates = buildSearchOfferPredicates(criteria, root,
          criteriaBuilder);
      return SpecificationUtils.andTogether(predicates, criteriaBuilder);
    };
  }

  private static List<Predicate> buildSearchOfferPredicates(final OfferSearchCriteria criteria,
      final Root<ViewOffer> root, final CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();

    predicates.add(criteriaBuilder.equal(root.get(ORG_ID), criteria.getOrganisationId()));

    Optional.ofNullable(criteria.getOfferNumber()).filter(StringUtils::isNotBlank)
    .map(value -> criteriaBuilder.like(root.get(OFFER_NR),
        SpecificationUtils.appendLikeText(value)))
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getCustomerName()).filter(StringUtils::isNotBlank)
    .map(value -> criteriaBuilder.like(root.get(CUSTOMER_NAME),
        SpecificationUtils.appendLikeText(value)))
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getRemark()).filter(StringUtils::isNotBlank)
    .map(value -> criteriaBuilder.like(root.get(REMARK), SpecificationUtils.appendLikeText(value)))
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getVehicleDesc()).filter(StringUtils::isNotBlank)
    .map(value -> criteriaBuilder.like(root.get(VEHICLE_DECSCRIPTIONS),
        SpecificationUtils.appendLikeText(value)))
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getOfferDate()).filter(StringUtils::isNotBlank)
    .map(value -> {
      final Expression<String> dateStringExpr = buildDateOfferExp(root, criteriaBuilder);
      return criteriaBuilder.like(dateStringExpr,
          SpecificationUtils.appendLikeText(trimDateLeadingZeros(value)));
    })
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getPrice()).filter(StringUtils::isNotBlank)
    .map(value -> {
      final Expression<String> priceExpr = root.get(TOTAL_GROSS_PRICE).as(String.class);
      return criteriaBuilder.like(priceExpr, SpecificationUtils.appendLikeText(value));
    })
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getStatus()).filter(StringUtils::isNotBlank)
    .map(value -> criteriaBuilder.equal(root.get(STATUS), value))
    .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getUsername()).filter(StringUtils::isNotBlank)
    .map(value -> criteriaBuilder.like(root.get(OWNER_USERNAME),
        SpecificationUtils.appendLikeText(value)))
    .ifPresent(predicates::add);
    return predicates;
  }

  private static Expression<String> buildDateOfferExp(final Root<ViewOffer> root,
      final CriteriaBuilder criteriaBuilder) {
    final Expression<String> dayExp = criteriaBuilder
        .function(DateUtils.DAY, Integer.class, root.get(OFFER_DATE)).as(String.class);

    final Expression<String> monthExp = criteriaBuilder
        .function(DateUtils.MONTH, Integer.class, root.get(OFFER_DATE)).as(String.class);
    final Expression<String> yearExp = criteriaBuilder
        .function(DateUtils.YEAR, Integer.class, root.get(OFFER_DATE)).as(String.class);
    Expression<String> dateStringExpr = criteriaBuilder.concat(dayExp, SagConstants.DOT);
    dateStringExpr = criteriaBuilder.concat(dateStringExpr, monthExp);
    dateStringExpr = criteriaBuilder.concat(dateStringExpr, SagConstants.DOT);
    dateStringExpr = criteriaBuilder.concat(dateStringExpr, yearExp);
    return dateStringExpr;
  }

  private static String trimDateLeadingZeros(String dateString) {
    final StringBuilder trimedDate = new StringBuilder();
    final String[] dateParts = dateString.trim().split(DATE_DELIMITER);
    for (final String datePart : dateParts) {
      trimedDate.append(StringUtils.stripStart(datePart, "0"));
      trimedDate.append(SagConstants.DOT);
    }
    return StringUtils.stripEnd(trimedDate.toString(), SagConstants.DOT);
  }

  private static List<Order> buildSearchOfferOrders(final OfferSearchCriteria criteria,
      final Root<ViewOffer> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByOfferNumber()).filter(Objects::nonNull)
    .map(value -> SpecificationUtils.defaultOrder(criteriaBuilder, root.get(OFFER_NR),
        value.booleanValue()))
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByCustomerName()).filter(Objects::nonNull)
    .map(value -> SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CUSTOMER_NAME),
        value.booleanValue()))
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByRemark()).filter(Objects::nonNull)
    .map(value -> SpecificationUtils.defaultOrder(criteriaBuilder, root.get(REMARK),
        value.booleanValue()))
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByVehicleDesc()).filter(Objects::nonNull)
    .map(value -> SpecificationUtils.defaultOrder(criteriaBuilder, root.get(VEHICLE_DECSCRIPTIONS),
        value.booleanValue()))
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByOfferDate()).filter(Objects::nonNull)
    .map(value -> {
      final Expression<Date> dateStringExpr = root.get(OFFER_DATE).as(Date.class);
      return SpecificationUtils.defaultOrder(criteriaBuilder, dateStringExpr, value.booleanValue());
    })
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByPrice()).filter(Objects::nonNull)
    .map(value -> {
      final Expression<Double> priceExpr = root.get(TOTAL_GROSS_PRICE).as(Double.class);
      return SpecificationUtils.defaultOrder(criteriaBuilder, priceExpr, value.booleanValue());
    })
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByStatus()).filter(Objects::nonNull)
    .map(value -> SpecificationUtils.defaultOrder(criteriaBuilder, root.get(STATUS),
        value.booleanValue()))
    .ifPresent(orders::add);

    Optional.ofNullable(criteria.getOrderDescByUsername()).filter(Objects::nonNull)
    .map(value -> SpecificationUtils.defaultOrder(criteriaBuilder, root.get(OWNER_USERNAME),
        value.booleanValue()))
    .ifPresent(orders::add);

    orders.add(criteriaBuilder.desc(root.get(ID)));

    return orders;
  }
}
