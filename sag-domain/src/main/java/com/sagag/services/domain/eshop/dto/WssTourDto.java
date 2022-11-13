package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WssTourDto implements Serializable {

  private static final long serialVersionUID = -4329577916141424534L;

  private Integer id;

  private Integer orgId;

  @NotBlank(message = "Tour name must not be blank")
  private String name;

  private List<WssTourTimesDto> wssTourTimesDtos;

}
