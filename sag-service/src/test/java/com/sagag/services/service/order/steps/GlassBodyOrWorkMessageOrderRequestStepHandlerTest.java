package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class GlassBodyOrWorkMessageOrderRequestStepHandlerTest {

  private static final String SAMPLE_TEXT =
      " zfa19800004361472 VF1KZ1Y0248342975 BITTE MIT VIN PRUEFEN";

  @InjectMocks
  private GlassBodyOrWorkMessageOrderRequestStepHandler handler;

  @Test
  public void shouldUpdateVinInfoTextToCustomerRefTextWithAfterEndWorkingTime() {
    final SupportedAffiliate affiliate = OrderedDataProvider.initSupportedAffWorkingTime();

    final ExternalOrderRequest expRequest = OrderedDataProvider.initExternalOrderRequest();
    final List<ShoppingCartItem> cartItems = OrderedDataProvider.initCartItems();

    final int outOfWorkingTime = 18;
    final String requestDateTime = LocalDateTime.now().withHour(outOfWorkingTime).withMinute(0)
        .format(DateTimeFormatter.ofPattern(DateUtils.SWISS_DATE_PATTERN_1));

    final ExternalOrderRequest actualRequest = SagBeanUtils.map(expRequest,
        ExternalOrderRequest.class);
    handler.handle(affiliate, actualRequest, cartItems, requestDateTime);

    final String expectedCustomerRefTxt = expRequest.getCustomerRefText() + SAMPLE_TEXT;
    final String expectedMessage = expRequest.getMessage();

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(actualRequest));

    Assert.assertThat(actualRequest.getMessage(), Matchers.is(StringUtils.trim(expectedMessage)));
    Assert.assertThat(actualRequest.getCustomerRefText(), Matchers.is(
        StringUtils.trim(expectedCustomerRefTxt)));
  }

  @Test
  public void shouldUpdateVinInfoTextToCustomerRefTextWithBeforeStartWorkingTime() {
    final SupportedAffiliate affiliate = OrderedDataProvider.initSupportedAffWorkingTime();

    final ExternalOrderRequest expRequest = OrderedDataProvider.initExternalOrderRequest();
    final List<ShoppingCartItem> cartItems = OrderedDataProvider.initCartItems();

    final int outOfWorkingTime = 4;
    final String requestDateTime = LocalDateTime.now().withHour(outOfWorkingTime).withMinute(0)
        .format(DateTimeFormatter.ofPattern(DateUtils.SWISS_DATE_PATTERN_1));

    final ExternalOrderRequest actualRequest = SagBeanUtils.map(expRequest,
        ExternalOrderRequest.class);
    handler.handle(affiliate, actualRequest, cartItems, requestDateTime);

    final String expectedCustomerRefTxt = expRequest.getCustomerRefText() + SAMPLE_TEXT;
    final String expectedMessage = expRequest.getMessage();

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(actualRequest));

    Assert.assertThat(actualRequest.getMessage(), Matchers.is(StringUtils.trim(expectedMessage)));
    Assert.assertThat(actualRequest.getCustomerRefText(), Matchers.is(
        StringUtils.trim(expectedCustomerRefTxt)));
  }

  @Test
  public void shouldUpdateVinInfoTextToMessage() {
    final SupportedAffiliate affiliate = OrderedDataProvider.initSupportedAffWorkingTime();

    final ExternalOrderRequest expRequest = OrderedDataProvider.initExternalOrderRequest();
    final List<ShoppingCartItem> cartItems = OrderedDataProvider.initCartItems();

    final int workingTime = 17;
    final String requestDateTime = LocalDateTime.now().withHour(workingTime).withMinute(0)
        .format(DateTimeFormatter.ofPattern(DateUtils.SWISS_DATE_PATTERN_1));

    final ExternalOrderRequest actualRequest = SagBeanUtils.map(expRequest,
        ExternalOrderRequest.class);
    handler.handle(affiliate, actualRequest, cartItems, requestDateTime);

    final String expectedCustomerRefTxt = expRequest.getCustomerRefText();
    final String expectedMessage = expRequest.getMessage() + SAMPLE_TEXT;

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(actualRequest));

    Assert.assertThat(actualRequest.getMessage(), Matchers.is(StringUtils.trim(expectedMessage)));
    Assert.assertThat(actualRequest.getCustomerRefText(), Matchers.is(
        StringUtils.trim(expectedCustomerRefTxt)));
  }

  @Test
  public void shouldUpdateNoVinInfoTextToMessage() {
    final SupportedAffiliate affiliate = OrderedDataProvider.initSupportedAffWorkingTime();

    final ExternalOrderRequest expRequest = OrderedDataProvider.initExternalOrderRequest();
    final List<ShoppingCartItem> cartItems =
        OrderedDataProvider.initCartItemsForServiceSchedulesCase();

    final int workingTime = 17;
    final String requestDateTime = LocalDateTime.now().withHour(workingTime).withMinute(0)
        .format(DateTimeFormatter.ofPattern(DateUtils.SWISS_DATE_PATTERN_1));

    final ExternalOrderRequest actualRequest = SagBeanUtils.map(expRequest,
        ExternalOrderRequest.class);
    handler.handle(affiliate, actualRequest, cartItems, requestDateTime);

    final String expectedCustomerRefTxt = expRequest.getCustomerRefText();
    final String expectedMessage = expRequest.getMessage() + " OHNE VIN";

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(actualRequest));

    Assert.assertThat(actualRequest.getMessage(), Matchers.is(StringUtils.trim(expectedMessage)));
    Assert.assertThat(actualRequest.getCustomerRefText(), Matchers.is(
        StringUtils.trim(expectedCustomerRefTxt)));
  }

  @Test
  public void shouldUpdateNoVinInfoTextToCustRefTxt() {
    final SupportedAffiliate affiliate = OrderedDataProvider.initSupportedAffWorkingTime();

    final ExternalOrderRequest expRequest = OrderedDataProvider.initExternalOrderRequest();
    final List<ShoppingCartItem> cartItems =
        OrderedDataProvider.initCartItemsForServiceSchedulesCase();

    final int workingTime = 4;
    final String requestDateTime = LocalDateTime.now().withHour(workingTime).withMinute(0)
        .format(DateTimeFormatter.ofPattern(DateUtils.SWISS_DATE_PATTERN_1));

    final ExternalOrderRequest actualRequest = SagBeanUtils.map(expRequest,
        ExternalOrderRequest.class);
    handler.handle(affiliate, actualRequest, cartItems, requestDateTime);

    final String expectedMessage = expRequest.getMessage();
    final String expectedCustomerRefTxt = expRequest.getCustomerRefText() + " OHNE VIN";

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(actualRequest));

    Assert.assertThat(actualRequest.getMessage(), Matchers.is(StringUtils.trim(expectedMessage)));
    Assert.assertThat(actualRequest.getCustomerRefText(), Matchers.is(
        StringUtils.trim(expectedCustomerRefTxt)));
  }
}
