package com.sagag.services.service.order.steps;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.CouponService;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.order.EshopOrderHistoryState;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.processor.ax.TransferBasketProcessor;
import com.sagag.services.service.order.steps.afterorder.EshopUserAfterOrderHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EshopUserAfterOrderHandlerTest {

  private static final long ORDER_HISTORY = 1L;

  @InjectMocks
  private EshopUserAfterOrderHandler eshoplUserAfterOrderHandler;

  @Mock
  protected CartBusinessService cartBusinessService;

  @Mock
  private VinOrderCacheService vinOrderCacheService;

  @Mock
  private LicenseService licenseService;

  @Mock
  private CouponService couponService;

  @Mock
  private CouponCacheService couponCacheService;

  @Mock
  protected OrderHistoryRepository orderHistoryRepo;

  @Mock
  private TransferBasketProcessor processor;

  @Test
  public void handle_shouldSuccess_givenUserAndShoppingCart() {
    final UserInfo user = DataProvider.createUserInfo();
    user.setCompanyName(SupportedAffiliate.WBB.getCompanyName());
    final OrderHistory orderHistory = initOrderHistory();
    AfterOrderCriteria afterOrderCriteria =
        AfterOrderCriteria.builder().shoppingCart(new ShoppingCart(Lists.newArrayList()))
            .shopType(ShopType.DEFAULT_SHOPPING_CART).orderHistory(orderHistory).build();

    Mockito.doNothing().when(cartBusinessService).clear(user, ShopType.DEFAULT_SHOPPING_CART);
    Mockito.doNothing().when(couponCacheService).clearCache(Mockito.any(String.class));
    Mockito.when(orderHistoryRepo.save(Mockito.any(OrderHistory.class))).thenReturn(orderHistory);
    Mockito.when(vinOrderCacheService.getSearchCount(user.key())).thenReturn(null);
    Mockito.when(licenseService.getLicenseSettingsByArticleId(Mockito.any()))
        .thenReturn(Optional.empty());
    eshoplUserAfterOrderHandler.handle(user, processor, afterOrderCriteria);
    Mockito.verify(processor, Mockito.times(1)).updateOrderHistory(orderHistory,
        afterOrderCriteria.getAxResult(), EshopOrderHistoryState.ORDERED, user.getCustNrStr());
  }

  private OrderHistory initOrderHistory() {
    final OrderHistory orderHistory = OrderHistory.builder().id(ORDER_HISTORY).build();
    return orderHistory;
  }

}
