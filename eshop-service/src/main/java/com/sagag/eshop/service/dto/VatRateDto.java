package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "artId", "customVatRate", "comment" })
public class VatRateDto implements Serializable {

  private static final long serialVersionUID = 4793968740146170730L;

  private Integer id;

  private String artId;

  private Double customVatRate;

  private String comment;

}
