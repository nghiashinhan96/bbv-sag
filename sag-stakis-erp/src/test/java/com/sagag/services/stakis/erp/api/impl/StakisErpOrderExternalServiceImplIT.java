package com.sagag.services.stakis.erp.api.impl;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.StakisErpApplication;
import com.sagag.services.stakis.erp.domain.TmSendOrderExternalRequest;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StakisErpApplication.class)
@EshopIntegrationTest
@CzProfile
@Slf4j
public class StakisErpOrderExternalServiceImplIT {

	@Autowired
	private StakisErpOrderExternalServiceImpl service;

	@Test
	public void testApi() throws ServiceException {
		final TmSendOrderExternalRequest request = DataProvider.buildExternalOrderRequest();
		final TmUserCredentials credentials = DataProvider.tmUserCredentials();
		request.setCustomerRefText("test");
		OrderConfirmation orderConfirmation = sendOrder(credentials, request);
		Assert.assertNotNull(orderConfirmation);
		Assert.assertNotNull(orderConfirmation.getOrderNr());
	}

	@Test
	public void getOrderHistoryByOrderNumber() throws ServiceException {
		final TmUserCredentials credentials = DataProvider.tmUserCredentials();
		final TmSendOrderExternalRequest request = DataProvider.buildExternalOrderRequest();
		request.setCustomerRefText("test");
		OrderConfirmation orderConfirmation = sendOrder(credentials, request);
		Optional<ExternalOrderHistory> orderHistory = service.getExternalOrderHistoryOfCustomer(
			SupportedAffiliate.STAKIS_CZECH.getCompanyName(), credentials.getCustomerId(),
			ExternalOrderHistoryRequest.builder().orderNumber(orderConfirmation.getOrderNr()).build(), 1);

		Assert.assertNotNull(orderHistory);
		Assert.assertNotNull(orderHistory.get().getOrders());
		Assert.assertThat(orderHistory.get().getOrders().size(), Matchers.is(1));
		Assert.assertNotNull(orderHistory.get().getOrders().get(0).getDate());
		Assert.assertEquals(orderConfirmation.getOrderNr(),
			orderHistory.get().getOrders().get(0).getNr());
		Assert.assertThat(orderHistory.get().getOrders().get(0).getSendMethodDesc(),
			Matchers.is("ROZVOZ STAHLGRUBER"));

	}

	@Test
	public void getOrderHistoryByArticleId() throws ServiceException {

		final TmUserCredentials credentials = DataProvider.tmUserCredentials();

		final TmSendOrderExternalRequest request = DataProvider.buildExternalOrderRequest();
		request.setCustomerRefText("test");

		final OrderConfirmation orderConfirmation = service.sendOrder(credentials.getUsername(),
			credentials.getCustomerId(), credentials.getPassword(), credentials.getLang(),
			request, null);

		Optional<ExternalOrderHistory> orderHistory = service.getExternalOrderHistoryOfCustomer(
			SupportedAffiliate.STAKIS_CZECH.getCompanyName(), credentials.getCustomerId(),
			ExternalOrderHistoryRequest.builder().articleNumber(DataProvider.TEST_ARTICLE_NUMBER).build(), 1);

		Assert.assertNotNull(orderHistory);
		Assert.assertNotNull(orderHistory.get().getOrders());
		Assert.assertThat(orderHistory.get().getOrders().size(), Matchers.greaterThan(1));
		Assert.assertNotNull(orderHistory.get().getOrders().get(0).getDate());
		Assert.assertEquals(orderConfirmation.getOrderNr(),
			orderHistory.get().getOrders().get(0).getNr());
		Assert.assertThat(orderHistory.get().getOrders().get(0).getSendMethodDesc(),
			Matchers.is("ROZVOZ STAHLGRUBER"));

	}

	@Test
	public void getOrderHistoryByOrderNumber_notFoundOrderNumber() throws ServiceException {

		final TmUserCredentials credentials = DataProvider.tmUserCredentials();

		String orderNumber = "fake_order_number_12345678";
		Optional<ExternalOrderHistory> orderHistory = service.getExternalOrderHistoryOfCustomer(
			SupportedAffiliate.STAKIS_CZECH.getCompanyName(), credentials.getCustomerId(),
			ExternalOrderHistoryRequest.builder().orderNumber(orderNumber).build(), 1);
		Assert.assertNotNull(orderHistory);
		Assert.assertNotNull(orderHistory.get().getOrders());
		Assert.assertThat(orderHistory.get().getOrders().size(), Matchers.is(0));
	}

