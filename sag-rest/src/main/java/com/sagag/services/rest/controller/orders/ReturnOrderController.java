package com.sagag.services.rest.controller.orders;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderReasonDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import com.sagag.services.rest.resource.ReturnOrderResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.ReturnOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class to define APIs for return items.
 */
@RestController
@RequestMapping(value = "/return/order", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Return Orders APIs")
public class ReturnOrderController {

  @Autowired
  private ReturnOrderService returnOrderService;

  @ApiOperation(value = ApiDesc.ReturnOrder.SEARCH_RETURN_ORDERS_API_DESC,
      notes = ApiDesc.ReturnOrder.SEARCH_RETURN_ORDERS_API_NOTE)
  @GetMapping("/search")
  public List<TransactionReferenceDto> searchReturnOrders(final OAuth2Authentication authed,
      @RequestParam("reference") final String reference) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        returnOrderService.searchTransactionReferences(affiliate, reference, user.getType()));
  }

  @ApiOperation(value = ApiDesc.ReturnOrder.REASON_API_DESC,
    notes = ApiDesc.ReturnOrder.REASON_API_NOTE)
  @GetMapping("/reason")
  public List<ReturnOrderReasonDto> getReturnOrderReasons() throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        returnOrderService.getReturnOrderReason());
  }

  @ApiOperation(value = ApiDesc.ReturnOrder.CREATE_RETURN_ORDER_API_DESC,
      notes = ApiDesc.ReturnOrder.CREATE_RETURN_ORDER_API_NOTE)
  @PostMapping("/create")
  public ReturnOrderResource createReturnOrder(final OAuth2Authentication authed,
      @RequestBody ReturnedOrderRequestBody body) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    return ReturnOrderResource.of(returnOrderService.createReturnOrder(affiliate, body));
  }

  @ApiOperation(value = ApiDesc.ReturnOrder.GET_RETURN_ORDER_BATCH_JOB_STATUS_API_DESC,
      notes = ApiDesc.ReturnOrder.GET_RETURN_ORDER_BATCH_JOB_STATUS_API_NOTE)
  @GetMapping("/batch-jobs")
  public ReturnOrderBatchJobsDto searchReturnOrdersBatchJobStatus(final OAuth2Authentication authed,
      @RequestParam("batchJobId") final String batchJobId) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        returnOrderService.searchReturnOrderBatchJobs(affiliate, batchJobId));
  }

  @ApiOperation(value = ApiDesc.ReturnOrder.GET_RETURN_ORDER_BATCH_JOB_RESULT_API_DESC,
      notes = ApiDesc.ReturnOrder.GET_RETURN_ORDER_BATCH_JOB_RESULT_API_NOTE)
  @GetMapping("/journals")
  public ReturnOrderBatchJobsResultDto searchReturnOrdersNumber(final OAuth2Authentication authed,
      @RequestParam("journalId") final String journalId) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        returnOrderService.searchReturnOrderBatchJobOrderNumbers(affiliate, journalId));
  }

  @ApiOperation(value = ApiDesc.ReturnOrder.GET_RETURN_ORDER_BATCH_JOB_STATUS_API_DESC,
      notes = ApiDesc.ReturnOrder.GET_RETURN_ORDER_BATCH_JOB_STATUS_API_NOTE)
  @PostMapping("/process-in-background")
  public void processInBackground(final OAuth2Authentication authed,
      @RequestParam("batchJobId") final String batchJobId,
      @RequestParam("journalId") final String journalId) throws InterruptedException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    returnOrderService.processReturnOrderBatchJobInBackground(affiliate, user, journalId,
        batchJobId);
  }

}
