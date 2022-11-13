package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OatesChangeIntervalDto implements Serializable {

  private static final long serialVersionUID = -1983383464872340035L;

  private String applicationId;

  private String applicationName;

  private List<OatesIntervalDto> intervals;

}
