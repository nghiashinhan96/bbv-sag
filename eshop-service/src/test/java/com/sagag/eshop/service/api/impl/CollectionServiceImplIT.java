package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.backoffice.dto.CollectionSearchResultDto;
import com.sagag.services.domain.eshop.criteria.CollectionSearchCriteria;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * Integration tests for branch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class CollectionServiceImplIT {

  @Autowired
  private OrganisationCollectionService organisationCollecitonService;

  @Test
  public void searchCollection_shouldReturnCollection_givenAffilate() {
    final CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().affiliate("wbb").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.getTotalElements(), Is.is(2L));
  }

  @Test
  public void searchCollection_shouldReturnCollection_givenCollectionName() {
    final CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().collectionName("Walchli-Bollier-Bulach").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchCollection_shouldReturnCollection_givenCustomerNr() {
    final CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().customerNr("8832646").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchCollection_shouldReturnCollection_givenCustomerNrCollectionName() {
    final CollectionSearchCriteria criteria = CollectionSearchCriteria.builder()
        .customerNr("8832646").collectionName("Matik-Austria").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchCollection_shouldReturnCollection_givenAffiliateCollectionName() {
    final CollectionSearchCriteria criteria = CollectionSearchCriteria.builder()
        .collectionName("Walchli-Bollier-Bulach").affiliate("wbb").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchCollection_shouldReturnCollection_noConditions() {
    final CollectionSearchCriteria criteria = CollectionSearchCriteria.builder().build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchCollection_shouldReturnCollection_givenCustomerNrAffiliate() {
    final CollectionSearchCriteria criteria = CollectionSearchCriteria.builder()
        .customerNr("8832646").collectionName("Matik-Austria").affiliate("matik-at").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void getAllCustomerNr_shouldReturnCustomerNr_givenCollectionName() {
    final String collectionShortName = "derendinger-at";
    Pageable pageable = PageRequest.of(0, 2);
    final Page<String> customers =
        organisationCollecitonService.getAllCustomerNr(collectionShortName, pageable);
    Assert.assertThat(customers.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchCollection_shouldNotReturnCollection_givenCustomerNr() {
    final CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().customerNr("883264611111").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(false));
  }
  @Test
  public void searchCollection_shouldReturnMoreCollections_givenCustomerNr() {
    final CollectionSearchCriteria criteria =
        CollectionSearchCriteria.builder().customerNr("8").build();
    final Page<CollectionSearchResultDto> resultDto =
        organisationCollecitonService.searchCollection(criteria);
    Assert.assertThat(resultDto.getTotalElements(), Is.is(2L));
  }

}
