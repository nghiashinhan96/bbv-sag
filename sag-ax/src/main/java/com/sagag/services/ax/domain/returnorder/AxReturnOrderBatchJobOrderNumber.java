package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;

import lombok.Data;

@Data
public class AxReturnOrderBatchJobOrderNumber implements Serializable {

  private static final long serialVersionUID = -8565686033522876511L;

  private String journalId;

  private String orderId;

}

