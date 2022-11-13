package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.OpeningDaysSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Utilities specifications for Opening Days Calendar
 */

@UtilityClass
public class OpeningDaysSpecifications {

  private static final String AFFILIATE = "affiliate";

  private static final String BRANCHES = "branches";

  private static final String WORKING_DAY_CODE = "workingDayCode";

  private static final String DELIVERY_ADDRESS_IDS = "deliveryAdrressIDs";

  private static final String EXCEPTIONS = "exceptions";

  private static final String COUNTRY = "country";

  private static final String SHORT_NAME = "shortName";

  private static final String WORKING_DAY = "workingDay";

  private static final String CODE = "code";

  private static final String DATETIME = "datetime";

  public static Specification<OpeningDaysCalendar> searchOpeningDaysByCriteria(
      final OpeningDaysSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchOpeningDaysPredicates(criteria, root, criteriaBuilder);

      // Order by criteria
      query.orderBy(buildSearchOpeningDaysOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchOpeningDaysPredicates(
      final OpeningDaysSearchCriteria criteria, final Root<OpeningDaysCalendar> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    // Search date
    predicates.add(criteriaBuilder.between(root.get(DATETIME),
        DateUtils.toDate(criteria.getDateFrom(), DateUtils.DEFAULT_DATE_PATTERN),
        DateUtils.toDate(criteria.getDateTo(), DateUtils.DEFAULT_DATE_PATTERN)));

    // Search country
    Optional.ofNullable(criteria.getCountryName()).filter(Objects::nonNull).ifPresent(value -> {
      final Join<Country, OpeningDaysCalendar> countryRoot = root.join(COUNTRY);
      predicates.add(criteriaBuilder.like(countryRoot.get(SHORT_NAME).as(String.class),
          SpecificationUtils.appendLikeText(value)));
    });

    // Search working day code
    Optional.ofNullable(criteria.getWorkingDayCode()).filter(Objects::nonNull).ifPresent(value -> {
      final Join<WorkingDay, OpeningDaysCalendar> countryRoot = root.join(WORKING_DAY);
      predicates.add(criteriaBuilder.equal(countryRoot.get(CODE).as(String.class), value));
    });

    // Search affiliate
    Optional.ofNullable(criteria.getExpAffiliate()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, EXCEPTIONS),
            SpecificationUtils.appendLikeText(buildLikeTextForException(AFFILIATE, value, BRANCHES,
                WORKING_DAY_CODE, DELIVERY_ADDRESS_IDS)))));

    // Search branch
    Optional.ofNullable(criteria.getExpBranchInfo()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, EXCEPTIONS),
            SpecificationUtils.appendLikeText(
                buildLikeTextForException(BRANCHES, value, WORKING_DAY_CODE, DELIVERY_ADDRESS_IDS)))));

    // Search working day code
    Optional.ofNullable(criteria.getExpWorkingDayCode()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.like(buildStringExpr(root, EXCEPTIONS), SpecificationUtils
                .appendLikeText(buildEqualTextForException(WORKING_DAY_CODE, value)))));

    // Search delivery address id
    Optional.ofNullable(criteria.getExpDeliveryAddressId()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.like(buildStringExpr(root, EXCEPTIONS), SpecificationUtils
                .appendLikeText(buildLikeTextForException(DELIVERY_ADDRESS_IDS, value)))));

    return predicates;
  }

  private static List<Order> buildSearchOpeningDaysOrders(final OpeningDaysSearchCriteria criteria,
      final Root<OpeningDaysCalendar> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    // Order by date
    Optional.ofNullable(criteria.getOrderDescByDate()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DATETIME), order)));

    // Order by country name
    Optional.ofNullable(criteria.getOrderDescByCountryName()).filter(Objects::nonNull)
        .ifPresent(order -> {
          final Join<Country, OpeningDaysCalendar> countryRoot = root.join(COUNTRY);
          orders.add(
              SpecificationUtils.defaultOrder(criteriaBuilder, countryRoot.get(SHORT_NAME), order));
        });
    return orders;
  }

  // Build like text exception with format "fieldName":"%value%"%followFieldsName
  // followFields prevent case search value for field A but exist in another field
  private static String buildLikeTextForException(final String fieldName, final String value,
      final String... followFields) {
    final String followText = StringUtils.join(followFields, SagConstants.LIKE_CHAR);
    return new StringBuilder().append(SagConstants.DOUBLE_QUOTE).append(fieldName)
        .append(SagConstants.DOUBLE_QUOTE).append(SpecificationUtils.appendLikeText(value.trim()))
        .append(followText).toString();
  }

  // Build text exception with format "fieldName":"value"
  private static String buildEqualTextForException(final String fieldName, final String value) {
    return new StringBuilder().append(SagConstants.DOUBLE_QUOTE).append(fieldName)
        .append(SagConstants.DOUBLE_QUOTE).append(SagConstants.COLON)
        .append(SagConstants.DOUBLE_QUOTE).append(value.trim()).append(SagConstants.DOUBLE_QUOTE)
        .toString();
  }

  private static Expression<String> buildStringExpr(final Root<OpeningDaysCalendar> root,
      final String field) {
    return root.get(field).as(String.class);
  }
}
