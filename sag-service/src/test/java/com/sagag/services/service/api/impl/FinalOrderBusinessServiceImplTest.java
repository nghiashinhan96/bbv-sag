package com.sagag.services.service.api.impl;

import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.order.FinalCustomerOrderItemRepository;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.request.FinalCustomerOrderSearchRequest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

/**
 * UT class for FinalOrderBusinessServiceImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class FinalOrderBusinessServiceImplTest {

  @InjectMocks
  private FinalOrderBusinessServiceImpl finalOrderBusinessService;

  @Mock
  private FinalCustomerOrderService finalCustomerOrderService;

  @Mock
  private OrganisationRepository organisationRepo;

  @Mock
  private FinalCustomerOrderItemRepository finalCustomerOrderItemRepo;

  @Test
  public void getOrderDashBoardOverview_shouldReturnResult_givenUserInfo() {
    final Long orgCode = 5132364L;
    UserInfo userInfo = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().nr(orgCode).build());
    userInfo.setSettings(ownSettings);
    userInfo.setRoles(Arrays.asList(EshopAuthority.FINAL_USER_ADMIN.name()));
    when(finalCustomerOrderService.getOrderDashboardByOrgCode(userInfo.getCustNrStr()))
        .thenReturn(OrderDashboardDto.builder().build());
    OrderDashboardDto result = finalOrderBusinessService.getOrderDashBoardOverview(userInfo);
    Assert.assertNotNull(result);
  }

  @Test
  public void searchFinalCustomerOrder_shouldReturnResult_givenRequestBody() {
    final Long orgCode = 5132364L;
    UserInfo userInfo = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().nr(orgCode).build());
    userInfo.setSettings(ownSettings);
    userInfo.setFinalCustomer(OrganisationDto.builder().build());
    userInfo.setRoles(Arrays.asList(EshopAuthority.FINAL_USER_ADMIN.name()));

    Page<VFinalCustomerOrderDto> page = new PageImpl<>(Arrays.asList(new VFinalCustomerOrderDto()));
    when(finalCustomerOrderService.searchVFinalCustomerOrderByCriteria(Mockito.any()))
        .thenReturn(page);

    FinalCustomerOrderSearchRequest body = FinalCustomerOrderSearchRequest.builder().build();
    Page<VFinalCustomerOrderDto> result =
        finalOrderBusinessService.searchFinalCustomerOrder(body, userInfo);
    Assert.assertNotNull(result);
  }

  @Test
  public void searchFinalCustomerOrder_shouldReturnEmpty_givenRequestBody() {
    final Long orgCode = 5132364L;
    UserInfo userInfo = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().nr(orgCode).build());
    userInfo.setSettings(ownSettings);
    userInfo.setFinalCustomer(OrganisationDto.builder().build());
    userInfo.setRoles(Arrays.asList(EshopAuthority.FINAL_USER_ADMIN.name()));

    Page<VFinalCustomerOrderDto> page = Page.empty();
    when(finalCustomerOrderService.searchVFinalCustomerOrderByCriteria(Mockito.any()))
        .thenReturn(page);

    FinalCustomerOrderSearchRequest body = FinalCustomerOrderSearchRequest.builder().build();
    Page<VFinalCustomerOrderDto> result =
        finalOrderBusinessService.searchFinalCustomerOrder(body, userInfo);
    Assert.assertNotNull(result);
    Assert.assertThat(result.getContent(), Matchers.empty());
  }

  @Test
  public void searchFinalCustomerOrder_shouldReturnResult_givenRequestBodyWholeSaler() {
    final Long orgCode = 5132364L;
    UserInfo userInfo = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().nr(orgCode).build());
    userInfo.setSettings(ownSettings);
    userInfo.setFinalCustomer(OrganisationDto.builder().build());
    userInfo.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    userInfo.setPermissions(Arrays.asList(PermissionDto.builder().permission(PermissionEnum.WHOLESALER.name()).build()));

    when(organisationRepo.findFinalOrgIdByOrgCode(orgCode.toString()))
        .thenReturn(Arrays.asList(139L));
    Page<VFinalCustomerOrderDto> page = new PageImpl<>(Arrays.asList(new VFinalCustomerOrderDto()));
    when(finalCustomerOrderService.searchVFinalCustomerOrderByCriteria(Mockito.any()))
        .thenReturn(page);

    FinalCustomerOrderSearchRequest body = FinalCustomerOrderSearchRequest.builder().build();
    Page<VFinalCustomerOrderDto> result =
        finalOrderBusinessService.searchFinalCustomerOrder(body, userInfo);
    Assert.assertNotNull(result);
  }

}
