package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ReturnOrderReasonDto implements Serializable {

  private static final long serialVersionUID = 6595675727455219441L;

  private String reasonId;

  private String reasonCode;

  private String reasonName;

  @JsonProperty("default")
  private boolean isDefault;

}
