package com.sagag.services.elasticsearch.utils;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for FreeTextNormalizeStringUtils.
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeTextNormalizeStringUtilsTest {

  @Test
  public void givenFreetext_ShouldNormalizeInput() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.normalizeFreetext("osram 12Watt BA9S 4000");
    Assert
        .assertThat(
            normalizedQuery,
            Matchers
            .is("((osram AND (12Watt OR (12 AND Watt)) AND BA9S AND 4000) OR (osram12Watt)) OR (osram12WattBA9S4000)"));
  }

  @Test
  public void givenFreetext_ShouldNormalizeInput2() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.normalizeFreetext("oc 90 Oilfilter");
    Assert.assertThat(normalizedQuery,
        Matchers.is("((oc AND 90 AND Oilfilter) OR (oc90)) OR (oc90Oilfilter)"));
  }

  @Test
  public void givenFreetext_ShouldNormalizeInput3() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.normalizeFreetext("Handseife");
    Assert.assertThat(normalizedQuery, Matchers.is("Handseife"));
  }

  @Test
  public void givenFreetext_ShouldNormalizeInput4() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.normalizeFreetext("oc");
    Assert.assertThat(normalizedQuery, Matchers.is("oc"));
  }

  @Test
  public void givenFreetext_ShouldNormalizeInput5() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.normalizeFreetext("FDB 1888");
    Assert.assertThat(normalizedQuery, Matchers.is("(FDB AND 1888) OR (FDB1888)"));
  }

  @Test
  public void givenFreetext_ShouldNormalizeInput6() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.normalizeFreetext("0 220 880 678");
    Assert.assertThat(normalizedQuery,
        Matchers.is("((0 AND 220 AND 880 AND 678) OR (0220)) OR (0220880678)"));
  }

  @Test
  public void givenFreetext_ShouldhandleExcludedChar1() {
    final String normalizedQuery
        = FreeTextNormalizeStringUtils.handleExcludedChar("mit Radiokontroll-Funktion");
    Assert.assertThat(normalizedQuery, Matchers.is(
        "((mit AND RadiokontrollFunktion) OR (mitRadiokontrollFunktion)) " +
          "OR (mit Radiokontroll-Funktion) OR (MIT RADIOKONTROLL-FUNKTION)"));
  }

  @Test
  public void givenFreetext_ShouldhandleExcludedChar2() {
    final String normalizedQuery = FreeTextNormalizeStringUtils.handleExcludedChar("U.200-10-06");
    Assert.assertThat(normalizedQuery, Matchers.is("(U2001006) OR (U.200-10-06)"));
  }

  @Test
  public void givenVehicleFullNameShouldNormalizeText1() {
    final String vehFullName = "PASSAT";
    Assert.assertThat(FreeTextNormalizeStringUtils.normalizeVehicleFullName(vehFullName),
        Matchers.is("(PASSAT) OR (PASSAT*) OR (*PASSAT) OR (*PASSAT*)"));
  }

  @Test
  public void givenVehicleFullNameShouldNormalizeText2() {
    final String vehFullName = "VW PASSAT";
    Assert.assertThat(FreeTextNormalizeStringUtils.normalizeVehicleFullName(vehFullName),
        Matchers.is("(VW PASSAT) OR (*VW* AND *PASSAT*)"));
  }

  @Test
  public void givenVehicleFullNameShouldNormalizeText3() {
    final String vehFullName = "VW PASSAT CXDA";
    Assert.assertThat(FreeTextNormalizeStringUtils.normalizeVehicleFullName(vehFullName),
        Matchers.is("(VW PASSAT CXDA) OR (*VW* AND *PASSAT* AND *CXDA*)"));
  }

  @Test
  public void givenVehicleFullNameShouldNormalizeText4() {
    final String vehFullName = StringUtils.EMPTY;
    Assert.assertThat(FreeTextNormalizeStringUtils.normalizeVehicleFullName(vehFullName),
        Matchers.is(StringUtils.EMPTY));
  }

  @Test
  public void givenVehicleFullNameShouldNormalizeText5() {
    final String vehFullName = "VW PASSAT*";
    Assert.assertThat(FreeTextNormalizeStringUtils.normalizeVehicleFullName(vehFullName),
        Matchers.is("VW PASSAT*"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("6000628381");
    Assert.assertThat(resultText, Matchers.is("6000628381"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText2() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("006000628381");
    Assert.assertThat(resultText, Matchers.is("(006000628381) OR (6000628381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText3() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("60006A28381");
    Assert.assertThat(resultText, Matchers.is("((60006A28381 OR (60006 AND A28381))) OR (60006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText4() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("0060006A28381");
    Assert.assertThat(resultText,
        Matchers.is("((0060006A28381 OR (0060006 AND A28381))) OR ((60006A28381 " +
          "OR (60006 AND A28381))) OR (0060006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText5() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("60*006A28381");
    Assert.assertThat(resultText, Matchers.is("60*006A28381"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText6() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("0060*006A28381");
    Assert.assertThat(resultText, Matchers.is("(0060*006A28381) OR (60*006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText7() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("00060-006A2*8381");
    Assert.assertThat(resultText,
        Matchers.is("(00060006A2*8381) OR (60006A2*8381) OR (00060-006A2*8381) OR (60-006A2*8381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText8() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("60-006A28381");
    Assert.assertThat(resultText, Matchers.is("((60006A28381 OR (60006 AND A28381))) OR (60-006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText9() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("60.006A28381");
    Assert.assertThat(resultText, Matchers.is("((60006A28381 OR (60006 AND A28381))) OR (60.006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText10() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("00060-006A28381");
    Assert.assertThat(resultText,
        Matchers
            .is("((00060006A28381 OR (00060006 AND A28381))) OR ((60006A28381 OR (60006 AND A28381))) OR "
                + "(00060-006A28381) OR (60-006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText11() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("00060.006A28381");
    Assert.assertThat(resultText,
        Matchers
            .is("((00060006A28381 OR (00060006 AND A28381))) OR ((60006A28381 OR (60006 AND A28381))) OR "
                + "(00060.006A28381) OR (60.006A28381)"));
  }

  @Test
  public void givenHandleExcludedChar_ShouldHandleInputText12() {
    final String resultText = FreeTextNormalizeStringUtils.handleExcludedChar("00060.006A2*8381");
    Assert.assertThat(resultText,
        Matchers.is("(00060006A2*8381) OR (60006A2*8381) OR (00060.006A2*8381) OR (60.006A2*8381)"));
  }

  @Test
  public void givenTextWithNonAlpha_ShouldBuildPerfectMatchCondition() {
    final String resultText = FreeTextNormalizeStringUtils.buildPerfectMatchCondition(
        "ocAz<äüößéèàùâêîôûëïç1234\\/?!@#$%^^? @ [ ] _ ´ ’ + => ® ° ² À Ä Ç É È Ö ØÜ");
    Assert.assertThat(resultText, Matchers.is("ocAz1234"));

  }
}
