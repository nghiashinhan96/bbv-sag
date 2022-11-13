package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.OrderHistorySearchCriteria;
import com.sagag.eshop.repo.entity.order.VOrderHistory;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.utils.DateUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specifications for VOrderHistory.
 */
@UtilityClass
public final class VOrderHistorySpecifications {

  private static final String ID = "id";

  private static final String CUSTOMER_NR = "customerNumber";

  private static final String ORDER_NR = "orderNumber";

  private static final String CUSTOMER_NAME = "customerName";

  private static final String CREATED_DATE = "createdDate";

  private static final String ORDER_TYPE = "type";

  private static final String TOTAL_PRICE = "totalPrice";

  private static final String AFFILIATE_ID = "affiliateId";

  private static final String SALE_ID = "saleId";

  public static Specification<VOrderHistory> of(final OrderHistorySearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = new ArrayList<>();
      if (Objects.nonNull(criteria.getAffiliateId())) {
        predicates.add(criteriaBuilder.equal(root.get(AFFILIATE_ID), criteria.getAffiliateId()));
      }
      predicates.add(criteriaBuilder.equal(root.get(SALE_ID), criteria.getSaleId()));

      if (StringUtils.isNotBlank(criteria.getCustomerNumber())) {
        predicates.add(criteriaBuilder.like(root.get(CUSTOMER_NR),
            SpecificationUtils.appendLikeText(criteria.getCustomerNumber().trim())));
      }

      if (StringUtils.isNotBlank(criteria.getCustomerName())) {
        predicates.add(criteriaBuilder.like(root.get(CUSTOMER_NAME),
            SpecificationUtils.appendLikeText(criteria.getCustomerName().trim())));
      }

      if (StringUtils.isNotBlank(criteria.getOrderNumber())) {
        predicates.add(criteriaBuilder.like(root.get(ORDER_NR),
            SpecificationUtils.appendLikeText(criteria.getOrderNumber().trim())));
      }

      if (StringUtils.isNotBlank(criteria.getOrderDate())) {
        predicates.add(buildOrderDateExp(root, criteriaBuilder, criteria.getOrderDate().trim()));
      }

      if (StringUtils.isNotBlank(criteria.getTotalPrice())) {
        final Expression<String> priceExpr = root.get(TOTAL_PRICE).as(String.class);
        predicates.add(criteriaBuilder.like(priceExpr,
            SpecificationUtils.appendLikeText(criteria.getTotalPrice().trim())));
      }

      if (StringUtils.isNotBlank(criteria.getType())) {
        predicates.add(criteriaBuilder.equal(root.get(ORDER_TYPE), criteria.getType()));
      }

      // Order by criteria
      query.orderBy(buildSortConditions(criteria, root, criteriaBuilder));

      return SpecificationUtils.andTogether(predicates, criteriaBuilder);
    };
  }

  private static List<Order> buildSortConditions(final OrderHistorySearchCriteria criteria,
      final Root<VOrderHistory> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    if (!Objects.isNull(criteria.getOrderDescByCustomerNumber())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CUSTOMER_NR),
          criteria.getOrderDescByCustomerNumber().booleanValue()));
    }

    if (!Objects.isNull(criteria.getOrderDescByCustomerName())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CUSTOMER_NAME),
          criteria.getOrderDescByCustomerName().booleanValue()));
    }

    if (!Objects.isNull(criteria.getOrderDescByOrderNumber())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(ORDER_NR),
          criteria.getOrderDescByOrderNumber().booleanValue()));
    }

    if (!Objects.isNull(criteria.getOrderDescByOrderDate())) {
      final Expression<Date> dateStringExpr = root.get(CREATED_DATE).as(Date.class);
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, dateStringExpr,
          criteria.getOrderDescByOrderDate().booleanValue()));
    }

    if (!Objects.isNull(criteria.getOrderDescByTotalPrice())) {
      final Expression<Double> priceExpr = root.get(TOTAL_PRICE).as(Double.class);
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, priceExpr,
          criteria.getOrderDescByTotalPrice().booleanValue()));
    }

    if (!Objects.isNull(criteria.getOrderDescByType())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(ORDER_TYPE),
          criteria.getOrderDescByType().booleanValue()));
    }

    orders.add(criteriaBuilder.desc(root.get(ID)));

    return orders;
  }

  private static Predicate buildOrderDateExp(final Root<VOrderHistory> root,
      final CriteriaBuilder criteriaBuilder, final String date) {

    Date beginOfDate = null;
    Date endOfDate = null;
    try {
      beginOfDate = DateUtils.parseUTCDateFromString(
          StringUtils.isBlank(date) ? StringUtils.EMPTY
              : date + DateUtils.T + DateUtils.BEGIN_OF_DAY + DateUtils.Z,
          DateUtils.UTC_DATE_PATTERN_2);
      endOfDate = DateUtils.parseUTCDateFromString(
          StringUtils.isBlank(date) ? StringUtils.EMPTY
              : date + DateUtils.T + DateUtils.END_OF_DAY + DateUtils.Z,
          DateUtils.UTC_DATE_PATTERN_2);
    } catch (ParseException e) {
      throw new IllegalArgumentException("The date format for search is not valid");
    }

    return criteriaBuilder.between(root.get(CREATED_DATE), beginOfDate, endOfDate);
  }

}
