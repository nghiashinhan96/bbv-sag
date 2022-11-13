package com.sagag.services.domain.sag.haynespro;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class LabourTimeJobDto implements Serializable {

  private static final long serialVersionUID = -5649460515565309988L;

  private String id;

  private String name;

  private String type;

  private String awNumber;

  private String oeCode;

  private Double time;

  private Double labourRate;

  private Double labourRateWithVat;

}
