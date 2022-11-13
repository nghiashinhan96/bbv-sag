package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Country;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link CountryRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class CountryRepositoryIT {

  private static final String AT = "at";

  @Autowired
  private CountryRepository countryRepository;

  @Test
  public void testfindByShortCode() {
    final String code = AT;
    final List<Country> countries = countryRepository.findByShortCode(code);
    Assert.assertThat(CollectionUtils.isEmpty(countries), Is.is(false));
    Assert.assertThat(countries.size(), Is.is(1));
    Assert.assertThat(countries.get(0).getShortCode(), Is.is(code));
  }

  @Test
  public void testfindByShortCode_NotFound() {
    final List<Country> countries = countryRepository.findByShortCode("abc");
    Assert.assertThat(CollectionUtils.isEmpty(countries), Is.is(true));
  }

  @Test
  public void testfindAllByShortCode() {
    final List<Country> country = countryRepository.findAllWithShortCode();
    Assert.assertThat(country.isEmpty(), Is.is(false));
    Assert.assertThat(country.size(), Is.is(2));
  }

  @Test
  public void testfindIdByShortCode() {
    final List<Integer> ids = countryRepository.findIdsByShortCode(AT);
    Assert.assertThat(ids.isEmpty(), Is.is(false));
    Assert.assertThat(ids.size(), Is.is(1));
  }

  @Test
  public void testFindByShortName() {
    final String shortName = "Austria";
    final Optional<Country> country = countryRepository.findByShortName(shortName);
    Assert.assertThat(country.isPresent(), Is.is(true));
    Assert.assertThat(country.get().getShortName(), Is.is(shortName));
  }

  @Test
  public void testFindByShortName_NotFound() {
    final String shortName = "Austria123";
    final Optional<Country> country = countryRepository.findByShortName(shortName);
    Assert.assertThat(country.isPresent(), Is.is(false));
  }

  @Test
  public void testFindAllByCountryNames() {
    final String countryName = "Austria";
    final List<Country> countries =
        countryRepository.findAllByCountryNames(Collections.singletonList(countryName));
    Assert.assertThat(CollectionUtils.isEmpty(countries), Is.is(false));
    Assert.assertThat(countries.size(), Is.is(1));
    Assert.assertThat(countries.get(0).getShortName(), Is.is(countryName));
  }

  @Test
  public void testFindByCodeIn() {
    List<Country> countries = countryRepository.findByCodeIn(Arrays.asList("AUT", "CHE"));
    for (Country country : countries) {
      log.debug("country = {}", country.getFullName());
    }
  }

  @Test
  public void testFindShortCodeByAffiliate() {
    Optional<String> countryCode = countryRepository.findShortCodeByAffiliate(
        SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    log.debug("countryCode = {}", countryCode);

    countryCode = countryRepository.findShortCodeByAffiliate(
        SupportedAffiliate.DERENDINGER_CH.getAffiliate());
    log.debug("countryCode = {}", countryCode);
  }
}
