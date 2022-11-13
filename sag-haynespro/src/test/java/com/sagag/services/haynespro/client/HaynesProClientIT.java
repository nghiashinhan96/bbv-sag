package com.sagag.services.haynespro.client;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.haynespro.HaynesProDataProvider;
import com.sagag.services.haynespro.app.HaynesProApplication;
import com.sagag.services.haynespro.domain.RegisterVisitResult;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HaynesProApplication.class })
@EshopIntegrationTest
@Slf4j
public class HaynesProClientIT {

  @Autowired
  private HaynesProClient client;

  @Test(expected = IllegalArgumentException.class)
  public void testGetHaynesProAccessUrl_ThrowIllArgEx() {
    client.getHaynesProAccessUrl(null);
  }

  @Test
  public void testGetHaynesProAccessUrl_DE() {
    RegisterVisitResult result = getHaynesProAccessUrl(Locale.GERMAN.getLanguage());
    Assert.assertThat(result, Matchers.notNullValue());
    assertDetails(result);
  }

  @Test
  public void testGetHaynesProAccessUrl_FR() {
    RegisterVisitResult result = getHaynesProAccessUrl(Locale.FRENCH.getLanguage());
    Assert.assertThat(result, Matchers.notNullValue());
    assertDetails(result);
  }

  @Test
  public void testGetHaynesProAccessUrl_IT() {
    RegisterVisitResult result = getHaynesProAccessUrl(Locale.ITALIAN.getLanguage());
    Assert.assertThat(result, Matchers.notNullValue());
    assertDetails(result);
  }

  private RegisterVisitResult getHaynesProAccessUrl(String lang) {
    final HaynesProAccessUrlRequest request = HaynesProDataProvider.getHaynesProAccessRequest(lang);
    RegisterVisitResult result = client.getHaynesProAccessUrl(request);
    log.debug("Result = {}", SagJSONUtil.convertObjectToPrettyJson(result));
    return result;
  }

  private static void assertDetails(RegisterVisitResult result) {
    Assert.assertThat(result.getCode(), Matchers.is(0));
    Assert.assertThat(result.getMessage(), Matchers.nullValue());
    Assert.assertThat(result.getRedirectUrl(),
      Matchers.startsWith("https://www.workshopdata.com/touch/site/layout/loginWithVrid?vrid="));
  }
}
