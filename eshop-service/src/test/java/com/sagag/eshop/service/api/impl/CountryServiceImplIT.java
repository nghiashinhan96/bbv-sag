package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.Country;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.transaction.Transactional;

/**
 * Integration tests for country service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class CountryServiceImplIT {

  @Autowired
  private CountryService countryService;

  @Test
  public void testGetCountriesWithShortCode() {
    final List<Country> countryList = countryService.getCountriesWithShortCode();
    Assert.assertThat(countryList.isEmpty(), Is.is(false));
    Assert.assertThat(countryList.size(), Is.is(2));
  }

  @Test
  public void testGetCountryList() {
    final List<Country> countryList = countryService.getCountries();
    Assert.assertThat(CollectionUtils.isEmpty(countryList), Is.is(false));
  }

  @Test
  public void testGetCountriesByShortCode() {
    final List<Country> countryList = countryService.getCountriesByCode("at");
    Assert.assertThat(countryList.isEmpty(), Is.is(false));
    Assert.assertThat(countryList.size(), Is.is(1));
  }
}
