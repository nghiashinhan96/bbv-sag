package com.sagag.services.article.api.request.returnorder;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class ReturnedOrderRequestBody implements Serializable {

  private static final long serialVersionUID = 2715991812289936183L;

  private String branchId;

  private String salesPersonalNumber;

  private boolean printConfirmDoc;

  private boolean returnOrderJournalPostingInBatch;

  private List<ReturnedOrderPosition> positions;

  public boolean isValidToExecute() {
    return !StringUtils.isAnyBlank(branchId, salesPersonalNumber)
        && !CollectionUtils.isEmpty(positions);
  }

  public AxReturnOrderRequest toAxRequest() {
    return axRequestConverter().apply(this);
  }

  private static Function<ReturnedOrderRequestBody, AxReturnOrderRequest> axRequestConverter() {
    return body -> {
      final AxReturnOrderRequest request = new AxReturnOrderRequest();
      // If the sales agent submits the articles by clicking on "> Erstatten"
      // then the system will create the required group of
      // ReturnOrderRequest containing the corresponding
      // ReturnOrderReqeustPositions, build from the items of the return basket items:
      // ReturnOrderRequest .branchID is empty.
      // Always force reset to EMPTY
      request.setBranchId(StringUtils.EMPTY);
      request.setPersonalNumber(body.getSalesPersonalNumber());
      request.setReturnOrderDocumentConfirmationPrint(body.isPrintConfirmDoc());
      request.setReturnOrderJournalPostingInBatch(body.isReturnOrderJournalPostingInBatch());
      request.setReturnOrderRequestPositions(ListUtils.emptyIfNull(body.getPositions())
          .stream().map(positionConverter()).collect(Collectors.toList()));
      return request;
    };
  }

  private static Function<ReturnedOrderPosition, AxReturnOrderPositionRequest> positionConverter() {
    return position -> {
      final AxReturnOrderPositionRequest pRequest = new AxReturnOrderPositionRequest();
      pRequest.setTransId(position.getTransactionId());
      pRequest.setQuantity(position.getQuantity());
      pRequest.setReturnReasonCodeId(StringUtils.defaultString(position.getReasonCode()));
      pRequest.setIsQuarantine(position.isQuarantine());
      pRequest.setQuarantineReason(StringUtils.defaultString(position.getQuarantineReason()));
      return pRequest;
    };
  }

}
