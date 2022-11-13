package com.sagag.services.service.order.steps;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.steps.afterorder.FinalUserAfterOrderHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FinalUserAfterOrderHandlerTest {

  @InjectMocks
  private FinalUserAfterOrderHandler finalUserAfterOrderHandler;

  @Mock
  protected CartBusinessService cartBusinessService;

  @Mock
  private FinalCustomerOrderService finalCustomerOrderService;

  @Mock
  private OrganisationService organisationService;

  @Mock
  private VinOrderCacheService vinOrderCacheService;

  @Test
  public void handle_shouldSuccess_givenFinalUserAndShoppingCart() {
    final UserInfo user = DataProvider.createUserInfo();
    user.setCompanyName(SupportedAffiliate.WBB.getCompanyName());
    AfterOrderCriteria afterOrderCriteria =
        AfterOrderCriteria.builder()
        .shoppingCart(new ShoppingCart(Lists.newArrayList()))
        .shopType(ShopType.DEFAULT_SHOPPING_CART)
        .build();
    Mockito.when(organisationService.getFirstByUserId(user.getId()))
        .thenReturn(Optional.of(new Organisation()));
    Mockito.doNothing().when(cartBusinessService).clear(user, ShopType.DEFAULT_SHOPPING_CART);
    Mockito.doNothing().when(finalCustomerOrderService).save(Mockito.any(FinalCustomerOrder.class));
    Mockito.when(vinOrderCacheService.getSearchCount(user.key())).thenReturn(null);
    finalUserAfterOrderHandler.handle(user, null, afterOrderCriteria);
    Mockito.verify(cartBusinessService, Mockito.times(1)).clear(user, ShopType.DEFAULT_SHOPPING_CART);
  }

}
