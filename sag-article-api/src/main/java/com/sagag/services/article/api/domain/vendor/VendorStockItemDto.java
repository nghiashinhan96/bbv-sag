package com.sagag.services.article.api.domain.vendor;

import lombok.Data;

@Data
public class VendorStockItemDto {

  private String vendorArticleId;

  private int quantity;
}
