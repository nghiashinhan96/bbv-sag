package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.criteria.BasketHistoryCriteria;
import com.sagag.eshop.repo.entity.VBasketHistory;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specification class for VBasketHistory.
 * */
public class BasketHistorySpecification extends AbstractSpecification<VBasketHistory> {

  private static final long serialVersionUID = 7652031880851437855L;

  private static final String BASKETNAME_FIELD = "basketName";

  private static final String CREATED_USER_ID_FIELD = "createdUserId";

  private static final String CREATED_FULL_NAME_FIELD = "createdFullName";

  private static final String SALES_USER_ID_FIELD = "salesUserId";

  private static final String SALES_FULL_NAME_FIELD = "salesFullName";

  private static final String ORG_CODE_FIELD = "orgCode";

  private static final String CUSTOMER_NAME_FIELD = "customerName";

  private static final String UPDATED_DATE_FIELD = "updatedDate";

  private static final String GRAND_TOTAL_EXCLUDE_VAT_FIELD = "grandTotalExcludeVat";

  private static final String CUSTOMER_REF_TEXT = "customerRefText";

  private final BasketHistoryCriteria criteria;

  private final boolean salesMode;

  private BasketHistorySpecification(BasketHistoryCriteria criteria, boolean salesMode) {
    this.criteria = criteria;
    this.salesMode = salesMode;
  }

  public static BasketHistorySpecification of(BasketHistoryCriteria criteria, boolean salesMode) {
    return new BasketHistorySpecification(criteria, salesMode);
  }

  @Override
  public Predicate toPredicate(Root<VBasketHistory> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {

    List<Predicate> predicates = new ArrayList<>();

    final String basketName = criteria.getBasketName();
    if (StringUtils.isNotBlank(basketName)) {
      predicates.add(criteriaBuilder.like(root.get(BASKETNAME_FIELD), likeBetween(basketName)));
    }

    final Long createdUserId = criteria.getCreatedUserId();
    if (Objects.nonNull(createdUserId)) {
      predicates.add(criteriaBuilder.equal(root.get(CREATED_USER_ID_FIELD), createdUserId));
    }

    final Long salesUserId = criteria.getSalesUserId();
    if (Objects.nonNull(salesUserId)) {
      predicates.add(criteriaBuilder.equal(root.get(SALES_USER_ID_FIELD), salesUserId));
    }

    final String custNr = criteria.getCustomerNumber();
    if (StringUtils.isNotBlank(custNr)) {
      predicates.add(criteriaBuilder.equal(root.get(ORG_CODE_FIELD), custNr));
    }

    final String custName = criteria.getCustomerName();
    if (StringUtils.isNotBlank(custName)) {
      predicates.add(criteriaBuilder.like(root.get(CUSTOMER_NAME_FIELD), likeBetween(custName)));
    }

    String createdLastName = criteria.getCreatedLastName();
    if (StringUtils.isNotBlank(createdLastName)) {
      predicates.add(criteriaBuilder.like(root.get(CREATED_FULL_NAME_FIELD),
          likeBetween(StringUtils.trim(createdLastName))));
    }

    String salesLastName = criteria.getSalesLastName();
    if (StringUtils.isNotBlank(salesLastName)) {
      predicates.add(criteriaBuilder.like(root.get(SALES_FULL_NAME_FIELD),
          likeBetween(StringUtils.trim(salesLastName))));
    }

    final Date updatedDate = criteria.getUpdatedDate();
    if (Objects.nonNull(updatedDate)) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(UPDATED_DATE_FIELD),
          updatedDate));
    }

    final String customerRefText = criteria.getCustomerRefText();
    if (StringUtils.isNotBlank(customerRefText)) {
      predicates.add(criteriaBuilder.like(root.get(CUSTOMER_REF_TEXT), likeBetween(customerRefText)));
    }

    query.orderBy(getOrdersByCriteria(criteria, salesMode, root, criteriaBuilder));

    return andTogether(predicates, criteriaBuilder);
  }

  private static List<Order> getOrdersByCriteria(BasketHistoryCriteria criteria, boolean salesMode,
      Root<VBasketHistory> root, CriteriaBuilder criteriaBuilder) {

    List<Order> orders = new ArrayList<>();
    if (Objects.nonNull(criteria.getOrderByDescCustomerNumber())) {
      orders.add(defaultOrder(criteriaBuilder, root.get(ORG_CODE_FIELD),
          criteria.getOrderByDescCustomerNumber().booleanValue()));
    }

    if (Objects.nonNull(criteria.getOrderByDescCustomerName())) {
      orders.add(defaultOrder(criteriaBuilder, root.get(CUSTOMER_NAME_FIELD),
          criteria.getOrderByDescCustomerName().booleanValue()));
    }

    if (Objects.nonNull(criteria.getOrderByDescBasketName())) {
      orders.add(defaultOrder(criteriaBuilder, root.get(BASKETNAME_FIELD),
          criteria.getOrderByDescBasketName().booleanValue()));
    }

    if (Objects.nonNull(criteria.getOrderByDescGrandTotalExcludeVat())) {
      orders.add(defaultOrder(criteriaBuilder, root.get(GRAND_TOTAL_EXCLUDE_VAT_FIELD),
          criteria.getOrderByDescGrandTotalExcludeVat().booleanValue()));
    }

    if (Objects.nonNull(criteria.getOrderByDescUpdatedDate())) {
      orders.add(defaultOrder(criteriaBuilder, root.get(UPDATED_DATE_FIELD),
          criteria.getOrderByDescUpdatedDate().booleanValue()));
    }

    if (Objects.nonNull(criteria.getOrderByDescLastName())) {
      final Expression<String> orderByFullName =
          createOrderByFullName(salesMode, root);
      orders.add(defaultOrder(criteriaBuilder, orderByFullName,
          criteria.getOrderByDescLastName().booleanValue()));
    }

    if (Objects.nonNull(criteria.getOrderByDescCustomerRefText())) {
      orders.add(defaultOrder(criteriaBuilder, root.get(CUSTOMER_REF_TEXT),
          criteria.getOrderByDescCustomerRefText().booleanValue()));
    }

    return orders;
  }

  private static Expression<String> createOrderByFullName(boolean salesMode,
      Root<VBasketHistory> root) {
    if (salesMode) {
      return root.get(SALES_FULL_NAME_FIELD);
    }
    return root.get(CREATED_FULL_NAME_FIELD);
  }
}
