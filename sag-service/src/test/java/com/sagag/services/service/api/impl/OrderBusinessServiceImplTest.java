package com.sagag.services.service.api.impl;

import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.api.order.VCustomerOrderHistoryRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.BranchService;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.request.OrderStatusRequest;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.domain.order.OrderDetailDto;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.exception.OrderStatusException;
import com.sagag.services.service.order.OrderHandlerContextV2;
import com.sagag.services.service.order.history.OrderHistoryHandler;
import com.sagag.services.service.order.history.OrderHistoryHelper;
import com.sagag.services.service.order.ordercondition.OrderConditionInitializer;
import com.sagag.services.service.request.order.OrderHistoryDetailRequestBody;
import com.sagag.services.service.request.order.OrderStatusRequestBody;

import lombok.Getter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * UT for {@link OrderBusinessServiceImpl}.
 */
@EshopMockitoJUnitRunner
public class OrderBusinessServiceImplTest {

  private static final String AT_CUSTOMER_NUMBER = "1100005";

  @InjectMocks
  private OrderBusinessServiceImpl orderBusinessServiceImpl;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private UserService userService;

  @Mock
  private OrderHistoryService orderHistoryService;

  @Mock
  private OrderExternalService orderExternalService;

  @Mock
  private OrganisationService orgService;

  @Mock
  private Map<String, OrderHistory> orderMap;

  @Mock
  private OrderHistory orderHistory;

  @Mock
  private OrderHistoryHelper orderHistoryHelper;

  private ExternalOrderHistory externalOrderHistory;

  @Mock
  private EshopUser eshopUser;

  @Mock
  @Getter
  private OAuth2Authentication oAuth2Authentication;

  @Mock
  private Authentication authentication;

  @Mock
  @Getter
  private UserInfo user;

  @Mock
  private EmployeeExternalService employeeExtService;

  @Mock
  private OrderHandlerContextV2 orderHandlerContext;

  @Mock
  private UserBusinessService userBusinessService;

  @Mock
  private InvoiceTypeRepository invoiceTypeRepo;

  @Mock
  private BranchService branchSearchService;

  @Mock
  private AddressFilterService addressFilterService;

  @Mock
  private VCustomerOrderHistoryRepository vCustomerOrderHistoryRepo;

  @Mock
  private OrderConditionInitializer orderConditionInitializer;

  @Mock
  private OrderHistoryHandler orderHistoryHandler;

  /**
   * Sets up the pre-condition for testing.
   *
   * @throws Exception throws when program fails.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(oAuth2Authentication.getUserAuthentication()).thenReturn(authentication);
    when(authentication.getDetails()).thenReturn(user);
    when(user.getCustomer()).thenReturn(new Customer());
  }

  @Test
  public void testViewOrderHistoryDetailSuccessfully() throws ResultNotFoundException {
    orderHistory = new OrderHistory();
    when(orderHistoryService.getOrderDetail(Mockito.any()))
        .thenReturn(Optional.ofNullable(orderHistory));

    externalOrderHistory = new ExternalOrderHistory();

    when(orderExternalService.getOrderDetailByOrderNr(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(Optional.ofNullable(externalOrderHistory));
    OrderHistoryDetailRequestBody body = new OrderHistoryDetailRequestBody();
    body.setAffiliate(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    body.setOrderId(1L);
    body.setOrderNumber("AUF00001122");
    body.setCustomerNr(AT_CUSTOMER_NUMBER);
    OrderDetailDto orderDetail = orderBusinessServiceImpl.getOrderDetail(getUser(), body);
    Assert.assertNotNull(orderDetail);
  }

  @Test
  public void testViewOrderHistoryDetailForSalesNotOnBehalf() throws ResultNotFoundException {
    orderHistory = new OrderHistory();
    when(orderHistoryService.getOrderDetail(Mockito.any()))
        .thenReturn(Optional.ofNullable(orderHistory));

    externalOrderHistory = new ExternalOrderHistory();

    when(orderExternalService.getOrderDetailByOrderNr(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(Optional.ofNullable(externalOrderHistory));
    OrderHistoryDetailRequestBody body = new OrderHistoryDetailRequestBody();
    body.setAffiliate(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    body.setOrderNumber("AUF00001122");
    body.setCustomerNr(AT_CUSTOMER_NUMBER);
    body.setOrderId(1L);
    UserInfo userInfo = getUser();
    userInfo.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    OrderDetailDto orderDetail = orderBusinessServiceImpl.getOrderDetail(userInfo, body);
    Assert.assertNotNull(orderDetail);
  }

  @Test(expected = OrderStatusException.class)
  public void givenChangeOrderRequestShouldBeCanNotSucceed() throws OrderStatusException {
    Mockito.when(orderExternalService.updateSalesOrderStatus(Mockito.anyString(),
        Mockito.any(OrderStatusRequest.class))).thenReturn(false);

    OrderStatusRequestBody body = new OrderStatusRequestBody();
    body.setOrderHistoryId(1L);
    body.setOrderNr("AU001");

    orderBusinessServiceImpl
        .updateSalesOrderStatus(SupportedAffiliate.DERENDINGER_AT.getCompanyName(), body);

    Mockito.verify(orderExternalService, Mockito.times(1))
        .updateSalesOrderStatus(Mockito.anyString(), Mockito.any(OrderStatusRequest.class));
  }

  @Test(expected = OrderStatusException.class)
  public void givenChangeOrderRequestShouldBeCanNotSucceedAxThrownException()
      throws OrderStatusException {
    // Request
    final long orderId = 1L;
    OrderStatusRequestBody body = new OrderStatusRequestBody();
    body.setOrderHistoryId(orderId);
    body.setOrderNr("AU001");

    Mockito.when(orderExternalService.updateSalesOrderStatus(Mockito.anyString(),
        Mockito.any(OrderStatusRequest.class))).thenThrow(new RestClientException("has error"));

    orderBusinessServiceImpl
        .updateSalesOrderStatus(SupportedAffiliate.DERENDINGER_AT.getCompanyName(), body);

    Mockito.verify(orderExternalService, Mockito.times(1))
        .updateSalesOrderStatus(Mockito.anyString(), Mockito.any(OrderStatusRequest.class));
  }

  @Test
  public void givenChangeOrderRequestShouldBeSucceed() throws OrderStatusException {
    Mockito.when(orderExternalService.updateSalesOrderStatus(Mockito.anyString(),
        Mockito.any(OrderStatusRequest.class))).thenReturn(true);

    OrderStatusRequestBody body = new OrderStatusRequestBody();
    body.setOrderHistoryId(1L);
    body.setOrderNr("AU0012");

    orderBusinessServiceImpl
        .updateSalesOrderStatus(SupportedAffiliate.DERENDINGER_AT.getCompanyName(), body);

    Mockito.verify(orderExternalService, Mockito.times(1))
        .updateSalesOrderStatus(Mockito.anyString(), Mockito.any(OrderStatusRequest.class));
  }

}
