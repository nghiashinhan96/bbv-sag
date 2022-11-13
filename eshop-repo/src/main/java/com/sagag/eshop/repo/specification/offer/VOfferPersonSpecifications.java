package com.sagag.eshop.repo.specification.offer;

import com.sagag.eshop.repo.criteria.offer.OfferPersonSearchCriteria;
import com.sagag.eshop.repo.entity.offer.ViewOfferPerson;
import com.sagag.eshop.repo.utils.SpecificationUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specifications for offer person.
 */
@UtilityClass
public final class VOfferPersonSpecifications {

  private static final String ID = "id";

  private static final String ORG_ID = "organisationId";

  private static final String DISPLAY_NAME = "displayName";

  private static final String PHONE = "phone";

  private static final String FAX = "fax";

  private static final String EMAIL = "email";

  private static final String LINE1 = "line1";

  private static final String ZIP_CODE = "zipcode";

  private static final String CITY = "city";

  public static Specification<ViewOfferPerson> of(final OfferPersonSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = new ArrayList<>();

      predicates.add(criteriaBuilder.equal(root.get(ORG_ID), criteria.getOrganisationId()));

      // Search offer person name with like mode
      if (StringUtils.isNotBlank(criteria.getName())) {
        predicates.add(criteriaBuilder.like(root.get(DISPLAY_NAME),
            SpecificationUtils.appendLikeText(criteria.getName())));
      }

      // Search offer address with like mode
      if (StringUtils.isNotBlank(criteria.getAddress())) {
        final Expression<String> addressExpression =
            createFullAddressExpression(root, criteriaBuilder);
        predicates.add(criteriaBuilder.like(addressExpression,
            SpecificationUtils.appendLikeText(criteria.getAddress())));
      }

      // Search offer person property with like mode
      if (StringUtils.isNotBlank(criteria.getContactInfo())) {
        final Expression<String> contactInfoExpression =
            createFullContactInfoExpression(root, criteriaBuilder);
        predicates.add(criteriaBuilder.like(contactInfoExpression,
            SpecificationUtils.appendLikeText(criteria.getContactInfo())));
      }

      // Order by criteria
      query.orderBy(buildSearchOfferPersonOrders(criteria, root, criteriaBuilder));

      return SpecificationUtils.andTogether(predicates, criteriaBuilder);
    };
  }

  private static List<Order> buildSearchOfferPersonOrders(final OfferPersonSearchCriteria criteria,
      final Root<ViewOfferPerson> root, final CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    if (criteria.getOrderDescByName() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DISPLAY_NAME), criteria
          .getOrderDescByName().booleanValue()));
    }

    if (criteria.getOrderDescByAddress() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LINE1), criteria
          .getOrderDescByAddress().booleanValue()));
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(ZIP_CODE), criteria
          .getOrderDescByAddress().booleanValue()));
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CITY), criteria
          .getOrderDescByAddress().booleanValue()));
    }

    if (criteria.getOrderDescByContactInfo() != null) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(PHONE), criteria
          .getOrderDescByContactInfo().booleanValue()));
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(FAX), criteria
          .getOrderDescByContactInfo().booleanValue()));
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(EMAIL), criteria
          .getOrderDescByContactInfo().booleanValue()));
    }

    orders.add(criteriaBuilder.desc(root.get(ID)));

    return orders;
  }

  private static Expression<String> createFullAddressExpression(final Root<ViewOfferPerson> root,
      final CriteriaBuilder criteriaBuilder) {
    Expression<String> exp = criteriaBuilder.concat(root.get(LINE1), StringUtils.SPACE);
    exp = criteriaBuilder.concat(exp, root.get(ZIP_CODE));
    exp = criteriaBuilder.concat(exp, StringUtils.SPACE);
    return criteriaBuilder.concat(exp, root.get(CITY));
  }

  private static Expression<String> createFullContactInfoExpression(
      final Root<ViewOfferPerson> root, final CriteriaBuilder criteriaBuilder) {
    Expression<String> exp = criteriaBuilder.concat(root.get(PHONE), StringUtils.SPACE);
    exp = criteriaBuilder.concat(exp, root.get(FAX));
    exp = criteriaBuilder.concat(exp, StringUtils.SPACE);
    return criteriaBuilder.concat(exp, root.get(EMAIL));
  }

}
