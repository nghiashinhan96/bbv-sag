package com.sagag.services.ivds.request;

import lombok.Data;

@Data
public class ErpInfoRequest {

  private boolean isPriceRequested;

  private boolean isAvailabilityRequested;

  private boolean isStockRequested;
}
