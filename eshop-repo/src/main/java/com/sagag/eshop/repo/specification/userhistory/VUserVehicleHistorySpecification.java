package com.sagag.eshop.repo.specification.userhistory;

import com.sagag.eshop.repo.criteria.user_history.UserVehicleHistorySearchCriteria;
import com.sagag.eshop.repo.entity.user_history.VUserVehicleHistory;
import com.sagag.eshop.repo.specification.AbstractSpecification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class VUserVehicleHistorySpecification extends AbstractSpecification<VUserVehicleHistory> {

  private static final long serialVersionUID = 3122392704648546023L;

  private static final String VEH_NAME_FIELD = "vehicleName";

  private static final String SEARCH_TERM_FIELD = "searchTerm";

  private static final String VEH_CLASS_FIELD = "vehicleClass";

  @NonNull
  private UserVehicleHistorySearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<VUserVehicleHistory> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getFilterMode())
    .ifPresent(val -> UserHistorySpecificationHelper
        .addFilterMode(predicates, val, criteria.getFullName(), criteria.getUserId(),
            root, criteriaBuilder, AbstractSpecification::likeBetween));

    Optional.ofNullable(criteria.getOrgId()).filter(Objects::nonNull)
    .ifPresent(val -> predicates.add(criteriaBuilder.equal(root.get("orgId"), val)));

    Optional.ofNullable(criteria.getVehicleName()).filter(StringUtils::isNotBlank)
    .ifPresent(val -> predicates.add(criteriaBuilder.like(root.get(VEH_NAME_FIELD),
        likeBetween(StringUtils.trim(val)))));

    Optional.ofNullable(criteria.getVehicleClass()).filter(StringUtils::isNotBlank)
    .ifPresent(val -> predicates.add(criteriaBuilder.like(root.get(VEH_CLASS_FIELD),
        likeBetween(StringUtils.trim(val)))));

    Optional.ofNullable(criteria.getSearchTerm()).filter(StringUtils::isNotBlank)
    .ifPresent(val -> predicates.add(criteriaBuilder.like(root.get(SEARCH_TERM_FIELD),
        likeBetween(StringUtils.trim(val)))));

    Optional.ofNullable(criteria.getFromDate()).filter(Objects::nonNull)
    .ifPresent(val -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(
        root.get(UserHistorySpecificationHelper.SELECT_DATE_FIELD), val)));


    Optional.ofNullable(criteria.getToDate()).filter(Objects::nonNull)
    .ifPresent(val -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(
        UserHistorySpecificationHelper.SELECT_DATE_FIELD),
        val)));

    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescByVehicleName()).filter(Objects::nonNull)
    .ifPresent(val -> orders.add(defaultOrder(criteriaBuilder, root.get(VEH_NAME_FIELD),
        val.booleanValue())));

    Optional.ofNullable(criteria.getOrderDescBySearchTerm()).filter(Objects::nonNull)
    .ifPresent(val -> orders.add(defaultOrder(criteriaBuilder, root.get(SEARCH_TERM_FIELD),
        val.booleanValue())));

    Optional.ofNullable(criteria.getOrderDescByFullName()).filter(Objects::nonNull)
    .ifPresent(val -> orders.add(defaultOrder(criteriaBuilder, root.get(
        UserHistorySpecificationHelper.FULL_NAME_FIELD),
        val.booleanValue())));

    Optional.ofNullable(criteria.getOrderDescBySelectDate()).filter(Objects::nonNull)
    .ifPresent(val -> orders.add(defaultOrder(criteriaBuilder, root.get(
        UserHistorySpecificationHelper.SELECT_DATE_FIELD),
        val.booleanValue())));

    if (!CollectionUtils.isEmpty(orders)) {
      query.orderBy(orders);
    }

    return andTogether(predicates, criteriaBuilder);
  }

}
