package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceSettingDto {

  private boolean allowViewBillingChanged;

  private boolean allowNetPriceChanged;

  private boolean allowNetPriceConfirmChanged;

}
