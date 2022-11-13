package com.sagag.eshop.repo.criteria.offer;

import lombok.Data;

@Data
public class ShopArticleSearchCriteria {

  private String articleNumber;

  private String name;

  private String description;

  private Double amount;

  private Double price;

  private String type;

  private Integer organisationId;

  private Boolean orderDescByNumber;

  private Boolean orderDescByName;

  private Boolean orderDescByDescription;

  private Boolean orderDescByAmount;

  private Boolean orderDescByPrice;

}
