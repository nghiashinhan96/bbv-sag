package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.criteria.BasketHistoryCriteria;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.eshop.service.api.BasketHistoryService;
import com.sagag.eshop.service.dto.BasketHistoryDto;
import com.sagag.eshop.service.dto.BasketHistoryItemDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.enums.BasketFilterEnum;
import com.sagag.services.service.request.BasketHistorySearchRequest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * UT for {@link BasketServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class BasketServiceImplTest {

  @Mock
  private CartBusinessService cartBusinessService;

  @Mock
  private BasketHistoryService basketHistoryService;

  @InjectMocks
  private BasketServiceImpl basketService;

  @Mock
  private BasketHistory basketHistory;

  @Mock
  private UserInfo user;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetBasketHistoriesWithB2BUserWithNoResult() {

    user.setId(Mockito.anyLong());
    Mockito.when(user.isUserAdminRole()).thenReturn(true);

    BasketHistorySearchRequest request = new BasketHistorySearchRequest();
    request.setOffset(0);
    request.setSize(SagConstants.MIN_PAGE_SIZE);
    request.setBasketFilterMode(BasketFilterEnum.CUSTOMER_BASKET);

    mockSearchBasketHistoriesByCriteriaWithSize(0);

    Page<BasketHistoryDto> histories = basketService.getBasketHistories(request, user);

    Assert.assertThat(histories.hasContent(), Matchers.equalTo(false));
  }

  @Test
  public void testGetBasketHistoriesWithB2BUserWithHasResult() {

    user.setId(Mockito.anyLong());
    Mockito.when(user.isUserAdminRole()).thenReturn(true);

    BasketHistorySearchRequest request = new BasketHistorySearchRequest();
    request.setOffset(0);
    request.setSize(SagConstants.MIN_PAGE_SIZE);
    request.setBasketFilterMode(BasketFilterEnum.CUSTOMER_BASKET);

    mockSearchBasketHistoriesByCriteriaWithSize(10);

    Page<BasketHistoryDto> histories = basketService.getBasketHistories(request, user);

    Assert.assertThat(histories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testGetBasketHistoriesWithSalesUserWithNoResult() {

    user.setId(Mockito.anyLong());
    Mockito.when(user.isSalesNotOnBehalf()).thenReturn(true);

    BasketHistorySearchRequest request = new BasketHistorySearchRequest();
    request.setOffset(0);
    request.setSize(SagConstants.MIN_PAGE_SIZE);
    request.setBasketFilterMode(BasketFilterEnum.ALL_BASKET);

    mockSearchBasketHistoriesByCriteriaWithSize(0);

    Page<BasketHistoryDto> histories = basketService.getBasketHistories(request, user);

    Assert.assertThat(histories.hasContent(), Matchers.equalTo(false));
  }

  @Test
  public void testGetBasketHistoriesWithSalesUserWithHasResult() {

    user.setId(Mockito.anyLong());
    Mockito.when(user.isSalesNotOnBehalf()).thenReturn(true);

    BasketHistorySearchRequest request = new BasketHistorySearchRequest();
    request.setOffset(0);
    request.setSize(SagConstants.MIN_PAGE_SIZE);
    request.setBasketFilterMode(BasketFilterEnum.ALL_BASKET);

    mockSearchBasketHistoriesByCriteriaWithSize(10);

    Page<BasketHistoryDto> histories = basketService.getBasketHistories(request, user);

    Assert.assertThat(histories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testGetBasketHistoryDetailsWithSalesUserWithHasResult() {
    user.setId(Mockito.anyLong());
    Mockito.when(user.isSalesNotOnBehalf()).thenReturn(true);
    basketHistory = new BasketHistory();
    final BasketHistoryItemDto basketItemDto = new BasketHistoryItemDto();
    basketItemDto.setArticles(Arrays.asList(new ArticleDocDto()));
    basketItemDto.setVehicle(new VehicleDto());
    basketHistory.setBasketJson(SagJSONUtil.convertObjectToJson(Arrays.asList(basketItemDto)));

    Mockito.when(basketHistoryService.getBasketHistoryDetails(Mockito.anyLong()))
      .thenReturn(Optional.of(new BasketHistoryDto()));

    final BasketHistoryDto basketHistoryDetails =
        basketService.getBasketHistoryDetails(20L).orElse(null);
    Assert.assertNotNull(basketHistoryDetails);
  }

  private void mockSearchBasketHistoriesByCriteriaWithSize(int size) {
    Mockito
        .when(basketHistoryService
            .searchBasketHistoriesByCriteria(Mockito.any(BasketHistoryCriteria.class), Mockito.eq(false)))
        .thenReturn(new PageImpl<>(content(size)));
  }

  private static List<BasketHistoryDto> content(int size) {
    if (size <= 0) {
      return Collections.emptyList();
    }

    List<BasketHistoryDto> items = new ArrayList<>();
    BasketHistoryDto item;
    int index;
    for (index = 0; index < size; index++) {
      item = new BasketHistoryDto();
      item.setBasketName("Item " + index + 1);
      BasketHistoryItemDto subItem = new BasketHistoryItemDto();
      subItem.setArticles(Arrays.asList(new ArticleDocDto()));
      subItem.setVehicle(new VehicleDto());
      item.setItems(Arrays.asList(subItem));

      items.add(item);
    }

    return items;
  }

}
