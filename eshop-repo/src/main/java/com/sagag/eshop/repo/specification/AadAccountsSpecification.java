package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.AadAccountsSearchCriteria;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.repo.utils.SpecificationUtils;

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


@UtilityClass
public class AadAccountsSpecification {

  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String PRIMARY_CONTACT_EMAIL = "primaryContactEmail";
  private static final String PERSONAL_NUMBER = "personalNumber";
  private static final String GENDER = "gender";
  private static final String LEGAL_ENTITY_ID = "legalEntityId";
  private static final String CREATED_DATE = "createdDate";

  public static Specification<AadAccounts> searchMessages(AadAccountsSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = buildSearchPredicates(criteria, root, criteriaBuilder);
      query.orderBy(buildOrderPredicates(criteria, root, criteriaBuilder));
      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchPredicates(AadAccountsSearchCriteria criteria,
      Root<AadAccounts> root, CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();
    String firstName = criteria.getFirstName();
    String lastName = criteria.getLastName();
    String primaryContactEmail = criteria.getPrimaryContactEmail();
    String personalNumber = criteria.getPersonalNumber();
    String legalEntityId = criteria.getLegalEntityId();
    String gender = criteria.getGender();

    if (StringUtils.isNotBlank(firstName)) {
      predicates.add(
          criteriaBuilder.like(root.get(FIRST_NAME), SpecificationUtils.appendLikeText(firstName)));
    }

    if (StringUtils.isNotBlank(lastName)) {
      predicates.add(
          criteriaBuilder.like(root.get(LAST_NAME), SpecificationUtils.appendLikeText((lastName))));
    }

    if (StringUtils.isNotBlank(primaryContactEmail)) {
      predicates.add(criteriaBuilder.like(root.get(PRIMARY_CONTACT_EMAIL),
          SpecificationUtils.appendLikeText(primaryContactEmail)));
    }

    if (StringUtils.isNotBlank(personalNumber)) {
      predicates.add(criteriaBuilder.like(root.get(PERSONAL_NUMBER),
          SpecificationUtils.appendLikeText(personalNumber)));
    }

    if (StringUtils.isNotBlank(gender)) {
      predicates.add(criteriaBuilder.equal(root.get(GENDER), gender));
    }

    if (StringUtils.isNotBlank(legalEntityId)) {
      predicates.add(criteriaBuilder.like(root.get(LEGAL_ENTITY_ID),
          SpecificationUtils.appendLikeText(legalEntityId)));
    }

    return predicates;
  }

  private static List<Order> buildOrderPredicates(AadAccountsSearchCriteria criteria,
      Root<AadAccounts> root, CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();
    Boolean orderDescByFirstName = criteria.getOrderDescByFirstName();
    Boolean orderDescByLastName = criteria.getOrderDescByLastName();
    Boolean orderDescByPrimaryContactEmail = criteria.getOrderDescByPrimaryContactEmail();
    Boolean orderDescByPersonalNumber = criteria.getOrderDescByPersonalNumber();
    Boolean orderDescByGender = criteria.getOrderDescByGender();
    Boolean orderDescBylegalEntityId = criteria.getOrderDescBylegalEntityId();

    if (!Objects.isNull(orderDescByFirstName)) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(FIRST_NAME),
          orderDescByFirstName));
    }

    if (!Objects.isNull(orderDescByLastName)) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LAST_NAME),
          orderDescByLastName));
    }

    if (!Objects.isNull(orderDescByPrimaryContactEmail)) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(PRIMARY_CONTACT_EMAIL),
          orderDescByPrimaryContactEmail));
    }

    if (!Objects.isNull(orderDescByPersonalNumber)) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(PERSONAL_NUMBER),
          orderDescByPersonalNumber));
    }

    if (!Objects.isNull(orderDescByGender)) {
      orders.add(
          SpecificationUtils.defaultOrder(criteriaBuilder, root.get(GENDER), orderDescByGender));
    }

    if (!Objects.isNull(orderDescBylegalEntityId)) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LEGAL_ENTITY_ID),
          orderDescBylegalEntityId));
    }
    orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CREATED_DATE), true));

    return orders;
  }
}
