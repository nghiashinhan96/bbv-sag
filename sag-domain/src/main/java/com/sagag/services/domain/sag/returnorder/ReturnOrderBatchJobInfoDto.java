package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnOrderBatchJobInfoDto implements Serializable {

  private static final long serialVersionUID = -3414287894276615404L;

  private Long batchJobId;

  private String status;
}
