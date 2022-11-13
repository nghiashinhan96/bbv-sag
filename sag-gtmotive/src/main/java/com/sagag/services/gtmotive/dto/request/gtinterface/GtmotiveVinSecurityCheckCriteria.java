package com.sagag.services.gtmotive.dto.request.gtinterface;

import com.sagag.services.common.utils.SagBeanUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class GtmotiveVinSecurityCheckCriteria extends GtmotiveVehicleInfoCriteria
    implements Serializable {

  private static final long serialVersionUID = 9145745854128141413L;

  // #870: Keep vinlogid for session
  private String vinLogId;

  private String gtEquip;

  private boolean showGui;

  private boolean useIdCar;

  public GtmotiveVehicleInfoCriteria toGtmotiveVehicleInfoCriteria() {
    final GtmotiveVehicleInfoCriteria criteria = new GtmotiveVehicleInfoCriteria();
    SagBeanUtils.copyProperties(this, criteria);
    return criteria;
  }
}
