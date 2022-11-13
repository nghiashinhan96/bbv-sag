package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SagStringUtilsTest {

  @Test
  public void testStripNonAlphaNumericCharacters() {
    final String val = "5G0853677C 9B9";
    Assert.assertThat("5G0853677C9B9", Is.is(SagStringUtils.stripNonAlphaNumericChars(val)));
  }

  @Test
  public void testStripNonAlphaNumericCharactersNegative() {
    final String val = "5G0853677$ 9K9";
    Assert.assertThat("5G08536779K9", Is.is(SagStringUtils.stripNonAlphaNumericChars(val)));
  }

  @Test
  public void testStripNonAlphaNumericCharactersNegative2() {
    final String val = "5G~`!0@853$&^%$#*)(67_+|-/\\][{}7'\"?.=;:,C 9B9";
    Assert.assertThat("5G0853677C9B9", Is.is(SagStringUtils.stripNonAlphaNumericChars(val)));
  }

  @Test
  public void testHandleElasticsearchEmpty() {
    final String val = "";
    Assert.assertThat("null", Is.is(SagStringUtils.handleElasticBlank(val)));
  }

  @Test
  public void testHandleElasticsearchNull() {
    final String val = null;
    Assert.assertThat("null", Is.is(SagStringUtils.handleElasticBlank(val)));
  }

  @Test
  public void testHandleElasticsearchUpperCase() {
    final String val = "CR05";
    Assert.assertThat("CR05", Is.is(SagStringUtils.handleElasticBlank(val)));
  }

  @Test
  public void shouldHaveEmptyWhenNullValue() {
    Assert.assertThat(StringUtils.EMPTY, Is.is(SagStringUtils.toEmptyIfNullValue("null")));
  }

  @Test
  public void shouldKeepOriginalValWhenNotNullValue() {
    final String original = "OC90";
    Assert.assertThat(original, Is.is(SagStringUtils.toEmptyIfNullValue(original)));
  }

  @Test
  public void shouldReturnTrueIfStringBuilderIsNull() {
    Assert.assertThat(SagStringUtils.isEmptyStrBuilder(null), Is.is(true));
  }

  @Test
  public void shouldReturnTrueIfStringBuilderEmpty() {
    Assert.assertThat(SagStringUtils.isEmptyStrBuilder(new StringBuilder()), Is.is(true));
  }

  @Test
  public void shouldReturnFalseIfStringBuilderHasText() {
    Assert.assertThat(SagStringUtils.isEmptyStrBuilder(new StringBuilder(SagConstants.DESC)), Is.is(false));
  }

  @Test
  public void shouldReturnFalseIfStringBuilderContainsSpace() {
    Assert.assertThat(SagStringUtils.isEmptyStrBuilder(new StringBuilder(StringUtils.SPACE)), Is.is(false));
  }

  @Test
  public void containsIgnoreCase_shouldReturnTrue() throws Exception {
    List<String> sources = Arrays.asList("element1", "element2");
    boolean result = SagStringUtils.containsIgnoreCase(sources, "Element1");
    Assert.assertTrue(result);
  }

  @Test
  public void containsIgnoreCase_shouldReturnFalse_givenEmptySources() throws Exception {
    boolean result = SagStringUtils.containsIgnoreCase(Collections.emptyList(), "Element1");
    Assert.assertFalse(result);
  }

  @Test
  public void containsIgnoreCase_shouldReturnFalse_givenTargetIsNull() throws Exception {
    boolean result = SagStringUtils.containsIgnoreCase(Collections.emptyList(), null);
    Assert.assertFalse(result);
  }

}
