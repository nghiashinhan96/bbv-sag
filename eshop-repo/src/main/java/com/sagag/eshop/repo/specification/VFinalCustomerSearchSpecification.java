package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchCriteria;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchSortCriteria;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchTermCriteria;
import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.enums.FinalCustomerStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class VFinalCustomerSearchSpecification extends AbstractSpecification<VFinalCustomer> {

  private static final long serialVersionUID = 6295663162123527938L;

  private final int customerOrgId;

  private final FinalCustomerSearchCriteria criteria;

  public static VFinalCustomerSearchSpecification getInstance(final int customerOrgId,
      final FinalCustomerSearchCriteria criteria) {
    return new VFinalCustomerSearchSpecification(customerOrgId, criteria);
  }

  @Override
  public Predicate toPredicate(Root<VFinalCustomer> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = buildPredicates(root, criteriaBuilder, criteria.getTerm());

    predicates.add(criteriaBuilder.equal(
        root.get(VFinalCustomerField.PARENT_ORG_ID.getField()), customerOrgId));

    query.orderBy(buildOrders(root, criteriaBuilder, criteria.getSort()));
    return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
  }

  private static List<Predicate> buildPredicates(Root<VFinalCustomer> root,
      CriteriaBuilder criteriaBuilder, FinalCustomerSearchTermCriteria criteria) {
    final List<Predicate> predicates = new ArrayList<>();
    Optional.ofNullable(criteria.getName()).filter(StringUtils::isNotBlank)
        .map(value -> buildLikePredicatesByFields(root, criteriaBuilder, value,
            VFinalCustomerField.NAME))
        .ifPresent(predicates::addAll);

    Optional.ofNullable(criteria.getFinalCustomerType())
        .map(value -> criteriaBuilder
            .equal(root.get(VFinalCustomerField.FINAL_CUSTOMER_TYPE.getField()), value.name()))
        .ifPresent(predicates::add);

    Optional.ofNullable(criteria.getAddress()).filter(StringUtils::isNotBlank)
        .map(value -> buildLikePredicatesByFields(root, criteriaBuilder, value,
            VFinalCustomerField.ADDRESS_INFO))
        .ifPresent(predicates::addAll);

    Optional.ofNullable(criteria.getContactInfo()).filter(StringUtils::isNotBlank)
        .map(value -> buildLikePredicatesByFields(root, criteriaBuilder, value,
            VFinalCustomerField.CONTACT_INFO))
        .ifPresent(predicates::addAll);

    Predicate statusPredicate = Optional.ofNullable(criteria.getStatus())
    .map(value -> criteriaBuilder.equal(root.get(VFinalCustomerField.STATUS.getField()),
        value.name()))
    .orElseGet(defaultStatusPredicate(root, criteriaBuilder));
    predicates.add(statusPredicate);

    return predicates;
  }

  private static Supplier<Predicate> defaultStatusPredicate(Root<VFinalCustomer> root,
      CriteriaBuilder criteriaBuilder) {
    return () -> criteriaBuilder.notEqual(root.get(VFinalCustomerField.STATUS.getField()),
        FinalCustomerStatus.DELETED.name());
  }

  private static List<Predicate> buildLikePredicatesByFields(Root<VFinalCustomer> root,
      CriteriaBuilder criteriaBuilder, String value, VFinalCustomerField... fields) {
    return Stream.of(fields).map(field -> criteriaBuilder.like(root.get(field.getField()),
        SpecificationUtils.appendLikeText(value))).collect(Collectors.toList());
  }

  private static List<Order> buildOrders(Root<VFinalCustomer> root, CriteriaBuilder criteriaBuilder,
      FinalCustomerSearchSortCriteria criteria) {
    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByName()).filter(Objects::nonNull)
        .map(value -> defaultOrder(criteriaBuilder, root.get(VFinalCustomerField.NAME.getField()),
            value.booleanValue()))
        .ifPresent(orders::add);

    return orders;
  }

  @AllArgsConstructor
  enum VFinalCustomerField {
    ORG_ID("orgId"),
    NAME("name"),
    DESCRIPTION("description"),
    PARENT_ORG_ID("parentOrgId"),
    FINAL_CUSTOMER_TYPE("finalCustomerType"),
    ADDRESS_INFO("addressInfo"),
    CONTACT_INFO("contactInfo"),
    STATUS("status");

    @Getter
    private String field;
  }

}
