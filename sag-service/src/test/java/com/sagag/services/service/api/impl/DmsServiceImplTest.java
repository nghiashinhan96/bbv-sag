package com.sagag.services.service.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferPositionDto;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.common.enums.offer.OfferStatus;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.api.DmsExportCacheService;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.hazelcast.api.VinOrderCacheService;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.OfferBusinessService;
import com.sagag.services.service.request.dms.DmsExportRequest;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DmsServiceImplTest {

  @InjectMocks
  private DmsServiceImpl dmsService;

  @Mock
  private OfferBusinessService offerBusinessService;

  @Mock
  private VinOrderCacheService vinOrderCacheService;

  @Mock
  private DmsExportCacheService dmsExportCacheService;

  @Mock
  private CartBusinessService cartBusinessService;

  @Mock
  private CouponCacheService couponCacheService;

  @Mock
  private HaynesProCacheService haynesProCacheService;

  @Test
  public void testAddOfferToShoppingCartSuccess() {
    OfferDto offer = buildDmsOfferDto();

    dmsService.addOfferToShoppingCart(DataProvider.createUserInfo(), offer,
        ShopType.DEFAULT_SHOPPING_CART);

    verify(offerBusinessService, times(1)).orderOffer(any(UserInfo.class),
        any(), any());
  }

  @Test
  public void testAddOfferToShoppingCart_EmptyOfer() {
    OfferDto offer = buildDmsOfferDto();
    offer.setOfferPositions(new ArrayList<>());

    dmsService.addOfferToShoppingCart(DataProvider.createUserInfo(), offer,
        ShopType.DEFAULT_SHOPPING_CART);
  }

  @Test
  public void testDownloadSuccess() {

    when(dmsExportCacheService.getFileContent(anyString())).thenReturn(StringUtils.EMPTY);
    doNothing().when(dmsExportCacheService).clearCache(anyString());

    dmsService.download(DataProvider.USER_ID, DataProvider.CUSTOMER_NR.toString());
    verify(dmsExportCacheService, times(1)).clearCache(anyString());
  }

  @Test
  public void testExportOfferSuccess() {
    doNothing().when(dmsExportCacheService).add(anyString(), anyString());
    doNothing().when(cartBusinessService).clear(any(UserInfo.class),
        eq(ShopType.DEFAULT_SHOPPING_CART));
    doNothing().when(couponCacheService).clearCache(anyString());
    doNothing().when(vinOrderCacheService).clearSearchCount(anyString());

    dmsService.export(DataProvider.createUserInfo(), buildDmsExportRequest());

    verify(vinOrderCacheService, times(1)).clearSearchCount(anyString());
  }

  private OfferDto buildDmsOfferDto() {
    List<OfferPositionDto> offerPositions = new ArrayList<>();
    offerPositions.add(buildOfferPositionDto());

    return OfferDto.builder()
        .id(40194L)
        .customerNr("1130438")
        .offerDate(Calendar.getInstance().getTime())
        .status(OfferStatus.OPEN.name())
        .vat(0.2)
        .offerPositions(offerPositions)
        .build();
  }

  private OfferPositionDto buildOfferPositionDto() {

    return OfferPositionDto.builder()
        .id(5413L)
        .offerId(40194L)
        .type(OfferPositionType.VENDOR_ARTICLE_WITHOUT_VEHICLE.name())
        .shopArticleId("0")
        .articleDescription("Pfand")
        .quantity(3.0)
        .grossPrice(3.0)
        .netPrice(3.0)
        .totalGrossPrice(9.0)
        .actionType("NONE")
        .version(0)
        .build();
  }

  private DmsExportRequest buildDmsExportRequest() {
    return DmsExportRequest.builder()
        .dmsCommand("CMD:ORDER")
        .orders(new ArrayList<>())
        .build();
  }

}
