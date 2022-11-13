package com.sagag.services.ax.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.sag.erp.Tour;

import lombok.Data;

/**
 * Class to receive the tour to delivery article result from Dynamic AX ERP.
 *
 */
@Data
@JsonPropertyOrder(
    {
     "nr",
     "tourName",
     "startTime"
    })
public class AxTour implements Serializable {

  private static final long serialVersionUID = -6455223967417921266L;

  private String tourName;

  private String startTime;

  public Tour toTourDto() {
    return Tour.builder().tourName(this.tourName).startTime(this.startTime).build();
  }

}
