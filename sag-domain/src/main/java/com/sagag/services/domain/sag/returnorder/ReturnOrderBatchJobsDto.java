package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnOrderBatchJobsDto implements Serializable {

  private static final long serialVersionUID = 8193361366308172868L;

  private List<ReturnOrderBatchJobInfoDto> batchJobs;
}
