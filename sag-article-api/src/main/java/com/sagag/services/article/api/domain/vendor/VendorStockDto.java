package com.sagag.services.article.api.domain.vendor;

import lombok.Data;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Optional;

@Data
public class VendorStockDto {

  private Map<String, VendorStockItemDto> vendorStockItems;

  private String deliveryDate;

  private String cutoffTime;
  
  private String branchId;
  
  private Integer ePriority;
  
  private String vendorId;

  public boolean containArticle(String vendorArticleId) {
    if (MapUtils.isEmpty(vendorStockItems)) {
      return false;
    }

    return vendorStockItems.entrySet().stream()
        .anyMatch(item -> vendorArticleId.equalsIgnoreCase(item.getValue().getVendorArticleId()));
  }

  public Optional<Integer> getReturnedQuantity(String idSagsys) {
    return Optional.ofNullable(vendorStockItems.get(idSagsys)).map(VendorStockItemDto::getQuantity);
  }
}
