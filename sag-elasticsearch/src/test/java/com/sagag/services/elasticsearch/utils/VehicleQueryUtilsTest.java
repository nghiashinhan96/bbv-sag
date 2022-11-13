package com.sagag.services.elasticsearch.utils;

import com.sagag.services.elasticsearch.query.vehicles.VehicleQueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class VehicleQueryUtilsTest {

  @Test
  public void shouldVerifyParseBuiltYearMothStringAllSuccessCases() {
    final String[] yearMonths = { "02-2018", "11-18", "12/2018", "11/18", "201801" };
    Optional<String> result;
    for (String str : yearMonths) {
      log.debug("date string = {}", str);
      result = VehicleQueryUtils.parseBuiltYearMonthString(str);
      log.debug("result = {}", result);
      Assert.assertThat(result.isPresent(), Matchers.is(true));
    }
  }

  @Test
  public void shouldVerifyParseBuiltYearStringAllSuccessCases() {
    final String[] yearMonths = { "2018", "18"};
    Optional<String> result;
    for (String str : yearMonths) {
      log.debug("date string = {}", str);
      result = VehicleQueryUtils.parseBuiltYearString(str);
      log.debug("result = {}", result);
      Assert.assertThat(result.isPresent(), Matchers.is(true));
    }
  }

  @Test
  public void shouldVerifyParseBuiltYearMothStringAllFailedCases() {
    final String[] yearMonths =
        { StringUtils.EMPTY, "518", "31-2018", "11-128", "28/2018", "31/18", "201813" };

    Optional<String> result;
    for (String str : yearMonths) {
      result = VehicleQueryUtils.parseBuiltYearMonthString(str);
      Assert.assertThat(result.isPresent(), Matchers.is(false));
    }
  }

  @Test
  public void shouldExtractQueryVehicleName_Empty() {
    final String value = StringUtils.EMPTY;
    final String actual = VehicleQueryUtils.extractQueryVehicleName().apply(value);
    Assert.assertThat(actual.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldExtractQueryVehicleName_VW_PAS_85() {
    final String value = "VW PAS 85";
    final String actual = VehicleQueryUtils.extractQueryVehicleName().apply(value);
    Assert.assertThat(actual, Matchers.is("VW PAS* 85"));
  }

  @Test
  public void shouldExtractQueryVehicleName_peu_106_sport() {
    final String value = "peu 106 sport";
    final String actual = VehicleQueryUtils.extractQueryVehicleName().apply(value);
    Assert.assertThat(actual, Matchers.is("PEU* 106 SPORT*"));
  }

  @Test
  public void analyzeVehPowerTest() {
    final List<String> testCases = Arrays.asList("123","123PS");
    final String expected = "(123) OR (123PS)";
    Assert.assertEquals(expected, VehicleQueryUtils.analyzePowers().apply(testCases));
  }
}
