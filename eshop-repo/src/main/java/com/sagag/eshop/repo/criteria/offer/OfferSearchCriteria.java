package com.sagag.eshop.repo.criteria.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferSearchCriteria  {

  private String offerNumber;

  private String customerName;

  private String remark;

  private String vehicleDesc;

  private String offerDate;

  private String price; // String to compare LIKE condition

  private String status;

  private String username;

  private Integer organisationId;

  private Boolean orderDescByOfferNumber;

  private Boolean orderDescByCustomerName;

  private Boolean orderDescByRemark;

  private Boolean orderDescByVehicleDesc;

  private Boolean orderDescByOfferDate;

  private Boolean orderDescByPrice;

  private Boolean orderDescByStatus;

  private Boolean orderDescByUsername;

  private String collectionShortname;

}
