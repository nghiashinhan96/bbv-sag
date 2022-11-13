package com.sagag.services.rest.controller;

import org.junit.Ignore;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.rest.app.RestApplication;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration tests class for Settings REST APIs.
 *
 * <pre>
 * @thinguyen
 * I marked ignored, because I got the problem when execute IT on Bamboo CI:
 * com.sagag.services.rest.controller.SettingControllerIT  Time elapsed: 0.145 s  <<< ERROR!
 * build	23-Nov-2018 09:35:46	org.springframework.web.client.ResourceAccessException:
 * I/O error on POST request for "https://ax.sib-services.ch/auth-server/oauth/token":
 * sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
 * unable to find valid certification path to requested target; nested exception is javax.net.ssl.SSLHandshakeException:
 * sun.security.validator.ValidatorException: PKIX path building failed:
 * </pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest
@Ignore("When run IT on Bamboo, I got the error")
public class SettingControllerIT extends AbstractControllerIT {

  private static final String PREFIX_SETTINGS_URL = "/settings/";
  private static final String AT_DEFAULT_EMAIL = "shop@derendinger.at";

  @Test
  public void shouldGetSettingsDerendingerAt() throws Exception {
    final String affiliate = SupportedAffiliate.DERENDINGER_AT.getAffiliate();
    performGet(PREFIX_SETTINGS_URL + affiliate).andExpect(status().isOk())
        .andExpect(jsonPath("$.title", Matchers.is("D-store Austria")))
        .andExpect(jsonPath("$.default_email", Matchers.is(AT_DEFAULT_EMAIL)));
  }

  @Test
  public void shouldGetSettingsMatikAt() throws Exception {
    final String affiliate = SupportedAffiliate.MATIK_AT.getAffiliate();
    performGet(PREFIX_SETTINGS_URL + affiliate).andExpect(status().isOk())
        .andExpect(jsonPath("$.title", Matchers.is("Matik Austria")))
        .andExpect(jsonPath("$.default_email", Matchers.is(AT_DEFAULT_EMAIL)));
  }

  @Test
  public void shouldGetSettingsMatikCh() throws Exception {
    final String affiliate = SupportedAffiliate.MATIK_CH.getAffiliate();
    performGet(PREFIX_SETTINGS_URL + affiliate).andExpect(status().isOk())
        .andExpect(jsonPath("$.title", Matchers.is("Matik AG")))
        .andExpect(jsonPath("$.default_email", Matchers.is("info@matik.ch")));
  }

  @Test
  public void shouldGetSettingsTechnomag() throws Exception {
    final String affiliate = SupportedAffiliate.TECHNOMAG.getAffiliate();
    performGet(PREFIX_SETTINGS_URL + affiliate).andExpect(status().isOk())
        .andExpect(jsonPath("$.title", Matchers.is("Techno-store")))
        .andExpect(jsonPath("$.default_email", Matchers.is("eshop@technomag.ch")));
  }

  @Test
  public void shouldGetSettingsDerendingerCh() throws Exception {
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();
    performGet(PREFIX_SETTINGS_URL + affiliate).andExpect(status().isOk())
        .andExpect(jsonPath("$.title", Matchers.is("D-store")))
        .andExpect(jsonPath("$.default_email", Matchers.is("shop@derendinger.ch")));
  }

}
