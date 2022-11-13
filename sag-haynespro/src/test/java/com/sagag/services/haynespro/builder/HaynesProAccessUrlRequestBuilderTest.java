package com.sagag.services.haynespro.builder;

import com.sagag.services.haynespro.HaynesProDataProvider;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

public class HaynesProAccessUrlRequestBuilderTest {

  @Test
  public void shouldBuildHaynesProAccessUrlRequest() {
    final HaynesProAccessUrlRequestBuilder builder = new HaynesProAccessUrlRequestBuilder(
        HaynesProDataProvider.getHaynesProAccessRequest(Locale.GERMAN.getLanguage()));
    final Map<String, String> result = builder.build();
    Assert.assertThat(result.isEmpty(), Matchers.is(false));
    Assert.assertThat(result.size(), Matchers.is(16));
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPE() {
    new HaynesProAccessUrlRequestBuilder(null).build();
  }
}
