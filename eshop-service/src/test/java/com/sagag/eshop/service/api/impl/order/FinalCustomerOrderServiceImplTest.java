package com.sagag.eshop.service.api.impl.order;

import com.sagag.eshop.repo.api.order.FinalCustomerOrderItemRepository;
import com.sagag.eshop.repo.api.order.FinalCustomerOrderRepository;
import com.sagag.eshop.repo.api.order.VFinalCustomerOrderStatusRepository;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.VFinalCustomerOrderStatus;
import com.sagag.eshop.service.api.impl.FinalCustomerOrderServiceImpl;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderReferenceDto;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FinalCustomerOrderServiceImplTest {

  @InjectMocks
  private FinalCustomerOrderServiceImpl finalCustomerOrderyService;

  @Mock
  private VFinalCustomerOrderStatusRepository vFinalCustomerOrderStatusRepo;

  @Mock
  private FinalCustomerOrderRepository finalCustomerOrderRepo;

  @Mock
  private FinalCustomerOrderItemRepository finalCustomerOrderItemRepo;

  @Test
  public void getOrderDashboardByOrgCode_shouldReturnResults_givenOrgCode() {
    final Long orgCode = 5132364L;
    Mockito.when(vFinalCustomerOrderStatusRepo.findOrderStatusByOrgCode(orgCode))
        .thenReturn(new ArrayList<VFinalCustomerOrderStatus>());
    OrderDashboardDto orderDashboardDto =
        finalCustomerOrderyService.getOrderDashboardByOrgCode(String.valueOf(orgCode));
    Assert.assertThat(orderDashboardDto, Matchers.notNullValue());
  }

  @Test
  public void getFinalCustomerOrderDetail_shouldReturnResults_givenFinalCustomerOrderId() {
    final Long finalCustomerOrderId = 1L;

    Mockito.when(finalCustomerOrderRepo.findById(finalCustomerOrderId))
        .thenReturn(mockFinalCustomerOrder());
    Mockito.when(finalCustomerOrderItemRepo.findByFinalCustomerOrderIds(Arrays.asList(1L)))
        .thenReturn(new ArrayList<>());
    Optional<FinalCustomerOrderDto> finalCustomerOrderDto =
        finalCustomerOrderyService.getFinalCustomerOrderDetail(finalCustomerOrderId);
    Assert.assertTrue(finalCustomerOrderDto.isPresent());
  }

  @Test
  public void getFinalCustomerOrderReference_shouldReturnResults_givenFinalCustomerOrderId() {
    final Long finalCustomerOrderId = 1L;

    Mockito.when(finalCustomerOrderRepo.findById(finalCustomerOrderId))
        .thenReturn(mockFinalCustomerOrder());
    Mockito.when(finalCustomerOrderItemRepo.findByFinalCustomerOrderIds(Arrays.asList(1L)))
        .thenReturn(new ArrayList<>());
    Optional<FinalCustomerOrderReferenceDto> finalCustomerOrderDto =
        finalCustomerOrderyService.getFinalCustomerOrderReference(finalCustomerOrderId);
    Assert.assertTrue(finalCustomerOrderDto.isPresent());
  }

  private Optional<FinalCustomerOrder> mockFinalCustomerOrder() {
    return Optional
        .of(FinalCustomerOrder.builder().id(1L).date(Calendar.getInstance().getTime()).build());
  }

}
