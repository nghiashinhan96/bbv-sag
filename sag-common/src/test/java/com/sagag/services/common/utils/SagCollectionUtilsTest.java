package com.sagag.services.common.utils;

import com.sagag.services.common.domain.country.CustomCountryValueConfigProperties;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SagCollectionUtilsTest {

  @Test
  public void shouldDistinctByKeys() {
    // Given
    final String erpType = "type";
    final CustomCountryValueConfigProperties country1 = new CustomCountryValueConfigProperties();
    country1.setErpType(erpType);
    final CustomCountryValueConfigProperties country2 = new CustomCountryValueConfigProperties();
    country2.setErpType(erpType);
    final List<CustomCountryValueConfigProperties> countries = new ArrayList<>();
    countries.add(country1);
    countries.add(country2);
    // When
    List<CustomCountryValueConfigProperties> result = countries.stream()
        .filter(SagCollectionUtils.distinctByKeys(CustomCountryValueConfigProperties::getErpType))
        .collect(Collectors.toList());
    // Then
    Assertions.assertThat(result.size()).isEqualTo(1);
    Assertions.assertThat(result.get(0).getErpType()).isEqualTo(erpType);
  }
}
