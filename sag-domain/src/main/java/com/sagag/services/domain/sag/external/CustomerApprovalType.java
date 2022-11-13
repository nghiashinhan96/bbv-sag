package com.sagag.services.domain.sag.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class POJO for contact of CustomerApprovalType.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerApprovalType implements Serializable {

  private static final long serialVersionUID = 4119348523945156687L;

  public String approvalTypeName;

  public String certificate;
}