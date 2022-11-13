package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.order.VOrderHistoryRepository;
import com.sagag.eshop.repo.criteria.OrderHistorySearchCriteria;
import com.sagag.eshop.repo.entity.order.VOrderHistory;
import com.sagag.eshop.service.dto.order.SaleOrderHistoryDto;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.utils.PageUtils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Optional;

/**
 * UT to verify for {@link OrderHistoryServiceImpl}.
 */
@EshopMockitoJUnitRunner
public class OrderHistoryServiceImplTest {

  @InjectMocks
  private OrderHistoryServiceImpl orderHistoryServiceImpl;

  @Mock
  private OrganisationRepository organisationRepo;

  @Mock
  private VOrderHistoryRepository vOrderHistoryRepo;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetSaleOrders() {

    Mockito.when(organisationRepo.findIdByShortName(Mockito.anyString()))
        .thenReturn(Optional.of(2));

    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageUtils.defaultPageable(3);
    VOrderHistory vOrderHistory = new VOrderHistory();
    vOrderHistory.setType(OrderType.ORDER.name());
    PageImpl<VOrderHistory> page = new PageImpl<>(Arrays.asList(vOrderHistory));
    Mockito.when(
        vOrderHistoryRepo.findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class)))
        .thenReturn(page);
    Page<SaleOrderHistoryDto> result = orderHistoryServiceImpl.getSaleOrders(criteria, pageRequest);
    Assert.assertNotNull(result.getContent());
    Assert.assertThat(result.getContent().size(), Matchers.greaterThan(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetSaleOrdersNoAffiliate() {

    Mockito.when(
        vOrderHistoryRepo.findAll(Mockito.any(Specification.class), Mockito.any(PageRequest.class)))
        .thenReturn(Page.empty());
    OrderHistorySearchCriteria criteria = new OrderHistorySearchCriteria();
    PageRequest pageRequest = PageUtils.defaultPageable(3);
    Page<SaleOrderHistoryDto> result = orderHistoryServiceImpl.getSaleOrders(criteria, pageRequest);
    Assert.assertNotNull(result.getContent());
    Assert.assertThat(result.getContent().size(), Matchers.equalTo(0));
  }

}
