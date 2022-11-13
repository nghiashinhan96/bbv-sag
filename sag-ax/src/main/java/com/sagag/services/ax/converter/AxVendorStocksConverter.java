package com.sagag.services.ax.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.domain.vendor.VendorStockItemDto;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.ax.domain.vendor.AxVendorStock;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class AxVendorStocksConverter
    implements BiFunction<AxVendorStock, List<BasketPosition>, Optional<VendorStockDto>> {

  @Override
  public Optional<VendorStockDto> apply(AxVendorStock axVendorStock,
      List<BasketPosition> originalPositions) {
    if (CollectionUtils.isEmpty(originalPositions) || axVendorStock == null) {
      return Optional.empty();
    }
    final VendorStockDto vendorStock = new VendorStockDto();
    vendorStock.setDeliveryDate(axVendorStock.getDeliveryDate());
    vendorStock.setCutoffTime(axVendorStock.getCutoffTime());
    vendorStock.setEPriority(axVendorStock.getEPriority());
    
    final Map<String, VendorStockItemDto> vendorStockItems = new HashMap<>();
    ListUtils.emptyIfNull(axVendorStock.getStocks()).stream()
    .forEach(axVendorStockItem -> findArticleIdByExternalVendorId(originalPositions,
        axVendorStockItem.getVendorArticleId())
        .ifPresent(articleId -> vendorStockItems.put(articleId, axVendorStockItem.toDto())));
    vendorStock.setVendorStockItems(vendorStockItems);
    return Optional.of(vendorStock);
  }

  private static Optional<String> findArticleIdByExternalVendorId(
      List<BasketPosition> originalPositions, String externalVendorId) {
    return originalPositions.stream()
        .filter(position -> StringUtils.equalsIgnoreCase(
            externalVendorId, position.getExternalArticleId()))
        .map(BasketPosition::getArticleId).findFirst();
  }
}
