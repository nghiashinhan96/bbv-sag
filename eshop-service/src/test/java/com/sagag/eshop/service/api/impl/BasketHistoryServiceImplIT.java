package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.criteria.BasketHistoryCriteria;
import com.sagag.eshop.service.api.BasketHistoryService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.BasketHistoryDto;
import com.sagag.eshop.service.dto.BasketHistoryItemDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

/**
 * IT to verify for {@link BasketHistoryService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
@Transactional
public class BasketHistoryServiceImplIT {

  /**
   * IMPORTANT: Because axtest DB is not imported the list of dummy data for ax version,
   * I just input some incorrect data test to verify functionalities.
   *
   */
  private static final String BASKET_NAME_TEST = "TEST_BASKET_NAME";

  private static final String CUSTOMER_NUMBER = "1127208";

  private static final Long SALES_USER_ID = 5L;

  private static final Long B2B_USER_ID = 26L;

  @Autowired
  private BasketHistoryService basketHistoryService;

  @Test
  public void testCreateBasketHistoryWithB2BUser() {
    BasketHistoryDto basketHistory = new BasketHistoryDto();
    basketHistory.setBasketName(BASKET_NAME_TEST);
    basketHistory.setCreatedUserId(B2B_USER_ID);
    basketHistory.setItems(items());
    basketHistory.setGrandTotalExcludeVat(NumberUtils.DOUBLE_ZERO);

    basketHistoryService.createBasketHistory(basketHistory);
  }

  @Test
  public void testCreateBasketHistoryWithSalesUser() {
    BasketHistoryDto basketHistory = new BasketHistoryDto();
    basketHistory.setBasketName(BASKET_NAME_TEST);
    basketHistory.setCreatedUserId(B2B_USER_ID);
    basketHistory.setSalesUserId(SALES_USER_ID);
    basketHistory.setItems(items());
    basketHistory.setGrandTotalExcludeVat(NumberUtils.DOUBLE_ZERO);

    basketHistoryService.createBasketHistory(basketHistory);

  }

  @Test
  public void testCreateBasketHistoryWithSalesUserAndEmptyBasketName() {
    BasketHistoryDto basketHistory = new BasketHistoryDto();
    basketHistory.setBasketName(StringUtils.EMPTY);
    basketHistory.setCreatedUserId(B2B_USER_ID);
    basketHistory.setSalesUserId(SALES_USER_ID);
    basketHistory.setItems(items());
    basketHistory.setGrandTotalExcludeVat(NumberUtils.DOUBLE_ZERO);

    basketHistoryService.createBasketHistory(basketHistory);
  }

  private static List<BasketHistoryItemDto> items() {
    BasketHistoryItemDto item = new BasketHistoryItemDto();
    item.setArticles(Arrays.asList(new ArticleDocDto()));
    item.setVehicle(new VehicleDto());
    return Arrays.asList(item);
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithAllRecords() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName(StringUtils.EMPTY);
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, true);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithCreatedB2BUser() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName(StringUtils.EMPTY);
    criteria.setCreatedUserId(B2B_USER_ID);
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithSaleUser() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName(StringUtils.EMPTY);
    criteria.setSalesUserId(SALES_USER_ID);
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, true);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithSaleUserOnbehalf() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName(StringUtils.EMPTY);
    criteria.setSalesUserId(SALES_USER_ID);
    criteria.setCreatedUserId(B2B_USER_ID);
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, true);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithCustomerNumber() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName(StringUtils.EMPTY);
    criteria.setCustomerNumber(CUSTOMER_NUMBER);
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithCustomerShortName() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName("Basket name test");
    criteria.setCustomerName("Eumo Car Gmbh");
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    criteria.setOrderByDescBasketName(true);
    criteria.setOrderByDescCustomerNumber(false);
    criteria.setOrderByDescGrandTotalExcludeVat(true);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithCreatedLastName() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setBasketName("Basket name test");
    criteria.setCreatedLastName("AX");
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    criteria.setOrderByDescBasketName(true);
    criteria.setOrderByDescCustomerNumber(false);
    criteria.setOrderByDescGrandTotalExcludeVat(true);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithOrderByDescUpdatedDate() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    criteria.setOrderByDescCustomerName(true);
    criteria.setOrderByDescUpdatedDate(true);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    log.debug("Result Json = {}", SagJSONUtil.convertObjectToJson(basketHistories));

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithOrderByAscUpdatedDate() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    criteria.setOrderByDescCustomerName(true);
    criteria.setOrderByDescUpdatedDate(false);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    log.debug("Result Json = {}", SagJSONUtil.convertObjectToJson(basketHistories));

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithUpdatedDate() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    criteria.setUpdatedDate(new Date(NumberUtils.createLong("1501020102697")));
    criteria.setOrderByDescCustomerName(true);
    criteria.setOrderByDescUpdatedDate(false);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    log.debug("Result Json = {}", SagJSONUtil.convertObjectToJson(basketHistories));

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testSearchAllBasketHistoriesByCriteria() {
    BasketHistoryDto basketHistory = new BasketHistoryDto();
    basketHistory.setBasketName(BASKET_NAME_TEST);
    basketHistory.setCreatedUserId(B2B_USER_ID);
    basketHistory.setItems(items());
    basketHistory.setGrandTotalExcludeVat(NumberUtils.DOUBLE_ZERO);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(new BasketHistoryCriteria(), false);

    log.debug("Request basket history = {}", basketHistory);

    Assert.assertThat(basketHistories.hasContent(), Matchers.is(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchBasketHistoriesByCriteriaWithNullCriteria() {

    BasketHistoryCriteria criteria = null;
    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, false);

    log.debug("Result = {}", basketHistories.getContent());

    log.debug("Result Json = {}", SagJSONUtil.convertObjectToJson(basketHistories));

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }

  @Test
  public void testCountBasketHistoriesBySalesUser() throws Exception {

    long totalItems = basketHistoryService.countSalesBasketHistories(SALES_USER_ID);
    Assert.assertThat(totalItems, Matchers.greaterThanOrEqualTo(Long.valueOf(0)));
  }

  @Test
  public void testCountBasketHistoriesByB2BUser() throws Exception {

    long totalItems = basketHistoryService.countBasketHistoriesByCustomer(CUSTOMER_NUMBER);
    Assert.assertThat(totalItems, Matchers.greaterThanOrEqualTo(Long.valueOf(0)));
  }

  @Test
  public void testSearchBasketHistoriesByCriteriaWithCustomerRefText() {

    BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setCustomerRefText("text");
    criteria.setOffset(NumberUtils.INTEGER_ZERO);
    criteria.setPageSize(SagConstants.MIN_PAGE_SIZE);

    criteria.setOrderByDescBasketName(true);
    criteria.setOrderByDescCustomerNumber(false);
    criteria.setOrderByDescGrandTotalExcludeVat(true);

    Page<BasketHistoryDto> basketHistories =
        basketHistoryService.searchBasketHistoriesByCriteria(criteria, true);

    log.debug("Result = {}", basketHistories.getContent());

    Assert.assertThat(basketHistories.hasContent(), Matchers.equalTo(true));
  }
}
