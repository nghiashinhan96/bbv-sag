package com.sagag.services.elasticsearch.parser;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class VehicleFreetextStringParserTest {

  @InjectMocks
  private VehicleFreetextStringParser parser;

  @Test
  public void test() {
    testAndAssert("ARS11/2\"", Arrays.asList(Matchers.is("ARS11 2")));
  }

  private void testAndAssert(String text, List<Matcher<String>> matchers) {
    String actual = parser.apply(text, null);
    for (Matcher<String> matcher : matchers) {
      Assert.assertThat(actual, matcher);
    }
  }

}
