package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.repo.entity.WssOpeningDaysCalendar;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.WssOpeningDaysSearchCriteria;

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
public class WssOpeningDaysSpecifications {

  private static final String BRANCHES = "branches";

  private static final String WORKING_DAY_CODE = "workingDayCode";

  private static final String EXCEPTIONS = "exceptions";

  private static final String COUNTRY = "country";

  private static final String SHORT_NAME = "shortName";

  private static final String WSS_WORKING_DAY = "wssWorkingDay";

  private static final String CODE = "code";

  private static final String DATETIME = "datetime";

  private static final String ORG_ID = "orgId";

  public static Specification<WssOpeningDaysCalendar> searchOpeningDaysByCriteria(
      final WssOpeningDaysSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildWssSearchOpeningDaysPredicates(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchWssOpeningDaysOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildWssSearchOpeningDaysPredicates(
      final WssOpeningDaysSearchCriteria criteria, final Root<WssOpeningDaysCalendar> root,
      final CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    predicates.add(criteriaBuilder.between(root.get(DATETIME),
        DateUtils.toDate(criteria.getDateFrom(), DateUtils.DEFAULT_DATE_PATTERN),
        DateUtils.toDate(criteria.getDateTo(), DateUtils.DEFAULT_DATE_PATTERN)));

    Optional.ofNullable(criteria.getCountryName()).filter(Objects::nonNull).ifPresent(value -> {
      final Join<Country, WssOpeningDaysCalendar> countryRoot = root.join(COUNTRY);
      predicates.add(criteriaBuilder.like(countryRoot.get(SHORT_NAME).as(String.class),
          SpecificationUtils.appendLikeText(value)));
    });

    Optional.ofNullable(criteria.getWorkingDayCode()).filter(Objects::nonNull).ifPresent(value -> {
      final Join<WorkingDay, WssOpeningDaysCalendar> countryRoot = root.join(WSS_WORKING_DAY);
      predicates.add(criteriaBuilder.equal(countryRoot.get(CODE).as(String.class), value));
    });

    Optional.ofNullable(criteria.getExpBranchInfo()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates.add(criteriaBuilder.like(buildStringExpr(root, EXCEPTIONS),
            SpecificationUtils.appendLikeText(
                buildLikeTextForException(BRANCHES, value, WORKING_DAY_CODE)))));

    Optional.ofNullable(criteria.getExpWorkingDayCode()).filter(StringUtils::isNotBlank)
        .ifPresent(value -> predicates
            .add(criteriaBuilder.like(buildStringExpr(root, EXCEPTIONS), SpecificationUtils
                .appendLikeText(buildEqualTextForException(WORKING_DAY_CODE, value)))));
    Optional.ofNullable(criteria.getOrgId())
    .ifPresent(value -> predicates
        .add(criteriaBuilder.equal(root.get(ORG_ID), value)));

    return predicates;
  }

  private static List<Order> buildSearchWssOpeningDaysOrders(final WssOpeningDaysSearchCriteria criteria,
      final Root<WssOpeningDaysCalendar> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByDate()).filter(Objects::nonNull)
        .ifPresent(order -> orders
            .add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DATETIME), order)));

    Optional.ofNullable(criteria.getOrderDescByCountryName()).filter(Objects::nonNull)
        .ifPresent(order -> {
          final Join<Country, WssOpeningDaysCalendar> countryRoot = root.join(COUNTRY);
          orders.add(
              SpecificationUtils.defaultOrder(criteriaBuilder, countryRoot.get(SHORT_NAME), order));
        });
    return orders;
  }

  private static String buildLikeTextForException(final String fieldName, final String value,
      final String... followFields) {
    final String followText = StringUtils.join(followFields, SagConstants.LIKE_CHAR);
    return new StringBuilder().append(SagConstants.DOUBLE_QUOTE).append(fieldName)
        .append(SagConstants.DOUBLE_QUOTE).append(SpecificationUtils.appendLikeText(value.trim()))
        .append(followText).toString();
  }

  private static String buildEqualTextForException(final String fieldName, final String value) {
    return new StringBuilder().append(SagConstants.DOUBLE_QUOTE).append(fieldName)
        .append(SagConstants.DOUBLE_QUOTE).append(SagConstants.COLON)
        .append(SagConstants.DOUBLE_QUOTE).append(value.trim()).append(SagConstants.DOUBLE_QUOTE)
        .toString();
  }

  private static Expression<String> buildStringExpr(final Root<WssOpeningDaysCalendar> root,
      final String field) {
    return root.get(field).as(String.class);
  }
}
