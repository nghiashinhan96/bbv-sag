package com.sagag.eshop.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity mapping to ORDER_HISTORY table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FINAL_CUSTOMER_ORDER_ITEM")
@Entity
public class FinalCustomerOrderItem implements Serializable {

  private static final long serialVersionUID = -283079023483744287L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long finalCustomerOrderId;

  private String vehicleId;

  private String vehicleDesc;

  private String articleId;

  private String articleDesc;

  private Integer quantity;

  private String type;

  private String genArtDescription;

  private String supplier;

  private String brand;

  private String images;

  private String reference;

  private Double grossPrice;

  private Double netPrice;

  private Integer salesQuantity;

  private String productAddon;

  private String displayedPriceType;

  private String displayedPriceBrand;

  private String displayedPriceBrandId;

  private String availabilities;

  private Double finalCustomerNetPrice;

  private Double grossPriceWithVat;

  private Double finalCustomerNetPriceWithVat;

  private String attachItems;
}
