package com.sagag.eshop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WssMarginBrandDto implements Serializable {

  private static final long serialVersionUID = 6558472980094066113L;

  private Integer id;

  private Integer orgId;

  private Integer brandId;

  @NotBlank(message = "Brand name must not be blank")
  private String name;

  @NotNull(message = "First margin must not be null")
  private Double margin1;

  @NotNull(message = "Second margin must not be null")
  private Double margin2;

  @NotNull(message = "Third margin must not be null")
  private Double margin3;

  @NotNull(message = "Fourth margin must not be null")
  private Double margin4;

  @NotNull(message = "Fifth margin must not be null")
  private Double margin5;

  @NotNull(message = "Sixth margin must not be null")
  private Double margin6;

  @NotNull(message = "Seventh margin must not be null")
  private Double margin7;

  private boolean isDefault;
}