	@Test
	public void getOrderHistoryByArticle_notFoundArticle() throws ServiceException {
		final TmUserCredentials credentials = DataProvider.tmUserCredentials();

		String fakeArticle = "fake_article_12345678";
		Optional<ExternalOrderHistory> orderHistory = service.getExternalOrderHistoryOfCustomer(
			SupportedAffiliate.STAKIS_CZECH.getCompanyName(), credentials.getCustomerId(),
			ExternalOrderHistoryRequest.builder().articleNumber(fakeArticle).build(), 1);
		Assert.assertNotNull(orderHistory);
		Assert.assertNotNull(orderHistory.get().getOrders());
		Assert.assertThat(orderHistory.get().getOrders().size(), Matchers.is(0));
	}

	@Test
	public void getOrderHistoryByOrderNumber_withPickupSendMethod() throws ServiceException {
		final TmUserCredentials credentials = DataProvider.tmUserCredentials();

		final TmSendOrderExternalRequest request = DataProvider.buildExternalOrderRequest();
		String sendMethodCode = "PICKUP";
		request.setSendMethodCode(sendMethodCode);
		request.setCustomerRefText("test");

		final OrderConfirmation orderConfirmation = sendOrder(credentials, request);

		Optional<ExternalOrderHistory> orderHistory = service.getExternalOrderHistoryOfCustomer(
			SupportedAffiliate.STAKIS_CZECH.getCompanyName(), credentials.getCustomerId(),
			ExternalOrderHistoryRequest.builder().orderNumber(orderConfirmation.getOrderNr()).build(), 1);

		Assert.assertNotNull(orderHistory);
		Assert.assertNotNull(orderHistory.get().getOrders());
		Assert.assertThat(orderHistory.get().getOrders().size(), Matchers.is(1));
		Assert.assertNotNull(orderHistory.get().getOrders().get(0).getDate());
		Assert.assertEquals(orderConfirmation.getOrderNr(),
			orderHistory.get().getOrders().get(0).getNr());
		Assert.assertThat(orderHistory.get().getOrders().get(0).getSendMethodDesc(),
			Matchers.is("OSOBNÍ ODBĚR NA POBOČCE"));

	}

	@Test
	public void getExternalOrderPositionHistory() throws ServiceException {

		final TmUserCredentials credentials = DataProvider.tmUserCredentials();

		final TmSendOrderExternalRequest request = DataProvider.buildExternalOrderRequest();
		request.setCustomerRefText("test");

		final OrderConfirmation orderConfirmation = sendOrder(credentials, request);

		Optional<ExternalOrderPositions> orderPosition = service.getExternalOrderPosistion(
			orderConfirmation.getOrderNr(), SupportedAffiliate.STAKIS_CZECH.getCompanyName(),
			credentials.getCustomerId());

		Assert.assertNotNull(orderPosition);
		Assert.assertNotNull(orderPosition.get().getPositions());
		Assert.assertThat(orderPosition.get().getPositions().size(), Matchers.is(1));

	}

	@Test
	public void getExternalOrderPositionHistory_notFound() throws ServiceException {
		String fakeOrderNumber = "fake_order_number_12345678";

		final TmUserCredentials credentials = DataProvider.tmUserCredentials();

		Optional<ExternalOrderPositions> orderPosition = service.getExternalOrderPosistion(
			fakeOrderNumber, SupportedAffiliate.STAKIS_CZECH.getCompanyName(),
			credentials.getCustomerId());

		Assert.assertNotNull(orderPosition);
		Assert.assertNotNull(orderPosition.get().getPositions());
		Assert.assertThat(orderPosition.get().getPositions().size(), Matchers.is(0));

	}

	private OrderConfirmation sendOrder(final TmUserCredentials credentials,
		TmSendOrderExternalRequest request) throws ServiceException {
		final OrderConfirmation orderConfirmation = service.sendOrder(credentials.getUsername(),
			credentials.getCustomerId(), credentials.getPassword(), credentials.getLang(),
			request, null);

		log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(orderConfirmation));
		return orderConfirmation;
		}

}
