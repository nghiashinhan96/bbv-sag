package com.sagag.services.ax.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * Class to receive the tour time table result from Dynamic AX ERP.
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxTourTimeTable implements Serializable {

  private static final long serialVersionUID = 1081846424065185679L;

  private String tourName;

  private String startTime;

}
