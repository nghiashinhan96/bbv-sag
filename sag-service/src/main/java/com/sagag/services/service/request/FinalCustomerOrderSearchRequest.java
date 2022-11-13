package com.sagag.services.service.request;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerOrderCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.List;


/**
 * Request body class for viewing final customer orders.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalCustomerOrderSearchRequest implements Serializable {

  private static final long serialVersionUID = -2020684354303934474L;

  private List<String> statuses;

  private String customerInfo;

  private String id;

  private String dateFrom;

  private String dateTo;

  private String username;

  private String vehicleDescs;

  private Boolean orderDescCustomerInfo;

  private Boolean orderDescById;

  private Boolean orderDescByOrderDate;

  private int page;

  private int size;

  public FinalCustomerOrderCriteria toFinalCustomerOrderyCriteria() {
    FinalCustomerOrderCriteria criteria = FinalCustomerOrderCriteria.builder()
        .statuses(statuses)
        .customerInfo(customerInfo)
        .id(NumberUtils.createLong(StringUtils.stripToNull(id)))
        .dateFrom(dateFrom)
        .dateTo(dateTo)
        .username(username)
        .vehicleDescs(vehicleDescs)
        .orderDescCustomerInfo(orderDescCustomerInfo)
        .orderDescById(orderDescById)
        .orderDescByOrderDate(orderDescByOrderDate)
        .build();
    criteria.setOffset(page);
    criteria.setPageSize(size);

    return criteria;
  }

}
