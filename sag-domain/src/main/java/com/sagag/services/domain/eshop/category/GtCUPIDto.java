package com.sagag.services.domain.eshop.category;

import java.io.Serializable;
import lombok.Data;

@Data
public class GtCUPIDto implements Serializable {

  private static final long serialVersionUID = 6167408851313524914L;

  private String zone;

  private String cupi;

  private String loc;

}
