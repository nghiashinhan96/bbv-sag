package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;

import lombok.Data;

@Data
public class AxReturnOrderBatchJobInfo implements Serializable {

  private static final long serialVersionUID = -6044422990157335780L;

  private Long batchJobId;

  private String status;

}

