package com.sagag.services.admin.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class) @Ignore
public class AffiliatesControllerTest {

  private static final String ADMIN_AFFILIATE_ROOT_URL = "/admin/affiliates";

  @InjectMocks
  private AdminAffiliatesController affiliatesController;

  private MockMvc mvc;

  @Before
  public void setUp() throws Exception {
    mvc = standaloneSetup(affiliatesController).build();
  }

  @Test
  public void testAffiliateUpdateNotFound() throws Exception {
    mvc.perform(get("/update")).andExpect(status().isNotFound());
  }

  @Test
  public void testAffiliateUpdateMethodNotAllowed() throws Exception {
    mvc.perform(get(ADMIN_AFFILIATE_ROOT_URL + "/update")).andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void testAffiliateUpdateSuccessful() throws Exception {
    mvc.perform(put(ADMIN_AFFILIATE_ROOT_URL + "/update").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
  }

  @Test
  public void testAffiliateCreateNotFound() throws Exception {
    mvc.perform(get("/create")).andExpect(status().isNotFound());
  }

  @Test
  public void testAffiliateCreateMethodNotAllowed() throws Exception {
    mvc.perform(get(ADMIN_AFFILIATE_ROOT_URL + "/create")).andExpect(status().isMethodNotAllowed());
  }

  @Test
  public void testAffiliateCreateSuccessful() throws Exception {
    mvc.perform(post(ADMIN_AFFILIATE_ROOT_URL + "/create").accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
  }
}
