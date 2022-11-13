package com.sagag.services.ax.domain.vendor;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AxVendorStock implements Serializable {

  private static final long serialVersionUID = 5836671616356256445L;

  private List<AxVendorStockItem> stocks;

  private String deliveryDate;

  private String cutoffTime;

  // Default value if this field is not present in the response of ERP
  // Its also a flag to check if we have to use the vendor priority from
  // DB in case ERP is not yet ready for ePriority
  private Integer ePriority = ExternalStockInfo.MISSING_VENDOR_E_PRIORITY_;

  private String errorMessage;

}
