package com.sagag.services.ax.domain.vendor;

import java.io.Serializable;

import com.sagag.services.article.api.domain.vendor.VendorStockItemDto;
import com.sagag.services.common.utils.SagBeanUtils;

import lombok.Data;

@Data
public class AxVendorStockItem implements Serializable {

  private static final long serialVersionUID = -7861350083657796600L;

  private String vendorArticleId;

  private int quantity;

  public VendorStockItemDto toDto() {
    return SagBeanUtils.map(this, VendorStockItemDto.class);
  }

}
