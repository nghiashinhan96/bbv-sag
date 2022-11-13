package com.sagag.services.admin.controller;

import com.sagag.services.admin.app.AdminApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AdminApplication.class })
@EshopIntegrationTest @Ignore
public class AffiliatesControllerIT extends AbstractControllerIT {

  private static final String ADMIN_AFFILIATE_ROOT_URL = "/admin/affiliates";

  @Test
  public void testCreateAffiliateAnonymousForbidden() throws Exception {
    // @formatter:off
    getMockMvc().perform(
                  post(ADMIN_AFFILIATE_ROOT_URL + "/create")
                  .accept(MediaType.APPLICATION_JSON_UTF8))
                  .andExpect(status().isUnauthorized())
                  .andExpect(jsonPath("$.error", is("unauthorized")));
  }

  @Test
  public void testCreateAffiliateAuthorized() throws Exception {
    final String accessToken = obtainAdminWebAccessToken("admin", "123", StringUtils.EMPTY);
    // @formatter:off
    getMockMvc().perform(
                  post(ADMIN_AFFILIATE_ROOT_URL + "/create")
                  .accept(MediaType.APPLICATION_JSON_UTF8)
                  .header("Authorization", "Bearer " + accessToken))
                  .andExpect(status().isOk());
  }

  @Test @Ignore
  public void testCreateAffiliateRoleUserAccessDenied() throws Exception {
    // @formatter:off
    final String accessToken = obtainWebAccessToken("ga.admin", "123", AFFI_TECH);
    getMockMvc().perform(
                  post(ADMIN_AFFILIATE_ROOT_URL + "/create")
                  .accept(MediaType.APPLICATION_JSON_UTF8)
                  .header("Authorization", "Bearer " + accessToken)
                  .param(PARAM_AFFILIATE, AFFI_TECH))
                  .andExpect(status().isForbidden())
                  .andExpect(jsonPath("$.error", is("access_denied")));
  }

}
