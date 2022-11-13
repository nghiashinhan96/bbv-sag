package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OciItemDto implements Serializable {

  private static final long serialVersionUID = 3412163075917034821L;

  /**
   * articleDescription
   */
  private String description;

  /**
   * ewbSchemaType
   * We get it from configuration (organisation_settings)
   */
  private String schemaType;

  /**
   * articleNumber
   */
  private String categoryId;

  /**
   * quantityOfItemUnit
   */
  private Integer quantity;

  /**
   * unitOfMeasure
   * This will be very rare. Suggest to make this a “phase 2” item.
   * The UOM will depend upon the article and we will need to also convert (ex: ml <-> l)
   */
  private String unit;

  /**
   * netPrice
   */
  private Double price;

  /**
   * pricedQuantity
   * It is saleQuantity (menge)
   */
  private Integer priceUnit;

  /**
   * currency
   * This should be a setting anyway per affiliate
   */
  private String currency;

  /**
   * deliverDate
   * We know what it “today”, “next day” etc. from our Availability logic, so just count the working days.
   * We need to check if these are working or actual days. Assume Working for now.
   *
   */
  private Integer leadTime;

  /**
   * vendorId
   */
  private String vendor;

  /**
   * UmsArtId
   */
  private String vendorMat;

  /**
   * branchName
   * We only have this with AX. We would need to possibly add to the organization_setttings
   */
  private String custField1;

  /**
   * deliveryType
   * (DeliveryType.TOUR ? "01" : "02")
   */
  private String custField2;

}
