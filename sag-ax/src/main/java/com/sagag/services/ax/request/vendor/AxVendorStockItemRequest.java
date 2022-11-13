package com.sagag.services.ax.request.vendor;

import java.io.Serializable;

import lombok.Data;

@Data
public class AxVendorStockItemRequest implements Serializable {

  private static final long serialVersionUID = -7594450156362557162L;

  private String vendorArticleId;

  private int quantity;

}
