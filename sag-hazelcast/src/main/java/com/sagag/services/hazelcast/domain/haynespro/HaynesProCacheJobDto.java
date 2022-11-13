package com.sagag.services.hazelcast.domain.haynespro;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.haynespro.LabourTimeJobDto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class HaynesProCacheJobDto implements Serializable {

  private static final long serialVersionUID = 2064463111006574342L;

  private String id;

  private String name;

  private String type;

  private String awNumber;

  private String oeCode;

  private String time;

  private String labourRate;

  public LabourTimeJobDto toLabourTimeJobDto(double vatRate) {
    LabourTimeJobDto dto = SagBeanUtils.map(this, LabourTimeJobDto.class);
    dto.setTime(Objects.isNull(getTime()) ? null : Double.valueOf(getTime()));
    dto.setLabourRate(Objects.isNull(getLabourRate()) ? null : Double.valueOf(getLabourRate()));
    dto.setLabourRateWithVat(Objects.isNull(getLabourRate()) ? null
        : Double.valueOf(getLabourRate()) * (vatRate * SagConstants.PERCENT + 1));
    return dto;
  }

}
