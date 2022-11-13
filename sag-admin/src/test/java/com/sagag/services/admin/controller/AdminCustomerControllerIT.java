package com.sagag.services.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.services.admin.app.AdminApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.eshop.criteria.CustomerProfileSearchCriteria;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AdminApplication.class })
@EshopIntegrationTest @Ignore
public class AdminCustomerControllerIT extends AbstractControllerIT {

  @Test
  public void testGetCustomerInfo_NotFound() throws Exception {
    CustomerProfileSearchCriteria eshopUserLoginDto =
        CustomerProfileSearchCriteria.builder().affiliate("technomag").companyName(AFFI_TECH)
            .customerNr("469743").build();
    getMockMvc().perform(
        post("/admin/customers/info").content(asJsonString(eshopUserLoginDto))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().is4xxClientError());
  }

  @Test
  public void testGetCustomerInfo_Success() throws Exception {
    CustomerProfileSearchCriteria eshopUserLoginDto =
        CustomerProfileSearchCriteria.builder().affiliate("derendinger-ch")
            .companyName(AFFI_DEREND).customerNr("469743").build();
    getMockMvc()
        .perform(
            post("/admin/customers/info").content(asJsonString(eshopUserLoginDto))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json("{nr: 469743, language: 'DE'}"))
        .andReturn();
  }

  @Test
  public void testUpdate_Setting() throws Exception {
    CustomerSettingsBODto eshopUserLoginDto = new CustomerSettingsBODto();
    eshopUserLoginDto.setId(6);
    eshopUserLoginDto.setAllocationId(1);
    eshopUserLoginDto.setDeliveryId(1);
    eshopUserLoginDto.setCollectiveDelivery(1);
    eshopUserLoginDto.setPaymentId(1);
    eshopUserLoginDto.setSessionTimeoutSeconds(300);
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/admin/customers/update/settings").content(asJsonString(eshopUserLoginDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(),
        CoreMatchers.containsString("Update setting successfully"));
  }


  private static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (JsonProcessingException ex) {
      return null;
    }
  }
}
