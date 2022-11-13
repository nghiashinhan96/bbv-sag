package com.sagag.services.ax.client;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxBaseIT;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.domain.AxAddress;
import com.sagag.services.ax.domain.AxAddressResourceSupport;
import com.sagag.services.ax.domain.AxBranch;
import com.sagag.services.ax.domain.AxBranchesResourceSupport;
import com.sagag.services.ax.domain.AxCreditLimitInfo;
import com.sagag.services.ax.domain.AxCustomer;
import com.sagag.services.ax.domain.AxOrderConfirmation;
import com.sagag.services.ax.domain.AxOrderHistory;
import com.sagag.services.ax.domain.AxOrderPosition;
import com.sagag.services.ax.domain.AxOrderPositions;
import com.sagag.services.ax.domain.AxReleaseNoteResourceSupport;
import com.sagag.services.ax.request.AxOrderRequest;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;

/**
 * Class to verify AX API in real time.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
@Ignore("Save time for verify build, because the AX service often has problems for failed build")
public class AxClientIT extends AxBaseIT {

  @Autowired
  private AxClient axClient;

  private static String cachedToken;

  @Before
  public void init() {
    cachedToken = getAxToken(cachedToken);
  }

  @Test(expected = RestClientException.class)
  public void testGetCustomerByNrIsNotFound() {

    final String customerNr = "00000000";
    final String companyName = SupportedAffiliate.TECHNOMAG.getCompanyName();
    axClient.getCustomerByNr(cachedToken, companyName, customerNr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCustomerByNrWithEmptyCustomerNr() {

    final String customerNr = StringUtils.EMPTY;
    final String companyName = SupportedAffiliate.TECHNOMAG.getCompanyName();
    axClient.getCustomerByNr(cachedToken, companyName, customerNr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCustomerByNrWithEmptyCompanyName() {

    final String customerNr = "1130436";
    final String companyName = StringUtils.EMPTY;
    axClient.getCustomerByNr(cachedToken, companyName, customerNr);
  }

  @Test
  public void testGetCustomerByNr() {

    ResponseEntity<AxCustomer> customerRes = axClient.getCustomerByNr(
        cachedToken, AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.customerNr());

    assertOkHttpStatus(customerRes);
    Assert.assertThat(customerRes.getBody().getNr(), Matchers.equalTo(AxDataTestUtils.customerNr()));
  }

  @Test
  public void testGetAddressesOfCustomer() {

    ResponseEntity<AxAddressResourceSupport> addressRes =
        axClient.getAddressesOfCustomer(cachedToken, AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.customerNr());

    assertOkHttpStatus(addressRes);
    Assert.assertThat(addressRes.getBody().getAddresses(), Matchers.not(Matchers.equalTo(Matchers.empty())));
  }

  @Test
  public void testGetAddressesOfCustomerByHrefLink() throws IOException {
    final String relativeUrl = "/webshop-service/customers/Derendinger-Austria/8301102/addresses";
    ResponseEntity<AxAddressResourceSupport> jsonRes =
        axClient.getAddressesOfCustomerByHrefLink(cachedToken, relativeUrl);

    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetAddressById() {

    ResponseEntity<AxAddress> addressRes =
      axClient.getAddressById(cachedToken, AxDataTestUtils.companyNameOfDDAT(),
        AxDataTestUtils.customerNr(), AxDataTestUtils.addressId());

    assertOkHttpStatus(addressRes);
  }

  @Test
  public void testGetAddressByIdByHrefLink() {

    ResponseEntity<AxAddress> addressRes =
      axClient.getAddressByHrefLink(cachedToken, AxDataTestUtils.addressDetailUrl());

    assertOkHttpStatus(addressRes);
  }

  @Test
  public void testCreateShoppingBasket() {
    AxOrderRequest axOrderRequest = AxDataTestUtils.orderRequest();
    ResponseEntity<AxOrderConfirmation> jsonRes =
        axClient.createShoppingBasket(cachedToken, AxDataTestUtils.companyNameOfDDAT(), axOrderRequest);
    assertSuccessfulHttpStatus(jsonRes);
  }

  @Test
  public void testCreateShoppingBasketWithOrderType() {
    AxOrderRequest axOrderRequest = AxDataTestUtils.orderRequest();
    axOrderRequest.setSendMethodCode("ABH");
    axOrderRequest.setOrderType("TH1");
    ResponseEntity<AxOrderConfirmation> jsonRes = axClient.createShoppingBasket(cachedToken, AxDataTestUtils.companyNameOfDDAT(), axOrderRequest);
    assertSuccessfulHttpStatus(jsonRes);
  }

  @Test
  public void testCreateOrder() {
    AxOrderRequest axOrderRequest = AxDataTestUtils.orderRequest();

    ResponseEntity<AxOrderConfirmation> jsonRes =
        axClient.createOrder(cachedToken, AxDataTestUtils.companyNameOfDDAT(), axOrderRequest);

    assertSuccessfulHttpStatus(jsonRes);
  }

  @Test
  public void testGetOrdersOfCustomer() {
    ResponseEntity<AxOrderHistory> orderHistoryRes = axClient.getOrdersOfCustomer(
        cachedToken, AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.customerNr(),
        AxDataTestUtils.orderNr(), AxDataTestUtils.fromDate(), AxDataTestUtils.toDate(), 1);
    assertOkHttpStatus(orderHistoryRes);
  }

  @Test
  public void testGetOrdersOfCustomerByHrefLink() {
    ResponseEntity<AxOrderHistory> orderHistoryRes =
        axClient.getOrdersOfCustomerByHrefLink(cachedToken, AxDataTestUtils.ordersLink());
    assertOkHttpStatus(orderHistoryRes);
  }

  @Test
  public void testGetOrdersOfCustomerByHrefLinkFilter() {
    StringBuilder orderUrlBuilder = new StringBuilder(AxDataTestUtils.ordersLink())
      .append("?dateFrom=").append(AxDataTestUtils.fromDate())
      .append("&dateTo=").append(AxDataTestUtils.toDate());
    ResponseEntity<AxOrderHistory> orderHistoryRes =
        axClient.getOrdersOfCustomerByHrefLink(cachedToken, orderUrlBuilder.toString());
    assertOkHttpStatus(orderHistoryRes);
  }

  @Test
  public void testGetOrderPositions() {
    ResponseEntity<AxOrderPositions> jsonRes =
        axClient.getOrderPositions(cachedToken, AxDataTestUtils.companyNameOfDDAT(),
        AxDataTestUtils.customerNr(), AxDataTestUtils.orderNr());
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetOrderPosition() {
    ResponseEntity<AxOrderPosition> jsonRes =
        axClient.getOrderPosition(cachedToken, AxDataTestUtils.companyNameOfDDAT(),
        AxDataTestUtils.customerNr(), AxDataTestUtils.orderNr(), 1);
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetBranches() {
    ResponseEntity<AxBranchesResourceSupport> jsonRes =
        axClient.getBranches(cachedToken, AxDataTestUtils.companyNameOfDDAT());
    assertOkHttpStatus(jsonRes);
  }

  /**
   * <p>
   * The API to get release note info from AX.
   * </p>
   *
   * <pre>
   * Helpful to verify new release from AX connection with all environments
   * </pre>
   *
   */
  @Test
  public void testGetReleaseInfo() {
    ResponseEntity<AxReleaseNoteResourceSupport> jsonRes = axClient.getReleaseInfo();
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetBranchById() {
    ResponseEntity<AxBranch> branchRes = axClient.getBranchById(cachedToken,
        AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.defaultBranchId());
    assertOkHttpStatus(branchRes);

    Assert.assertThat(branchRes.getBody().getBranchId(),
        Matchers.equalTo(AxDataTestUtils.defaultBranchId()));
  }

  @Test
  public void testGetCustomerByNr_MatikAt() {

    ResponseEntity<AxCustomer> customerRes = axClient.getCustomerByNr(
        cachedToken, AxDataTestUtils.companyNameOfMAT(),
        AxDataTestUtils.CUSTOMER_NR_MATIK_AT_8000016);

    assertOkHttpStatus(customerRes);
    Assert.assertThat(customerRes.getBody().getNr(),
        Matchers.equalTo(AxDataTestUtils.CUSTOMER_NR_MATIK_AT_8000016));
  }

  @Test
  public void testGetAddressesOfCustomerByHrefLink_MatikAt() throws IOException {
    final String relativeUrl = "/webshop-service/customers/Matik-Austria/8000016/addresses";
    ResponseEntity<AxAddressResourceSupport> jsonRes =
        axClient.getAddressesOfCustomerByHrefLink(cachedToken, relativeUrl);

    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void givenCustNr_shouldGetCreditLimit() {
    ResponseEntity<AxCreditLimitInfo> res =
        axClient.getCreditLimitByCustomerNr(cachedToken, AxDataTestUtils.companyNameOfDDAT(),
            "1110914");
    assertOkHttpStatus(res);
  }

}
