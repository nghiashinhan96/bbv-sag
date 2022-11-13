package com.sagag.services.domain.eshop.dto;

import com.sagag.services.common.enums.WeekDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssTourTimesDto implements Serializable {

  private static final long serialVersionUID = -3057722487312015980L;

  private Integer id;

  private WeekDay weekDay;

  private String departureTime;

}
