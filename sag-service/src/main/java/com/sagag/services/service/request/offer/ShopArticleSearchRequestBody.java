package com.sagag.services.service.request.offer;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopArticleSearchRequestBody implements Serializable {

  private static final long serialVersionUID = 3119044861462795596L;

  private String articleNumber;

  private String name;

  private String description;

  private Double amount;

  private Double price;

  private String type;

  private Boolean orderDescByNumber;

  private Boolean orderDescByName;

  private Boolean orderDescByDescription;

  private Boolean orderDescByAmount;

  private Boolean orderDescByPrice;
}
