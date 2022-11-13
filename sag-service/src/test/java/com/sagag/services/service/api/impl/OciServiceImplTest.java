package com.sagag.services.service.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.utils.OciHtmlBuilder;
import com.sagag.services.service.utils.UserDataProvider;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class OciServiceImplTest {

  @InjectMocks
  private OciServiceImpl ociService;

  @Mock
  private CartBusinessService cartBusinessService;

  @Mock
  private CustomerSettingsService customerSettingsService;

  @Mock
  private ContextService contextService;

  @Mock
  private OciHtmlBuilder ociHtmlBuilder;

  @Mock
  private NumberFormatterContext numberFormatter;

  @Test
  public void testExportOrder() throws Exception {
    UserInfo userInfo = UserDataProvider.buildOfferUserInfo();
    userInfo.setSettings(new OwnSettings());
    userInfo.setLanguage("DE");
    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
    shoppingCartItem.setArticle(new ArticleDocDto());
    ShoppingCart shoppingCart = new ShoppingCart(Arrays.asList(shoppingCartItem));
    when(cartBusinessService.checkoutShopCart(any(),
        Mockito.eq(ShopType.DEFAULT_SHOPPING_CART), Mockito.anyBoolean())).thenReturn(shoppingCart);

    EshopContext eshopContext = new EshopContext();
    eshopContext.setEshopBasketContext(new EshopBasketContext());
    when(contextService.getEshopContext(any())).thenReturn(eshopContext);
    when(customerSettingsService.findSettingsByOrgId(Mockito.anyInt()))
        .thenReturn(new CustomerSettings());

    when(ociHtmlBuilder.buildOciHtmlContent(any(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(null);

    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMANY);
    when(numberFormatter.getFormatterByAffiliateShortName(any())).thenReturn(formatter);

    ociService.exportOrder(userInfo, StringUtils.EMPTY, ShopType.DEFAULT_SHOPPING_CART);
    Mockito.verify(ociHtmlBuilder, Mockito.times(1)).buildOciHtmlContent(any(), Mockito.anyString(),
        Mockito.anyString());
  }

}
