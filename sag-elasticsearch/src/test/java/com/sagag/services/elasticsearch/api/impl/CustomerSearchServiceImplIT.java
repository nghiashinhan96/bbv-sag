package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.CustomerSearchService;
import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.elasticsearch.criteria.Telephone;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Optional;

/**
 * IT Test class for customer search.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class CustomerSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private CustomerSearchService customerService;

  private CustomerSearchCriteria criteria;

  private Pageable pageable = PageUtils.DEF_PAGE;

  @Before
  public void initCriteria() {
    criteria = new CustomerSearchCriteria();
    criteria.setAffiliates(Arrays.asList(SupportedAffiliate.MATIK_AT.getCompanyName().toLowerCase(),
        SupportedAffiliate.DERENDINGER_AT.getCompanyName().toLowerCase()));
  }

  @After
  public void clearCriteria() {
    criteria.setAffiliates(null);
    criteria.setTelephone(Optional.empty());
    criteria.setText(null);
  }

  @Test
  public void givenATTelephone_shouldGetCustomerByPhone() {
    final Telephone tel = new Telephone("43", "69912120025");
    criteria.setTelephone(Optional.of(tel));
    final Optional<CustomerDoc> cust = customerService.searchCustomerByTelephone(criteria);
    Assert.assertThat(cust.isPresent(), Matchers.is(true));
  }

  @Test
  @Ignore("ES data has been changed, all the telephone of customer CH null")
  public void givenCHTelephone_shouldGetCustomerByPhone() {
    final Telephone tel = new Telephone("41", "732247475");
    criteria.setTelephone(Optional.of(tel));
    final Optional<CustomerDoc> cust = customerService.searchCustomerByTelephone(criteria);
    Assert.assertThat(cust.isPresent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByCustName() {
    final String custName = "Walter";
    criteria.setText(custName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasName() {
    final String aliasName = "POSZWE"; // custNr = 1106423
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByPrimaryEmail() {
    final String primaryEmail = "carklinik.holy@gmx.at";
    criteria.setText(primaryEmail);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerWithSameCountry() {
    final String primaryEmail = "carklinik.holy@gmx.at"; // belongs to MATIK-AT
    criteria.setText(primaryEmail);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  @Ignore("ES data has been changed")
  public void givenFreetext_shouldGetCustomerByPrimaryFax() {
    final String primaryFax = "+4304227 3564";
    criteria.setText(primaryFax);
    criteria
        .setAffiliates(Arrays.asList(SupportedAffiliate.MATIK_AT.getCompanyName().toLowerCase()));
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  @Ignore("not yet have ES test data to run, ask Simon #1485")
  public void givenFreetext_shouldGetCustomerByTaxNr() {
    final String taxNr = "72372265";
    criteria
        .setAffiliates(Arrays.asList(SupportedAffiliate.MATIK_AT.getCompanyName().toLowerCase()));
    criteria.setText(taxNr);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  @Ignore("Data is not ready")
  public void givenFreetext_shouldGetCustomerByTaxExemptCode() {
    final String taxExemptCode = "ATU63970929"; // custNr = 1100070
    criteria.setAffiliates(Arrays.asList(SupportedAffiliate.DERENDINGER_AT
      .getCompanyName().toLowerCase()));
    criteria.setText(taxExemptCode);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  @Ignore("not yet have ES test data to run, ask Simon #1485")
  public void givenFreetext_shouldGetCustomerByOfficalRegistrationCode() {
    final String officalRegCode = "72372265";
    criteria.setText(officalRegCode);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByContactsFName() {
    final String contactFistName = "Zsolt";
    criteria.setText(contactFistName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByContactsLName() {
    final String contactLastName = "Nyikos";
    criteria.setText(contactLastName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAddressIdLocation() {
    final String idLocation = "8060000000011434624";
    criteria
        .setAffiliates(Arrays.asList(SupportedAffiliate.MATIK_AT.getCompanyName().toLowerCase()));
    criteria.setText(idLocation);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAddressBuildingComp() {
    final String buildingComp = "KFZ-Meisterbetrieb";
    criteria.setText(buildingComp);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldNotGetCustomerByVietnamAddress() {
    final String buildingComp = "Saigon";
    criteria.setText(buildingComp);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(false));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAddressDesc() {
    final String addressDesc = "Car-Klinik";
    criteria.setText(addressDesc);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAddressStreet() {
    final String addressStreet = "Mauthausner";
    criteria.setText(addressStreet);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAddressCity() {
    final String addressCity = "Gusen";
    criteria.setText(addressCity);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAddressZip() {
    final String addressZip = "4222";
    criteria
        .setAffiliates(Arrays.asList(SupportedAffiliate.MATIK_AT.getCompanyName().toLowerCase()));
    criteria.setText(addressZip);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive1() {
    final String aliasName = "SALBRI";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive2() {
    final String aliasName = "Salbri";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive3() {
    final String aliasName = "SalBri";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive4() {
    final String aliasName = "ferwie";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive5() {
    final String aliasName = "FEUWIE";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive6() {
    final String aliasName = "eisKLA";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive7() {
    final String aliasName = "ÖSTBAD";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive8() {
    final String aliasName = "NK2gra";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive9() {
    final String aliasName = "unsst.";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive10() {
    final String aliasName = "ch-GRA";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive11() {
    final String aliasName = "A24003";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive12() {
    final String aliasName = "k&pKLA";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenFreetext_shouldGetCustomerByAliasNameCaseInsensitive13() {
    final String aliasName = "öamgra";
    criteria.setText(aliasName);
    final Page<CustomerDoc> cust = customerService.searchCustomerByFreetext(criteria, pageable);
    Assert.assertThat(cust.hasContent(), Matchers.is(true));
  }

}
