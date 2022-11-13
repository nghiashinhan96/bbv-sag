package com.sagag.services.article.api.request.returnorder;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AxReturnOrderRequest implements Serializable {

  private static final long serialVersionUID = 7276958943882171762L;

  private String branchId;

  private String personalNumber;

  private String returnOrderName;

  private Boolean returnOrderDocumentConfirmationPrint;

  private Boolean returnOrderJournalPostingInBatch;

  private List<AxReturnOrderPositionRequest> returnOrderRequestPositions;


}

