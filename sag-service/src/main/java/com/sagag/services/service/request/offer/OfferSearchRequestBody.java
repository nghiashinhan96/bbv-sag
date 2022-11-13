package com.sagag.services.service.request.offer;

import lombok.Data;

import java.io.Serializable;

@Data
public class OfferSearchRequestBody implements Serializable {

  private static final long serialVersionUID = -8426791674358111325L;

  private String offerNumber;

  private String customerName;

  private String remark;

  private String vehicleDesc;

  private String offerDate;

  private String price;

  private String status;

  private String username;



  private Boolean orderDescByOfferNumber;

  private Boolean orderDescByCustomerName;

  private Boolean orderDescByRemark;

  private Boolean orderDescByVehicleDesc;

  private Boolean orderDescByOfferDate;

  private Boolean orderDescByPrice;

  private Boolean orderDescByStatus;

  private Boolean orderDescByUsername;

}
