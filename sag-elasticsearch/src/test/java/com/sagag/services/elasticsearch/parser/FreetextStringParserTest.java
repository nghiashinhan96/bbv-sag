package com.sagag.services.elasticsearch.parser;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class FreetextStringParserTest {

  @InjectMocks
  private FreetextStringParser parser;

  @Test
  public void testParseEmptyString() {
    final List<Matcher<String>> matchers = new ArrayList<>();
    matchers.add(Matchers.isEmptyString());
    testAndAssertFreetextParser(StringUtils.EMPTY, matchers);
  }

  @Test
  public void testParseIdPim() {
    final String idPim = "9001639010";
    final List<Matcher<String>> matchers = new ArrayList<>();
    matchers.add(Matchers.equalTo("9001639010"));
    testAndAssertFreetextParser(idPim, matchers);
  }

  @Test
  public void testPerfectMatch() {
    // Given
    final String freeText = "0 451 103 079";
    final Object[] object = new Object[] { true };
    final String expected = "451103079";
    // When
    String actual = parser.apply(freeText, object);
    // Then
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testDirectMatch() {
    // Given
    final String freeText = "0 451 103 079";
    final Object[] object = new Object[] { true, true };
    final String expected = "0451103079";
    // When
    String actual = parser.apply(freeText, object);
    // Then
    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testParseIdAutonet() {
    final String idAutonet = "10-00-098ASK";
    final List<Matcher<String>> matchers = new ArrayList<>();
    matchers.add(Matchers.equalTo(
          "((1000098ask OR (1000098 AND ask))) OR (10-00-098ask) OR (10-00-098ASK)"));
    testAndAssertFreetextParser(idAutonet, matchers);
  }

  private void testAndAssertFreetextParser(String freetext, List<Matcher<String>> matchers) {
    final String result = parser.apply(freetext, new Object[] { false });
    log.debug("Freetext after normalise = {}", result);
    if (CollectionUtils.isEmpty(matchers)) {
      return;
    }
    for (Matcher<String> matcher : matchers) {
      Assert.assertThat(result, matcher);
    }
  }
}
