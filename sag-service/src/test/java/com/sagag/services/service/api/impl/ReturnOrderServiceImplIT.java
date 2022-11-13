package com.sagag.services.service.api.impl;

import com.sagag.services.article.api.request.returnorder.ReturnedOrderPosition;
import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderPositionDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderReasonDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.ReturnOrderService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Integration test class for return order business service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class ReturnOrderServiceImplIT {

  private static final SupportedAffiliate AFF = SupportedAffiliate.DERENDINGER_AT;

  @Autowired
  private ReturnOrderService service;

  @Test
  @Ignore("Because this is the real data")
  public void shouldCreateReturnOrder() {
    final ReturnedOrderRequestBody body = new ReturnedOrderRequestBody();
    body.setBranchId(AxConstants.DEFAULT_BRANCH_ID);
    body.setSalesPersonalNumber("axtest01");
    body.setPrintConfirmDoc(false);
    body.setPositions(Arrays.asList(
        createReturnOrderPositionRequest("300021007956", 1, false)));

    final ReturnOrderDto actual = service.createReturnOrder(AFF, body);
    Assert.assertThat(actual.getReturnOrderUrl(),
        Matchers.stringContainsInOrder(Arrays.asList("ReturnOrderLine")));
    Assert.assertThat(actual.getReturnOrderPositions(), Matchers.not(Matchers.empty()));
    for (ReturnOrderPositionDto position : actual.getReturnOrderPositions()) {
      assertOneReturnOrderPosition(position);
    }
  }

  private static ReturnedOrderPosition createReturnOrderPositionRequest(final String transId,
      final int quantity, final boolean isQ) {
    ReturnedOrderPosition position = new ReturnedOrderPosition();
    position.setTransactionId(transId);
    position.setQuantity(quantity);
    position.setQuarantine(isQ);
    if (isQ) {
      position.setQuarantineReason(RandomStringUtils.randomAlphabetic(24));
    }
    return position;
  }

  private static void assertOneReturnOrderPosition(ReturnOrderPositionDto position) {
    Assert.assertThat(position.getOrderNr(), Matchers.notNullValue());
    Assert.assertThat(position.getOrderUrl(),
        Matchers.stringContainsInOrder(Arrays.asList("ReturnOrderTableDetail")));
    Assert.assertThat(position.getQuarantineOrder(), Matchers.isOneOf(Boolean.TRUE, Boolean.FALSE));
  }

  @Test
  public void shouldSearchTransactionReferences() {
    final String ref = "3000266446";
    final String userType = StringUtils.EMPTY;
    final List<TransactionReferenceDto> actual =
        service.searchTransactionReferences(AFF, ref, userType);
    log.debug("JSON = {}", SagJSONUtil.convertObjectToPrettyJson(actual));
    Assert.assertThat(actual.size(), Matchers.greaterThanOrEqualTo(0));
    if (CollectionUtils.isEmpty(actual)) {
      return;
    }
    for (TransactionReferenceDto item : actual) {
      assertOneTransactionReference(item);
    }
  }

  private static void assertOneTransactionReference(TransactionReferenceDto item) {
    Assert.assertThat(item, Matchers.notNullValue());
    Assert.assertThat(item.getTransId(), Matchers.notNullValue());
    Assert.assertThat(item.getArticleId(), Matchers.notNullValue());
    Assert.assertThat(item.getArticleName(), Matchers.notNullValue());
    Assert.assertThat(item.getArticleKeyword(), Matchers.notNullValue());
    Assert.assertThat(item.getOrderNr(), Matchers.notNullValue());
    Assert.assertThat(item.getCustomerNr(), Matchers.notNullValue());
    Assert.assertThat(item.getCustomerName(), Matchers.notNullValue());
    Assert.assertThat(item.getQuantity(), Matchers.notNullValue());
    Assert.assertThat(item.getReturnQuantity(), Matchers.notNullValue());
    Assert.assertThat(item.getBranchId(), Matchers.notNullValue());
    Assert.assertThat(item.getPaymentType(), Matchers.notNullValue());
    Assert.assertThat(item.getTermOfPayment(), Matchers.notNullValue());
    Assert.assertThat(item.getCashDiscount(), Matchers.notNullValue());
    Assert.assertThat(item.getUnitOfMeasurement(), Matchers.notNullValue());
    Assert.assertThat(item.getSourcingType(), Matchers.notNullValue());
  }

  @Test
  public void shouldReturnAllReasons() {
    final List<ReturnOrderReasonDto> actual = service.getReturnOrderReason();
    Assert.assertThat(actual.size(), Matchers.greaterThanOrEqualTo(9));
    for (ReturnOrderReasonDto reason : actual) {
      Assert.assertThat(reason, Matchers.notNullValue());
      Assert.assertThat(reason.getReasonId(), Matchers.notNullValue());
      Assert.assertThat(reason.getReasonCode(), Matchers.notNullValue());
      Assert.assertThat(reason.getReasonName(), Matchers.notNullValue());
      Assert.assertThat(reason.isDefault(), Matchers.notNullValue());
    }
  }
}
