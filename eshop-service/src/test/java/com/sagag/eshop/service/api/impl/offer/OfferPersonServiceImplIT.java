package com.sagag.eshop.service.api.impl.offer;

import com.sagag.eshop.repo.criteria.offer.OfferPersonSearchCriteria;
import com.sagag.eshop.service.api.OfferPersonService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for offer person service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
@Transactional
public class OfferPersonServiceImplIT {

  private static final Long USER_ID = 10057L;

  @Autowired
  private OfferPersonService offerPersonService;

  @Test
  public void testCreateNewOfferPerson() {
    final OfferPersonDto offerPersonRequest = new OfferPersonDto();
    offerPersonRequest.setCurrentUserId(USER_ID);
    offerPersonRequest.setOrganisationId(1);
    offerPersonRequest.setSalutation("GENERAL_SALUTATION_FEMALE");
    offerPersonRequest.setCompanyName("companyName test");
    offerPersonRequest.setFirstName("firstName test");
    offerPersonRequest.setLastName("lastName test");
    offerPersonRequest.setRoad("road test");
    offerPersonRequest.setAdditionalAddress1("additionalAddress1 test");
    offerPersonRequest.setAdditionalAddress2("additionalAddress2 test");
    offerPersonRequest.setPoBox("poBox test");
    offerPersonRequest.setPostCode("1234");
    offerPersonRequest.setPhone("123456789");
    offerPersonRequest.setFax("123456");
    offerPersonRequest.setEmail("email@bbv.vn");

    final OfferPersonDto createdOfferPerson =
        offerPersonService.createOfferPerson(offerPersonRequest);

    Assert.assertNotNull(createdOfferPerson);
    Assert.assertNotNull(createdOfferPerson.getId());
  }

  @Test
  public void testSearchOfferPersonByCriteria_WithEmptyText() {
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().organisationId(3).build();

    final Page<ViewOfferPersonDto> offerPerson =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(offerPerson, Matchers.notNullValue());
  }

  @Test
  public void testSearchOfferPerson_WithName() {
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().name("Ekrem").organisationId(3).build();

    final Page<ViewOfferPersonDto> offerPerson =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(offerPerson, Matchers.notNullValue());
  }

  @Test
  public void testSearchOfferPerson_WithAddress() {
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().address("Gratkorn").organisationId(8).build();

    final Page<ViewOfferPersonDto> offerPerson =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(offerPerson, Matchers.notNullValue());
  }

  @Test
  public void testSearchOfferPerson_WithContactInfo() {

    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().contactInfo("COMPLETE").organisationId(3).build();

    final Page<ViewOfferPersonDto> offerPerson =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(offerPerson, Matchers.notNullValue());
  }

  @Test
  public void testSearchOfferPersonByCriteria_OrderByNameDesc() {
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().organisationId(6).orderDescByName(true).build();

    final Page<ViewOfferPersonDto> offerPersons =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);
    if (offerPersons.getContent().size() > 1) {
      Assert.assertTrue(offerPersons.getContent().get(0).getFirstName()
          .compareTo(offerPersons.getContent().get(1).getFirstName()) >= 1);
    }
    Assert.assertThat(offerPersons, Matchers.notNullValue());
    log.debug("testSearchOfferPersonByCriteria_OrderByNameDesc offerPersons {}",
        SagJSONUtil.convertObjectToPrettyJson(offerPersons));
  }

  @Test
  public void testSearchOfferPersonByCriteria_OrderByContactInfoDesc() {
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().organisationId(6).orderDescByContactInfo(true).build();

    final Page<ViewOfferPersonDto> offerPersons =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);
    Assert.assertThat(offerPersons, Matchers.notNullValue());
    if (offerPersons.getContent().size() > 1) {
      Assert.assertTrue(offerPersons.getContent().get(0).getPhone()
          .compareTo(offerPersons.getContent().get(1).getPhone()) >= 1);
    }
    log.debug("testSearchOfferPersonByCriteria_OrderByContactInfoDesc offerPersons {}",
        SagJSONUtil.convertObjectToPrettyJson(offerPersons));
  }

  @Test
  public void testSearchOfferPersonByCriteria_OrderByAddressDesc() {
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().organisationId(6).orderDescByAddress(true).build();

    final Page<ViewOfferPersonDto> offerPersons =
        offerPersonService.searchOfferPersons(criteria, PageUtils.DEF_PAGE);
    if (offerPersons.getContent().size() > 1) {
      Assert.assertTrue(offerPersons.getContent().get(0).getRoad()
          .compareTo(offerPersons.getContent().get(1).getRoad()) >= 1);
    }
    Assert.assertThat(offerPersons, Matchers.notNullValue());
    log.debug("testSearchOfferPersonByCriteria_OrderByAddressDesc offerPersons {}",
        SagJSONUtil.convertObjectToPrettyJson(offerPersons));
  }

  @Test
  public void testEditOfferPerson() {
    final OfferPersonDto offerPersonRequest = new OfferPersonDto();
    offerPersonRequest.setId(24L);
    offerPersonRequest.setCurrentUserId(USER_ID);
    offerPersonRequest.setOrganisationId(7);
    offerPersonRequest.setSalutation("GENERAL_SALUTATION_FEMALE");
    offerPersonRequest.setCompanyName("companyName updated");
    offerPersonRequest.setFirstName("firstName test");
    offerPersonRequest.setLastName("lastName updated");
    offerPersonRequest.setRoad("road test");
    offerPersonRequest.setAdditionalAddress1("additionalAddress1 updated");
    offerPersonRequest.setAdditionalAddress2("additionalAddress2 test");
    offerPersonRequest.setPoBox("poBox updated");
    offerPersonRequest.setPostCode("1234");
    offerPersonRequest.setPhone("123456");
    offerPersonRequest.setFax("123456789");
    offerPersonRequest.setEmail("email@bbv.vn");

    final OfferPersonDto editedOfferPerson = offerPersonService.editOfferPerson(offerPersonRequest);

    Assert.assertNotNull(editedOfferPerson);
    Assert.assertNotNull(editedOfferPerson.getId());
    Assert.assertEquals(editedOfferPerson.getId(), offerPersonRequest.getId());
    Assert.assertEquals(editedOfferPerson.getLastName(), offerPersonRequest.getLastName());
    Assert.assertEquals(editedOfferPerson.getAdditionalAddress1(),
        offerPersonRequest.getAdditionalAddress1());
    Assert.assertEquals(editedOfferPerson.getPhone(), offerPersonRequest.getPhone());
  }

  @Test
  public void testGetOfferPersonDetails() {
    final long offerPersonId = 24L;
    final Optional<OfferPersonDto> offerPerson =
        offerPersonService.getOfferPersonDetails(offerPersonId);
    Assert.assertThat(offerPerson.isPresent(), Matchers.is(true));
    Assert.assertThat(offerPerson.get().getId(), Matchers.equalTo(offerPersonId));
  }

  @Test
  public void testRemoveOfferPerson() {
    final OfferPersonDto offerPerson =
        offerPersonService.removeOfferPerson(USER_ID, 24L);
    Assert.assertNotNull(offerPerson);
  }
}
