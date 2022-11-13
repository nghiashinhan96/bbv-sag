package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.VActiveUser;
import com.sagag.services.domain.eshop.criteria.UserSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@UtilityClass
public class EshopUserSpecifications {

  private static final String PERCENT = "%";
  private static final String FULL_NAME = "fullName";
  private static final String PHONE = "phone";
  private static final String EMAIL = "email";
  private static final String USERNAME = "username";
  private static final String ORG_CODE = "orgCode";
  private static final String AFFILIATE = "affiliate";
  private static final String IS_USER_ACTIVE = "isUserActive";

  public static Specification<VActiveUser> searchActiveUserProfile(UserSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchActiveUserProfile(criteria, root, criteriaBuilder);
      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchActiveUserProfile(UserSearchCriteria criteria,
      Root<VActiveUser> root, CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(criteria.getAffiliate())) {
      predicates.add(criteriaBuilder.equal(root.get(AFFILIATE), criteria.getAffiliate()));
    }

    if (StringUtils.isNotBlank(criteria.getCustomerNumber())) {
      predicates.add(criteriaBuilder.equal(root.get(ORG_CODE), criteria.getCustomerNumber()));
    }

    if (StringUtils.isNotBlank(criteria.getUserName())) {
      predicates.add(
          criteriaBuilder.like(root.get(USERNAME), PERCENT + criteria.getUserName() + PERCENT));
    }

    if (StringUtils.isNotBlank(criteria.getEmail())) {
      predicates
          .add(criteriaBuilder.like(root.get(EMAIL), PERCENT + criteria.getEmail() + PERCENT));
    }

    if (StringUtils.isNotBlank(criteria.getTelephone())) {
      predicates
          .add(criteriaBuilder.like(root.get(PHONE), PERCENT + criteria.getTelephone() + PERCENT));
    }

    if (StringUtils.isNotBlank(criteria.getName())) {
      predicates
          .add(criteriaBuilder.like(root.get(FULL_NAME), PERCENT + criteria.getName() + PERCENT));
    }

    if (Objects.nonNull(criteria.getIsUserActive())) {
      predicates.add(BooleanUtils.isFalse(criteria.getIsUserActive())
          ? criteriaBuilder.isFalse(root.get(IS_USER_ACTIVE))
          : criteriaBuilder.isTrue(root.get(IS_USER_ACTIVE)));
    }

    return predicates;

  }
}
