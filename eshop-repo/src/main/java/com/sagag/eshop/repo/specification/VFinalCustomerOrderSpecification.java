package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerOrderCriteria;
import com.sagag.eshop.repo.entity.order.VFinalCustomerOrder;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specification class for VFinalCustomerOrder.
 */
public class VFinalCustomerOrderSpecification extends AbstractSpecification<VFinalCustomerOrder> {

  private static final long serialVersionUID = 7652031880851437855L;

  @AllArgsConstructor
  enum OrderField {
    STATUS("status"),
    DATE_FIELD("date"),
    ID("id"),
    COMPANY_NAME("companyName"),
    ADDRESS("address"),
    POSTCODE("postcode"),
    CUSTOMER_NUMBER("customerNumber"),
    ORG_ID("orgId"),
    USERNAME("username"),
    VEHICLE_DESCS("vehicleDescs");

    @Getter
    private String field;
  }

  private final FinalCustomerOrderCriteria criteria;

  private VFinalCustomerOrderSpecification(FinalCustomerOrderCriteria criteria) {
    this.criteria = criteria;
  }

  public static VFinalCustomerOrderSpecification of(FinalCustomerOrderCriteria criteria) {
    return new VFinalCustomerOrderSpecification(criteria);
  }

  @Override
  public Predicate toPredicate(Root<VFinalCustomerOrder> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {

    List<Predicate> predicates = new ArrayList<>();

    predicates.add(root.get(OrderField.ORG_ID.getField()).in(criteria.getFinalOrgIds()));

    final List<String> statuses = criteria.getStatuses();
    if (!CollectionUtils.isEmpty(statuses)) {
      final In<String> inClause = criteriaBuilder.in(root.get(OrderField.STATUS.getField()));
      statuses.forEach(inClause::value);
      predicates.add(inClause);
    }

    final String dateFrom = criteria.getDateFrom();
    if (StringUtils.isNotBlank(dateFrom)) {
      predicates.add(buildDateFromExp(root, criteriaBuilder, dateFrom.trim()));
    }

    final String dateTo = criteria.getDateTo();
    if (StringUtils.isNotBlank(dateTo)) {
      predicates.add(buildDateToExp(root, criteriaBuilder, dateTo.trim()));
    }

    final Long finalCustomerOrderId = criteria.getId();
    if (Objects.nonNull(finalCustomerOrderId)) {
      predicates
          .add(criteriaBuilder.equal(root.get(OrderField.ID.getField()), finalCustomerOrderId));
    }

    final String vehicleDescs = criteria.getVehicleDescs();
    if (StringUtils.isNotBlank(vehicleDescs)) {
      predicates.add(
          criteriaBuilder.like(root.get(OrderField.VEHICLE_DESCS.getField()), likeBetween(vehicleDescs)));
    }

    final String username = criteria.getUsername();
    if (StringUtils.isNotBlank(username)) {
      predicates.add(
          criteriaBuilder.like(root.get(OrderField.USERNAME.getField()), likeBetween(username)));
    }

    final String customerInfo = criteria.getCustomerInfo();
    if (StringUtils.isNotBlank(customerInfo)) {
      Predicate companyNamePredicate = criteriaBuilder
          .like(root.get(OrderField.COMPANY_NAME.getField()), likeBetween(customerInfo));
      Predicate addressPredicate =
          criteriaBuilder.like(root.get(OrderField.ADDRESS.getField()), likeBetween(customerInfo));
      Predicate postCodePredicate =
          criteriaBuilder.like(root.get(OrderField.POSTCODE.getField()), likeBetween(customerInfo));
      Predicate companyNrPredicate = criteriaBuilder
          .like(root.get(OrderField.CUSTOMER_NUMBER.getField()), likeBetween(customerInfo));
      predicates.add(criteriaBuilder.or(companyNamePredicate, addressPredicate, postCodePredicate,
          companyNrPredicate));
    }

    query.orderBy(getOrdersByCriteria(criteria, root, criteriaBuilder));

    return andTogether(predicates, criteriaBuilder);
  }

  private static List<Order> getOrdersByCriteria(FinalCustomerOrderCriteria criteria,
      Root<VFinalCustomerOrder> root, CriteriaBuilder criteriaBuilder) {

    List<Order> orders = new ArrayList<>();
    final Boolean orderDescByOrderDate = criteria.getOrderDescByOrderDate();
    if (Objects.nonNull(orderDescByOrderDate)) {
      final Expression<Date> dateStringExpr =
          root.get(OrderField.DATE_FIELD.getField()).as(Date.class);
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, dateStringExpr,
          orderDescByOrderDate.booleanValue()));
    }

    final Boolean orderDescById = criteria.getOrderDescById();
    if (Objects.nonNull(orderDescById)) {
      orders.add(defaultOrder(criteriaBuilder, root.get(OrderField.ID.getField()),
          orderDescById.booleanValue()));
    }

    final Boolean orderDescCustomerInfo = criteria.getOrderDescCustomerInfo();
    if (Objects.nonNull(orderDescCustomerInfo)) {
      orders.add(defaultOrder(criteriaBuilder, root.get(OrderField.COMPANY_NAME.getField()),
          orderDescCustomerInfo.booleanValue()));
      orders.add(defaultOrder(criteriaBuilder, root.get(OrderField.ADDRESS.getField()),
          orderDescCustomerInfo.booleanValue()));
      orders.add(defaultOrder(criteriaBuilder, root.get(OrderField.POSTCODE.getField()),
          orderDescCustomerInfo.booleanValue()));
      orders.add(defaultOrder(criteriaBuilder, root.get(OrderField.CUSTOMER_NUMBER.getField()),
          orderDescCustomerInfo.booleanValue()));
    }

    return orders;
  }

  private static Predicate buildDateFromExp(final Root<VFinalCustomerOrder> root,
      final CriteriaBuilder criteriaBuilder, final String dateFrom) {

    Date beginOfDate = null;
    try {
      beginOfDate = DateUtils.parseUTCDateFromString(
          StringUtils.isBlank(dateFrom) ? StringUtils.EMPTY
              : dateFrom + DateUtils.T + DateUtils.BEGIN_OF_DAY + DateUtils.Z,
          DateUtils.UTC_DATE_PATTERN_2);
    } catch (ParseException e) {
      throw new IllegalArgumentException("The date format for search is not valid");
    }

    return criteriaBuilder.greaterThanOrEqualTo(root.get(OrderField.DATE_FIELD.getField()),
        beginOfDate);
  }

  private static Predicate buildDateToExp(final Root<VFinalCustomerOrder> root,
      final CriteriaBuilder criteriaBuilder, final String dateTo) {

    Date endOfDate = null;
    try {
      endOfDate = DateUtils.parseUTCDateFromString(
          StringUtils.isBlank(dateTo) ? StringUtils.EMPTY
              : dateTo + DateUtils.T + DateUtils.END_OF_DAY + DateUtils.Z,
          DateUtils.UTC_DATE_PATTERN_2);
    } catch (ParseException e) {
      throw new IllegalArgumentException("The date format for search is not valid");
    }

    return criteriaBuilder.lessThanOrEqualTo(root.get(OrderField.DATE_FIELD.getField()), endOfDate);
  }

}
