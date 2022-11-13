package com.sagag.services.domain.eshop.mail;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderArticleItem implements Serializable {

  private static final long serialVersionUID = -160197057160363296L;

  private String vehicleInfo;
  private int amount;
  private String artnrDisplay;
  private String articleText;
  private String grossPrice;
  private String netPrice;
  private String remark;
  private String arrivalDate;
  private String priceType;
  private String finalCustomerNetPrice;
}
