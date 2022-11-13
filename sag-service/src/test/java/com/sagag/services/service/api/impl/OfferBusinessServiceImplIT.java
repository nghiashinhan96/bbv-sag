package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferActionType;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.OfferBusinessService;
import com.sagag.services.service.request.offer.OfferPositionItemRequestBody;
import com.sagag.services.service.request.offer.OfferRequestBody;
import com.sagag.services.service.utils.UserDataProvider;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Integration test class for offer business service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Slf4j
@Transactional
public class OfferBusinessServiceImplIT {

  @Autowired
  private OfferBusinessService offerBusinessService;

  private static final String OFFER_NUMBER_TEST = "OFFER_NUMBER_TEST";

  @Test
  public void testUpdateOfferCaseRemovePosition() {
    UserInfo user = UserDataProvider.buildOfferUserInfo();
    OfferRequestBody offerRequestBody = buildOfferRequests();
    offerRequestBody.setOfferId(25L);
    offerRequestBody.setTotalGrossPrice(31.5);
    final OfferDto offers = offerBusinessService.updateOffer(user, offerRequestBody);

    Assert.assertNotNull(offers);
    Assert.assertTrue(offers.getOfferNr().equals(OFFER_NUMBER_TEST));
    log.debug("###   testUpdateOfferCaseRemovePosition");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testUpdateOfferCaseUpdateNewPosition() {
    UserInfo user = UserDataProvider.buildOfferUserInfo();
    OfferRequestBody offerRequestBody = buildOfferRequests();
    offerRequestBody.setOfferId(25L);
    offerRequestBody.setTotalGrossPrice(31.5);
    OfferPositionItemRequestBody existingOfferPosition = buildOfferPositionRequest();
    existingOfferPosition.setOfferPositionId(16L);
    existingOfferPosition.setQuantity(2.0);
    existingOfferPosition.setGrossPrice(15.5);
    existingOfferPosition.setTotalGrossPrice(31.0);

    OfferPositionItemRequestBody newOfferPosition = buildOfferPositionRequest();
    List<OfferPositionItemRequestBody> offerPositionItems = new ArrayList<>();
    offerPositionItems.add(existingOfferPosition);
    offerPositionItems.add(newOfferPosition);
    offerRequestBody.setOfferPositionRequests(offerPositionItems);
    final OfferDto offers = offerBusinessService.updateOffer(user, offerRequestBody);

    Assert.assertNotNull(offers);
    Assert.assertTrue(offers.getOfferNr().equals(OFFER_NUMBER_TEST));
    log.debug("###   testUpdateOfferCaseUpdateNewPosition");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testUpdateOfferCaseUpdateExistingPosition() {
    UserInfo user = UserDataProvider.buildOfferUserInfo();
    OfferRequestBody offerRequestBody = buildOfferRequests();
    offerRequestBody.setOfferId(25L);
    offerRequestBody.setTotalGrossPrice(31.5);
    OfferPositionItemRequestBody updateOfferPosition = buildOfferPositionRequest();
    updateOfferPosition.setOfferPositionId(16L);
    updateOfferPosition.setQuantity(3.0);
    updateOfferPosition.setGrossPrice(10.5);
    updateOfferPosition.setTotalGrossPrice(31.5);

    offerRequestBody.setOfferPositionRequests(Arrays.asList(updateOfferPosition));
    final OfferDto offers = offerBusinessService.updateOffer(user, offerRequestBody);

    Assert.assertNotNull(offers);
    Assert.assertTrue(offers.getOfferNr().equals(OFFER_NUMBER_TEST));
    log.debug("###   testUpdateOfferCaseUpdateExistingPosition");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  private OfferRequestBody buildOfferRequests() {
    OfferRequestBody offerRequestBody = new OfferRequestBody();
    offerRequestBody.setOfferNumber(OFFER_NUMBER_TEST);
    offerRequestBody.setOfferPersonId(24L);

    return offerRequestBody;
  }

  private OfferPositionItemRequestBody buildOfferPositionRequest() {
    OfferPositionItemRequestBody newofferPosition = OfferPositionItemRequestBody.builder()
    .offerPositionId(null)
    .actionType(OfferActionType.NONE)
    .shopArticleId("2")
    .articleDescription("Test ArticleDescription")
    .quantity(2.0)
    .totalGrossPrice(14.21)
    .type(OfferPositionType.CLIENT_ARTICLE).build();

    return newofferPosition;
  }

}
