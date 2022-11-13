package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.entity.Country;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

/**
 * Test class for Country Service.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CountryServiceImplTest {

  @InjectMocks
  private CountryServiceImpl countryServiceImpl;

  @Mock
  private CountryRepository countryRepository;

  @Test
  public void testGetCountriesWithShortCode() {
    Mockito.when(countryRepository.findAllWithShortCode())
        .thenReturn(Collections.singletonList(new Country()));
    countryServiceImpl.getCountriesWithShortCode();
    Mockito.verify(countryRepository, times(1)).findAllWithShortCode();
  }

  @Test
  public void testGetCountries() {
    Mockito.when(countryRepository.findAll()).thenReturn(Collections.singletonList(null));
    countryServiceImpl.getCountries();
    Mockito.verify(countryRepository, times(1)).findAll();
  }

  @Test
  public void testGetCountriesByShortCode() {
    final String at = "at";
    Mockito.when(countryRepository.findByShortCode(Mockito.anyString()))
        .thenReturn(Collections.singletonList(new Country()));
    countryServiceImpl.getCountriesByCode(at);
    Mockito.verify(countryRepository, times(1)).findByShortCode(at);
  }
}
