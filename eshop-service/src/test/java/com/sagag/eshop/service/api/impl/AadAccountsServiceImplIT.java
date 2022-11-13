package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.sagag.eshop.service.api.AadAccountsService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.DuplicatedEmailException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.criteria.AadAccountsSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.AadAccountsDto;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.transaction.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class AadAccountsServiceImplIT {

  @Autowired
  private AadAccountsService service;

  @Test(expected = DuplicatedEmailException.class)
  public void create_shouldThrowException_givenDuplicatedEmail() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().firstName("Adriano").lastName("Sinigoi")
        .primaryContactEmail("adriano.sinigoi@derendinger.at").personalNumber("5011").gender("Male")
        .legalEntityId("30DA").build();
    service.create(dto);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_shouldThrowException_givenMissingFirstName() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().lastName("Sinigoi")
        .primaryContactEmail("adriano.sinigoi@derendinger.at").personalNumber("5011").gender("Male")
        .legalEntityId("30DA").build();
    service.create(dto);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_shouldThrowException_givenMissingLastName() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().firstName("Adriano")
        .primaryContactEmail("adriano.sinigoi@derendinger.at").personalNumber("5011").gender("Male")
        .legalEntityId("30DA").build();

    service.create(dto);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_shouldThrowException_givenMissingEmail() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().firstName("Adriano").lastName("Sinigoi")
        .personalNumber("5011").gender("Male").legalEntityId("30DA").build();
    service.create(dto);
  }

  @Test
  public void create_shouldCreatSuccessfully_givenValidInput() throws Exception {
    AadAccountsDto dto = AadAccountsDto.builder().firstName("Albert").lastName("Salfinger")
        .primaryContactEmail("albert.salfinger@derendinger.at").personalNumber("5031")
        .gender("Male").legalEntityId("30DA").build();
    service.create(dto);
  }

  @Test
  public void search_shouldReturnAadAccounts_givenCriteriaHasFoundFirstName() throws Exception {
    AadAccountsSearchRequestCriteria criteria =
        AadAccountsSearchRequestCriteria.builder().firstName("Amir").build();

    List<AadAccountsSearchResultDto> results =
        service.search(criteria, PageUtils.DEF_PAGE).getContent();
    assertNotNull(results);
    assertThat(results.size(), Matchers.is(2));
  }

  @Test
  public void search_shouldReturnEmpty_givenCriteriaHasNotFoundFirstName() throws Exception {
    AadAccountsSearchRequestCriteria criteria =
        AadAccountsSearchRequestCriteria.builder().firstName("beckham").build();

    List<AadAccountsSearchResultDto> results =
        service.search(criteria, PageUtils.DEF_PAGE).getContent();
    assertThat(results.size(), Matchers.is(0));
  }

  @Test
  public void search_shouldReturnSortedItem_givenCriteriaSortDescByEmail() throws Exception {
    AadAccountsSearchRequestCriteria criteria = AadAccountsSearchRequestCriteria.builder()
        .firstName("Amir").orderDescByPrimaryContactEmail(true).build();

    List<AadAccountsSearchResultDto> results =
        service.search(criteria, PageUtils.DEF_PAGE).getContent();
    assertNotNull(results);
    assertThat(results.size(), Matchers.is(2));
    assertThat(results.get(0).getPrimaryContactEmail(), Matchers.is("danhnguyen@bbv.ch"));
  }

  @Test
  public void update_shouldUpdateSuccessfully_givenUpdatingModel() throws Exception {
    String firstName = "updated FirstName";
    String email = "amir.jorgic_1@derendinger.at";
    AadAccountsDto dto = AadAccountsDto.builder().firstName(firstName).personalNumber("32767")
        .lastName("Jorgic").primaryContactEmail(email).build();
    service.update(dto, 1);

    AadAccountsSearchRequestCriteria criteria =
        AadAccountsSearchRequestCriteria.builder().primaryContactEmail(email).build();
    List<AadAccountsSearchResultDto> results =
        service.search(criteria, PageUtils.DEF_PAGE).getContent();
    assertThat(results.get(0).getFirstName(), Matchers.is(firstName));
  }
}
