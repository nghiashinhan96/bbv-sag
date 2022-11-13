package com.sagag.services.common.domain.wss;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WssMarginGroupValueDto {
  private Double margin1;
  private Double margin2;
  private Double margin3;
  private Double margin4;
  private Double margin5;
  private Double margin6;
  private Double margin7;
}
