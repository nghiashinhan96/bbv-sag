package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.api.CartBusinessService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

@EshopMockitoJUnitRunner
public class VinPackageServiceImplTest {

  @InjectMocks
  private VinPackageServiceImpl vinPackageService;

  @Mock
  private CartManagerService cartManagerService;

  @Mock
  private CartBusinessService cartBusinessService;

  @Mock
  private IvdsArticleService ivdsArticleService;

  @Mock
  private LicenseService licenseService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSearchVinPackagesSuccess() {

    Mockito.when(licenseService.getAllVinLicenses()).thenReturn(new ArrayList<>());

    vinPackageService.searchVinPackages();

    Mockito.verify(licenseService, Mockito.times(1)).getAllVinLicenses();
  }

  @Test
  public void testSearchAvailableVinCallsSuccess() {

    Mockito.when(licenseService.getVinCallsLeft(DataProvider.CUSTOMER_NR)).thenReturn(1);

    vinPackageService.searchAvailableVinCalls(DataProvider.CUSTOMER_NR);

    Mockito.verify(licenseService, Mockito.times(1)).getVinCallsLeft(DataProvider.CUSTOMER_NR);
  }

  @Test
  public void testAddVinLicense_AlreadyExisted() {

    Mockito.when(cartManagerService.findByKey(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new CartItemDto()));
    Mockito.when(cartBusinessService.update(Mockito.any(UserInfo.class),
        Mockito.any(), Mockito.eq(ShopType.DEFAULT_SHOPPING_CART))).thenReturn(new ShoppingCart());

    vinPackageService.addVinItemToShoppingCart(DataProvider.createUserInfo(),
        buildLicenseSettingsDto());

    Mockito.verify(cartBusinessService, Mockito.times(1)).update(Mockito.any(UserInfo.class),
        Mockito.any(), Mockito.eq(ShopType.DEFAULT_SHOPPING_CART));
  }

  @Test
  public void testAddVinLicense_NotExisted() {

    Mockito.when(cartManagerService.findByKey(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
    Mockito.when(cartBusinessService.add(Mockito.any(UserInfo.class),
        Mockito.anyList(), Mockito.eq(ShopType.DEFAULT_SHOPPING_CART))).thenReturn(new ShoppingCart());
    Mockito.when(ivdsArticleService.searchVinArticle(Mockito.any(UserInfo.class), Mockito.anyString()))
    .thenReturn(new ArticleDocDto ());

    vinPackageService.addVinItemToShoppingCart(DataProvider.createUserInfo(),
        buildLicenseSettingsDto());

    Mockito.verify(cartBusinessService, Mockito.times(1)).add(Mockito.any(UserInfo.class),
        Mockito.anyList(), Mockito.eq(ShopType.DEFAULT_SHOPPING_CART));
  }

  private LicenseSettingsDto buildLicenseSettingsDto() {
    return LicenseSettingsDto.builder().build();
  }
}
