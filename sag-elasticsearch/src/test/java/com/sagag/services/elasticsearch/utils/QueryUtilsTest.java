package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.enums.LanguageCode;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

public class QueryUtilsTest {

  @Test
  public void verifyRemoveNonAlphaChars() {
    Assert.assertThat(QueryUtils.removeNonAlphaChars(StringUtils.EMPTY, true),
        Matchers.is(StringUtils.EMPTY));

    final String input = "TESTsäüößéèàùâêîôûëïç1234/?!@#$%^^";
    String text = QueryUtils.removeNonAlphaChars(input, true);
    Assert.assertThat(text, Matchers.is("TESTsäüößéèàùâêîôûëïç1234"));
  }

  @Test
  public void verifyRemoveNonAlphaCharsCz() {

    LocaleContextHolder.setLocale(new Locale(LanguageCode.CS.getCode()));
    Assert.assertThat(QueryUtils.removeNonAlphaChars(StringUtils.EMPTY, false),
        Matchers.is(StringUtils.EMPTY));

    final String input = "áčďéěíňóřšťúůýžTEST1234/?!@#$%^^";
    String text = QueryUtils.removeNonAlphaChars(input, false);
    Assert.assertThat(text, Matchers.is("áčďéěíňóřšťúůýžTEST1234"));
  }

  @Test
  public void givenStringShouldRemoveEsSpecialCharacter() {

    final String input = "[/\"]abc";
    String text = QueryUtils.removeEsSpecialCharacter(input);
    Assert.assertThat(text, Matchers.is("[]abc"));
  }

  @Test
  public void givenStringShouldRemoveSpace() {

    final String input = "[/ \"]abc";
    String text = QueryUtils.removeSpace(input);
    Assert.assertThat(text, Matchers.is( "[/\"]abc"));
  }
}
