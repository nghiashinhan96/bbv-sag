package com.sagag.services.ax.domain.vendor;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VendorDeliveryInfo implements Serializable{

  private static final long serialVersionUID = 8717740391220027332L;
  
  private Date vendorCutOffTime;
  private Date vendorDeliveryTime;
  private Integer returnedQuantity;
  private String branchId;

}
