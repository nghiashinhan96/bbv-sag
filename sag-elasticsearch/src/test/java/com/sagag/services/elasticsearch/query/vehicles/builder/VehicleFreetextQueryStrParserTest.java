package com.sagag.services.elasticsearch.query.vehicles.builder;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VehicleFreetextQueryStrParserTest {

  private static final String EXPECTED = "1.5i OR 1,5i";

  @Test
  public void testParseQueryContainEngineCode_Freetext() {

    final Map<String, String> testStrMap = new HashMap<>();
    testStrMap.put("audi 1,5i", "audi 1,5i OR 1.5i");
    testStrMap.put(StringUtils.SPACE, StringUtils.EMPTY);
    testStrMap.put("15", "15");
    testStrMap.put("1.5i", EXPECTED);
    testStrMap.put("1,5i", "1,5i OR 1.5i");
    testStrMap.put("1.5i OR 1,5i", "1.5i OR 1,5i");

    testStrMap.forEach((actual, expected) -> testAndAssertion(actual, expected));

  }

  public void testAndAssertion(String query, String expectedResult) {
    String actual = VehicleFreetextQueryStrParser.parseQueryContainEngineCode(query);
    Assert.assertThat(actual, Matchers.equalTo(expectedResult));
  }

}
