package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnOrderBatchJobsResultDto implements Serializable {

  private static final long serialVersionUID = -242610183928859608L;
  
  private List<ReturnOrderBatchJobOrderNumberDto> orderNumbers;
}
