package com.sagag.services.rest.controller;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.CustomerSettingsDto;
import com.sagag.services.rest.app.RestApplication;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests class for Customer settings REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class CustomerSettingsControllerIT extends AbstractControllerIT {

  private static final String ROOT_URL = "/customer/settings";

  @Test
  public void testEditDefaultSettingsIsForbiddenWithB2BUser() throws Exception {
    final boolean allowViewNetPrice = true;
    final boolean allowViewBilling = true;
    final boolean allowSendConfirmEmail = true;

    CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(allowViewNetPrice);
    customerSettingsDto.setNetPriceConfirm(allowSendConfirmEmail);
    customerSettingsDto.setViewBilling(allowViewBilling);

    getMockMvc()
        .perform(put(ROOT_URL + "/default/edit").contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, bearerToken())
            .content(new ObjectMapper().writeValueAsString(customerSettingsDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testEditDefaultSettingsIsSuccessWithUserAdmin() throws Exception {
    final boolean allowViewNetPrice = false;
    final boolean allowViewBilling = true;
    final boolean allowSendConfirmEmail = true;

    CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(allowViewNetPrice);
    customerSettingsDto.setNetPriceConfirm(allowSendConfirmEmail);
    customerSettingsDto.setViewBilling(allowViewBilling);

    getMockMvc()
        .perform(put(ROOT_URL + "/default/edit").contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, bearerToken())
            .content(new ObjectMapper().writeValueAsString(customerSettingsDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.netPriceView", Matchers.equalTo(allowViewNetPrice)))
        .andExpect(jsonPath("$.viewBilling", Matchers.equalTo(allowViewBilling)))
        .andExpect(jsonPath("$.netPriceConfirm", Matchers.equalTo(allowSendConfirmEmail)));
  }

  @Test
  public void testEditDefaultSettingsIsSuccessWithGroupAdmin() throws Exception {
    final boolean allowViewNetPrice = false;
    final boolean allowViewBilling = true;
    final boolean allowSendConfirmEmail = true;

    CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(allowViewNetPrice);
    customerSettingsDto.setNetPriceConfirm(allowSendConfirmEmail);
    customerSettingsDto.setViewBilling(allowViewBilling);

    getMockMvc()
        .perform(put(ROOT_URL + "/default/edit").contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, bearerToken())
            .content(new ObjectMapper().writeValueAsString(customerSettingsDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.netPriceView", Matchers.equalTo(allowViewNetPrice)))
        .andExpect(jsonPath("$.viewBilling", Matchers.equalTo(allowViewBilling)))
        .andExpect(jsonPath("$.netPriceConfirm", Matchers.equalTo(allowSendConfirmEmail)));
  }

  @Test
  public void testEditDefaultSettingsIsSuccessWithSystemAdmin() throws Exception {
    final boolean allowViewNetPrice = false;
    final boolean allowViewBilling = true;
    final boolean allowSendConfirmEmail = true;

    CustomerSettingsDto customerSettingsDto = new CustomerSettingsDto();
    customerSettingsDto.setNetPriceView(allowViewNetPrice);
    customerSettingsDto.setNetPriceConfirm(allowSendConfirmEmail);
    customerSettingsDto.setViewBilling(allowViewBilling);

    getMockMvc()
        .perform(put(ROOT_URL + "/default/edit").contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, bearerToken())
            .content(new ObjectMapper().writeValueAsString(customerSettingsDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.netPriceView", Matchers.equalTo(allowViewNetPrice)))
        .andExpect(jsonPath("$.viewBilling", Matchers.equalTo(allowViewBilling)))
        .andExpect(jsonPath("$.netPriceConfirm", Matchers.equalTo(allowSendConfirmEmail)));
  }

  @Test
  public void testGetCustomerSettings() throws Exception {
    performGet(ROOT_URL + "/default/").andExpect(status().isOk());
  }

  @Test
  public void testGetCustomerSettingsUnauthorized() throws Exception {
    performGet(ROOT_URL + "/default/").andExpect(status().isForbidden());
  }

  @Test
  public void testGetCustomerSettingsFailed() throws Exception {
    performGet(ROOT_URL + "/default/").andExpect(status().isUnauthorized());
  }

}
