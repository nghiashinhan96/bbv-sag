package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.finaluser.FinalUserSearchCriteria;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@UtilityClass
public class FinalUserSearchSpecifications {

  private static final String ID = "orgId";
  private static final String USER_NAME = "userName";
  private static final String EMAIL = "userEmail";
  private static final String ROLE_ID = "roleId";
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String ORG_CODE = "orgCode";
  private static final String SALUTATION_ID = "salutId";

  public static Specification<VUserDetail> searchUserListByFinalCustomer(
      FinalUserSearchCriteria criteria, int finalCustomerId) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchUserConditions(criteria, root, criteriaBuilder, finalCustomerId);

      query.orderBy(buildSearchCollectionOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchUserConditions(FinalUserSearchCriteria criteria,
      Root<VUserDetail> root, CriteriaBuilder criteriaBuilder, int finalCustomerId) {
    final List<Predicate> predicates = new ArrayList<>();

    predicates.add(criteriaBuilder.equal(root.get(ID), finalCustomerId));
    predicates.add(criteriaBuilder.isNull(root.get(ORG_CODE)));
    if (StringUtils.isNotBlank(criteria.getUserName())) {
      predicates.add(criteriaBuilder.like(root.get(USER_NAME),
          SpecificationUtils.appendLikeText(criteria.getUserName())));
    }
    if (StringUtils.isNotBlank(criteria.getFullName())) {
      Expression<String> exp = criteriaBuilder.concat(
          criteriaBuilder.concat(root.get(FIRST_NAME), SagConstants.SPACE), root.get(LAST_NAME));
      predicates.add(
          criteriaBuilder.like(exp, SpecificationUtils.appendLikeText(criteria.getFullName())));
    }
    if (StringUtils.isNotBlank(criteria.getUserEmail())) {
      predicates.add(criteriaBuilder.like(root.get(EMAIL),
          SpecificationUtils.appendLikeText(criteria.getUserEmail())));
    }
    if (!Objects.isNull(criteria.getType())) {
      predicates.add(criteriaBuilder.equal(root.get(ROLE_ID), criteria.getType()));
    }

    if (StringUtils.isNotBlank(criteria.getFirstName())) {
      predicates.add(criteriaBuilder.like(root.get(FIRST_NAME),
          SpecificationUtils.appendLikeText(criteria.getFirstName())));
    }

    if (StringUtils.isNotBlank(criteria.getLastName())) {
      predicates.add(criteriaBuilder.like(root.get(LAST_NAME),
          SpecificationUtils.appendLikeText(criteria.getLastName())));
    }

    if (!Objects.isNull(criteria.getSalutation())) {
      predicates.add(criteriaBuilder.equal(root.get(SALUTATION_ID), criteria.getSalutation()));
    }
    return predicates;
  }

  private static List<Order> buildSearchCollectionOrders(FinalUserSearchCriteria criteria,
      Root<VUserDetail> root, CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();
    if (Objects.isNull(criteria.getSort())) {
      return Collections.emptyList();
    }
    if (!Objects.isNull(criteria.getSort().getOrderByUserNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(USER_NAME),
          criteria.getSort().getOrderByUserNameDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByEmailDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(EMAIL),
          criteria.getSort().getOrderByEmailDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByFullNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(FIRST_NAME),
          criteria.getSort().getOrderByFullNameDesc().booleanValue()));
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LAST_NAME),
          criteria.getSort().getOrderByFullNameDesc().booleanValue()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByFirstNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(FIRST_NAME),
          criteria.getSort().getOrderByFirstNameDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByLastNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LAST_NAME),
          criteria.getSort().getOrderByLastNameDesc()));
    }

    return orders;
  }
}
