package com.sagag.services.article.api;

import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;

import java.util.List;
import java.util.Optional;

public interface ReturnExternalOrderService {

  /**
   * Retrieves the representation of a list of transaction references for a given parameter.
   *
   * @param compName The company to which the webshop belongs, e.g. 'Derendinger-Switzerland’.
   * @param reference The value of this parameter can refer to orderNr or deliveryNoteNr or
   *        invoiceNr or transaction Id
   * @return the list of <code>TransactionReferenceDto</code>
   */
  List<TransactionReferenceDto> searchTransactionReferences(String compName, String reference);

  /**
   * Provides functionality to create a return order and post it.
   *
   * @param companyName The company to which the webshop belongs, e.g.’Derendinger-Switzerland’.
   * @param request The request containing the return order to be created
   * @return the result of <code>ReturnOrder</code>
   */
  ReturnOrderDto createReturnOrder(String companyName, ReturnedOrderRequestBody request);

  /**
   * Provides functionality to search for return order batch job status.
   *
   * @param compName The company to which the webshop belongs, e.g.’Derendinger-Switzerland’.
   * @param batchJobIds The request batch job ids to be searched
   * @return the batch job status of <code>ReturnOrder</code>
   */
  Optional<ReturnOrderBatchJobsDto> searchReturnOrderBatchJobStatus(String compName,
      List<String> batchJobIds);

  /**
   * Provides functionality to search for return order number.
   *
   * @param compName The company to which the webshop belongs, e.g.’Derendinger-Switzerland’.
   * @param journalIds The request journal ids to be searched
   * @return the order numbers of <code>ReturnOrder</code>
   */
  Optional<ReturnOrderBatchJobsResultDto> searchReturnOrderNumber(String compName,
      List<String> journalIds);
}
