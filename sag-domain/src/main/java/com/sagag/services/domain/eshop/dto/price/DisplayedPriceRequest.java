package com.sagag.services.domain.eshop.dto.price;

import com.sagag.services.common.enums.PriceDisplayTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisplayedPriceRequest {

  private List<DisplayedPriceRequestItem> requestItems;

  private String companyName;

  private String custNr;

  private PriceDisplayTypeEnum priceTypeDisplayEnum;

  private boolean isFinalCustomerUser;

  private double vatRate;
}
