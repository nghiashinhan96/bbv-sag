package com.sagag.services.article.api.request.returnorder;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AxReturnOrderPositionRequest implements Serializable {

  private static final long serialVersionUID = -938776928953250954L;

  private String transId;

  private Integer quantity;

  private Boolean isQuarantine;

  private String quarantineReason;

  private String returnReasonCodeId;

}

