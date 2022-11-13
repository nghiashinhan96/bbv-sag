package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.VUserExportSearchCriteria;
import com.sagag.eshop.repo.entity.VUserExport;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class VUserExportSpecification extends AbstractSpecification<VUserExport> {

  private static final long serialVersionUID = 3610675732459299945L;

  private VUserExportSearchCriteria criteria;

  @AllArgsConstructor
  enum VUserExportField {
    AFFILIATE("orgParentShortName"),
    CUSTOMER_NUMBER("orgCode"),
    USERNAME("userName"),
    EMAIL("userEmail"),
    IS_USER_ACTIVE("isUserActive");

    @Getter
    private String field;
  }

  private VUserExportSpecification(VUserExportSearchCriteria criteria) {
    this.criteria = criteria;
  }

  public static VUserExportSpecification of(VUserExportSearchCriteria criteria) {
    return new VUserExportSpecification(criteria);
  }

  @Override
  public Predicate toPredicate(Root<VUserExport> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    List<Predicate> predicates = new ArrayList<>();

    final String orgParentShortName = criteria.getAffiliate();
    if (StringUtils.isNotBlank(orgParentShortName)) {
      predicates.add(criteriaBuilder.equal(root.get(VUserExportField.AFFILIATE.getField()),
          orgParentShortName));
    }

    final String orgCode = criteria.getCustomerNumber();
    if (StringUtils.isNotBlank(orgCode)) {
      predicates.add(
          criteriaBuilder.equal(root.get(VUserExportField.CUSTOMER_NUMBER.getField()), orgCode));
    }

    final String userName = criteria.getUserName();
    if (StringUtils.isNotBlank(userName)) {
      predicates.add(criteriaBuilder.like(root.get(VUserExportField.USERNAME.getField()),
          likeBetween(userName)));
    }

    final String userEmail = criteria.getEmail();
    if (StringUtils.isNotBlank(userEmail)) {
      predicates.add(criteriaBuilder.like(root.get(VUserExportField.EMAIL.getField()),
          likeBetween(userEmail)));
    }

    final Boolean isUserActive = criteria.getIsUserActive();
    if (Objects.nonNull(isUserActive)) {
      predicates.add(criteriaBuilder.equal(root.get(VUserExportField.IS_USER_ACTIVE.getField()),
          isUserActive));
    }

    return andTogether(predicates, criteriaBuilder);
  }

}
