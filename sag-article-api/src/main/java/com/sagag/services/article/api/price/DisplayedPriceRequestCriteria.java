package com.sagag.services.article.api.price;

import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;

import lombok.Data;

@Data
public class DisplayedPriceRequestCriteria {

  private String brand;

  private Long brandId;

  private String companyName;

  private boolean isFinalCustomerUser;

  private DisplayedPriceDto price;

  private double vatRate;

  private int amountNumber;

  private String articleId;
}
