package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnOrderBatchJobOrderNumberDto implements Serializable {

  private static final long serialVersionUID = -3137621984869774659L;

  private String journalId;

  private String orderId;
}
