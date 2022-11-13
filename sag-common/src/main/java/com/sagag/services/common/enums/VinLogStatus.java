package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum VinLogStatus {

  OK(0),
  FAILED(1),
  ESTIMATE_USED(2);

  private int status;

  public boolean isOk() {
    return OK == this;
  }

  public static VinLogStatus findEstimateStatus(final String existingEstimateId) {
    if (StringUtils.isBlank(existingEstimateId)) {
      return VinLogStatus.OK;
    }
    return VinLogStatus.ESTIMATE_USED;
  }

}
