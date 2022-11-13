package com.sagag.services.common.domain.wss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WssMarginGroupDto {
  private Integer id;
  private String name;
}
