package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.ReturnOrderReasonRepository;
import com.sagag.eshop.repo.entity.ReturnOrderReason;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.ReturnExternalOrderService;
import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.ax.enums.AxBatchJobStatus;
import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxTimeoutException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobInfoDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobOrderNumberDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderReasonDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import com.sagag.services.service.api.ReturnOrderService;
import com.sagag.services.service.enums.ReturnOrderErrorType;
import com.sagag.services.service.mail.returnorder.ReturnOrderCriteria;
import com.sagag.services.service.mail.returnorder.ReturnOrderMailSender;
import com.sagag.services.service.returnorder.ReturnTransactionReferencesFilter;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReturnOrderServiceImpl implements ReturnOrderService {


  public static final long MAX_BATCH_JOB_STATUS_SEARCH_TIME_IN_MINUTE = 15;

  public static final int BATCH_JOB_STATUS_SEARCH_DELAY_TIME_BY_SECOND = 30;

  @Autowired
  private ReturnOrderReasonRepository returnOrderReasonRepo;

  @Autowired
  private ReturnExternalOrderService axReturnOrderService;

  @Autowired
  protected ReturnOrderMailSender returnOrderMailSender;

  @Autowired
  private ReturnTransactionReferencesFilter returnTransactionReferencesFilter;

  @Autowired
  private EshopUserRepository eshopUserRepository;

  @Override
  public List<TransactionReferenceDto> searchTransactionReferences(
      final SupportedAffiliate affiliate, final String reference, final String userType) {
    return returnTransactionReferencesFilter.filterTransactionReferences(affiliate, reference,
        userType);
  }

  @Override
  public List<ReturnOrderReasonDto> getReturnOrderReason() {
    return returnOrderReasonRepo.findAll().stream().map(reasonConverter())
        .collect(Collectors.toList());
  }

  private static Function<ReturnOrderReason, ReturnOrderReasonDto> reasonConverter() {
    return entity -> ReturnOrderReasonDto.builder().reasonId(entity.getAxCode())
        .reasonCode(entity.getCode()).reasonName(entity.getName()).isDefault(entity.isDefault())
        .build();
  }

  @Override
  public ReturnOrderDto createReturnOrder(final SupportedAffiliate affiliate,
      final ReturnedOrderRequestBody body) {
    log.debug("Creating return order by affiliate = {} and request = {}", affiliate, body);
    return axReturnOrderService.createReturnOrder(affiliate.getCompanyName(), body);
  }

  @Override
  public Optional<ReturnOrderBatchJobsDto> searchReturnOrderBatchJobs(
      final SupportedAffiliate affiliate, final String batchJobId) {
    log.debug("Creating return order by affiliate = {} and request = {}", affiliate, batchJobId);
    final ArrayList<String> batchJobIds = Lists.newArrayList(batchJobId);
    return axReturnOrderService.searchReturnOrderBatchJobStatus(affiliate.getCompanyName(),
        batchJobIds);
  }

  @Override
  public Optional<ReturnOrderBatchJobsResultDto> searchReturnOrderBatchJobOrderNumbers(
      final SupportedAffiliate affiliate, String journalId) {
    final ArrayList<String> journalIds = Lists.newArrayList(journalId);
    return axReturnOrderService.searchReturnOrderNumber(affiliate.getCompanyName(), journalIds);
  }

  @Async
  @Override
  public void processReturnOrderBatchJobInBackground(final SupportedAffiliate affiliate,
      final UserInfo user, final String journalId, final String batchJobId)
      throws InterruptedException {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    while (stopWatch.getTime() < TimeUnit.MINUTES
        .toMillis(MAX_BATCH_JOB_STATUS_SEARCH_TIME_IN_MINUTE)) {
      if (isReturnOrderBatchJobFinished(affiliate, user, journalId, batchJobId)) {
        return;
      }
      TimeUnit.SECONDS.sleep(BATCH_JOB_STATUS_SEARCH_DELAY_TIME_BY_SECOND);
    }
    handleBatchJobRunningResponse(batchJobId, user);
    stopWatch.stop();
  }

  private boolean isReturnOrderBatchJobFinished(SupportedAffiliate affiliate, UserInfo user,
      String journalId, String batchJobId) {
    try {
      Optional<ReturnOrderBatchJobsDto> returnOrderBatchJobsOpt =
          this.searchReturnOrderBatchJobs(affiliate, batchJobId);
      if (returnOrderBatchJobsOpt.isPresent()) {
        AxBatchJobStatus returnOrderBatchJobStatus =
            findReturnOrderBatchJobStatus(affiliate, batchJobId);
        if (Objects.nonNull(returnOrderBatchJobStatus) && returnOrderBatchJobStatus.isFinished()) {
          sendReturnOrderBatchJobResultEmail(affiliate, user, journalId, batchJobId,
              returnOrderBatchJobStatus);
          return true;
        }
      }
    } catch (AxTimeoutException ex) {
      log.error("Find return order batch job status request timeout", ex);
    }
    return false;
  }

  private void handleBatchJobRunningResponse(final String batchJobId, final UserInfo user) {
    String salesEmail = getSalesEmail(user);
    returnOrderMailSender
        .send(ReturnOrderCriteria.builder().toEmail(salesEmail).orderNumbers(Lists.emptyList())
            .langiso(user.getLanguage()).batchJobId(batchJobId).isError(true)
            .errorType(ReturnOrderErrorType.STATUS_CHECK_TIME_OUT)
            .affiliateEmail(user.getSettings().getAffiliateEmail()).build());
  }

  private void sendReturnOrderBatchJobResultEmail(final SupportedAffiliate affiliate,
      final UserInfo user, final String journalId, final String batchJobId,
      AxBatchJobStatus returnOrderBatchJobStatus) {
    final boolean isError =
        returnOrderBatchJobStatus.isError() || returnOrderBatchJobStatus.isCanceled();
    List<String> orderNumbers = Lists.emptyList();
    String salesEmail = getSalesEmail(user);
    if (!isError) {
      try {
        Optional<ReturnOrderBatchJobsResultDto> returnOrderBatchJobsResult = axReturnOrderService
            .searchReturnOrderNumber(affiliate.getCompanyName(), Lists.newArrayList(journalId));
        if (returnOrderBatchJobsResult.isPresent()) {
          orderNumbers = CollectionUtils
              .emptyIfNull(returnOrderBatchJobsResult.get().getOrderNumbers()).stream()
              .map(ReturnOrderBatchJobOrderNumberDto::getOrderId).collect(Collectors.toList());
        }
      } catch (AxExternalException ex) {
        returnOrderMailSender.send(ReturnOrderCriteria.builder().toEmail(salesEmail)
            .orderNumbers(Lists.emptyList()).langiso(user.getLanguage()).batchJobId(batchJobId)
            .isError(true).errorType(ReturnOrderErrorType.GET_RETURN_ORDER_ORDER_NUMBER_FAILED)
            .affiliateEmail(user.getSettings().getAffiliateEmail()).build());
        return;
      }
    }
    returnOrderMailSender.send(ReturnOrderCriteria.builder().toEmail(salesEmail)
        .orderNumbers(orderNumbers).langiso(user.getLanguage()).isError(isError)
        .errorType(ReturnOrderErrorType.STATUS_CHECK_RETURN_ERROR).batchJobId(batchJobId)
        .affiliateEmail(user.getSettings().getAffiliateEmail()).build());
  }

  private String getSalesEmail(UserInfo user) {
    String salesEmail =
        eshopUserRepository.findEmailById(user.getSalesId()).orElse(StringUtils.EMPTY);
    return salesEmail;
  }

  private AxBatchJobStatus findReturnOrderBatchJobStatus(final SupportedAffiliate affiliate,
      final String batchJobId) {
    Optional<ReturnOrderBatchJobsDto> returnOrderBatchJobsOpt =
        this.searchReturnOrderBatchJobs(affiliate, batchJobId);
    if (!returnOrderBatchJobsOpt.isPresent()) {
      return null;
    }
    List<ReturnOrderBatchJobInfoDto> batchJobs = returnOrderBatchJobsOpt.get().getBatchJobs();
    return CollectionUtils.emptyIfNull(batchJobs).stream()
        .filter(batchJob -> StringUtils.equalsIgnoreCase(
            Objects.toString(batchJob.getBatchJobId(), StringUtils.EMPTY), batchJobId))
        .findFirst()
        .map(batchJob -> AxBatchJobStatus.valueOf(StringUtils.upperCase(batchJob.getStatus())))
        .orElse(null);
  }
}
