package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(value = Include.NON_NULL)
public class CrossReferenceRequest implements Serializable {

  private static final long serialVersionUID = -2214089747750949884L;

  @JsonProperty("articleNumber")
  private String articleNumber;
  @JsonProperty("dlnrId")
  private String brandId;
}
