package com.sagag.services.ax.request.vendor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.article.api.request.BasketPosition;

import lombok.Data;

@Data
public class AxVendorStockRequest implements Serializable {

  private static final long serialVersionUID = -6255767276142854704L;

  private List<AxVendorStockItemRequest> stocks;

  private String branchId;

  public AxVendorStockRequest(String branchId, List<BasketPosition> positions) {
    setBranchId(StringUtils.defaultString(branchId));
    setStocks(ListUtils.emptyIfNull(positions).stream().map(positionMapper())
        .collect(Collectors.toList()));
  }

  private static Function<BasketPosition, AxVendorStockItemRequest> positionMapper() {
    return position -> {
      AxVendorStockItemRequest item = new AxVendorStockItemRequest();
      item.setVendorArticleId(position.getExternalArticleId());
      item.setQuantity(position.getQuantity());
      return item;
    };
  }

}
