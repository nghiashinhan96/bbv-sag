package com.sagag.eshop.service.api.impl.offer;

import com.sagag.eshop.repo.criteria.offer.OfferSearchCriteria;
import com.sagag.eshop.service.api.OfferService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferGeneralDto;
import com.sagag.eshop.service.tests.utils.UserInfoTestUtils;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferStatus;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.external.Customer;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for offer service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
@Transactional
public class OfferServiceImplIT {

  private final Pageable paging = PageRequest.of(0, 10);

  @Autowired
  private OfferService offerService;

  @Test
  public void testGetOfferDetails() {
    final UserInfo user = UserInfoTestUtils.buildOfferUserInfo();
    final Optional<OfferDto> offer = offerService.getOfferDetails(user, 25L);
    Assert.assertTrue(offer.isPresent());
    log.debug("###   testGetOfferDetails");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offer.get()));
  }

  @Test
  public void testOffersMatchingAllCriteria() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingAllCriteria");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersOrderedByOfferNumber() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setOrderDescByOfferNumber(true);
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertThat(offers.hasContent(), Matchers.is(true));
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersOrderedByOfferNumber");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }


  @Test
  public void testOffersOrderedByVehicleDesc() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setOrganisationId(6);
    criteria.setOrderDescByVehicleDesc(true);
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersOrderedByVehicleDesc");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }


  @Test
  public void testOffersMatchingOfferNr() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setOfferNumber("W-997");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingOfferNr");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersMatchingVehicleDesc() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setOrganisationId(8);
    criteria.setVehicleDesc("LADA");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingVehicleDesc");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersMatchingCustomerName() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setCustomerName("rage-D");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingCustomerName");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersMatchingOfferDate() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setOfferDate("05.03");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingOfferDate");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersMatchingOfferDate2() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setOfferDate("05.3");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingOfferDate2");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersMatchingPrice() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setPrice("1302.5");
    criteria.setCollectionShortname("derendinger-at");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingPrice");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }


  @Test
  public void testOffersMatchingStatus() {

    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setStatus("INPROCES");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingStatus");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testOffersMatchingUsername() {
    final OfferSearchCriteria criteria = buildFreshOfferCriteria();
    criteria.setUsername("admi");
    final Page<OfferGeneralDto> offers = offerService.searchOffers(criteria, paging);

    Assert.assertNotNull(offers);
    Assert.assertNotNull(offers.getContent());
    log.debug("###   testOffersMatchingUsername");
    log.debug("result {}", SagJSONUtil.convertObjectToPrettyJson(offers));
  }

  @Test
  public void testCreateOffer() {
    final UserInfo user = new UserInfo();
    user.setId(27L);
    user.setOrganisationId(41);
    final Customer customer = new Customer();
    customer.setNr(1130438L);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    final OfferDto offerDto = offerService.create(user);
    Assert.assertTrue(OfferStatus.OPEN.name().equals(offerDto.getStatus()));
    Assert.assertTrue(OfferStatus.OPEN.name().equals(offerDto.getStatus()));
    log.debug("offerDto.getId().toString() {} ", offerDto.getId().toString());
  }

  private OfferSearchCriteria buildFreshOfferCriteria() {
    return OfferSearchCriteria.builder().vehicleDesc(StringUtils.EMPTY)
        .offerNumber(StringUtils.EMPTY).remark(StringUtils.EMPTY).customerName(StringUtils.EMPTY)
        .offerDate(StringUtils.EMPTY).price(StringUtils.EMPTY).status(StringUtils.EMPTY)
        .username(StringUtils.EMPTY).organisationId(41).build();
  }

}
