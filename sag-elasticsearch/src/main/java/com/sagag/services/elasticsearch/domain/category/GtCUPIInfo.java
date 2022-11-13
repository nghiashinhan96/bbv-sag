package com.sagag.services.elasticsearch.domain.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "zone", "cupi" })
public class GtCUPIInfo implements Serializable {

  private static final long serialVersionUID = 828454382965613261L;

  private String zone;

  private String cupi;

  private String loc;
}
