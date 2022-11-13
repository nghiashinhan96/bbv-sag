package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderReasonDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import java.util.List;
import java.util.Optional;

/**
 * Interface to define return articles business service APIs.
 */
public interface ReturnOrderService {

  /**
   * Returns the transaction references.
   *
   * @param affiliate the supported affiliate
   * @param reference the search reference
   * @param userType the user type
   * @return the list of {@link TransactionReferenceDto}
   */
  List<TransactionReferenceDto> searchTransactionReferences(SupportedAffiliate affiliate,
      String reference, String userType);

  /**
   * Returns the return order batch jobs status.
   *
   * @param affiliate the supported affiliate
   * @param batchJobId the return order batch job id to be searched
   * @return {@link ReturnOrderBatchJobsDto}
   */
  Optional<ReturnOrderBatchJobsDto> searchReturnOrderBatchJobs(SupportedAffiliate affiliate,
      String batchJobId);

  /**
   * Returns the return order reasons.
   *
   * @return the list of {@link ReturnOrderReasonDto}
   */
  List<ReturnOrderReasonDto> getReturnOrderReason();

  /**
   * Creates the return order.
   *
   * @param affiliate the supported affiliate
   * @param body the return order request
   * @return the result of {@link ReturnOrderDto}
   */
  ReturnOrderDto createReturnOrder(SupportedAffiliate affiliate, ReturnedOrderRequestBody body);

  /**
   * Search return order batch job order numbers.
   *
   * @param affiliate the supported affiliate
   * @param journalId the return order batch job journal id
   * @return the result of {@link ReturnOrderBatchJobsResultDto}
   */
  Optional<ReturnOrderBatchJobsResultDto> searchReturnOrderBatchJobOrderNumbers(
      SupportedAffiliate affiliate, String journalId);

  /**
   * Process the return order batch job in background.
   *
   * @param affiliate
   * @param user
   * @param journalId
   * @param batchJobId
   */
  void processReturnOrderBatchJobInBackground(SupportedAffiliate affiliate, UserInfo user,
      String journalId, String batchJobId) throws InterruptedException;
}
