package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
@JsonPropertyOrder(
    {
     "nr",
     "tourName",
     "startTime"
    })
public class Tour implements Serializable {

  private static final long serialVersionUID = -6455223967417921266L;

  private Integer nr;

  private String tourName;

  private String startTime;

}
