package com.sagag.services.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.services.admin.app.AdminApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AdminApplication.class })
@EshopIntegrationTest @Ignore
public class AdminUserControllerIT extends AbstractControllerIT {

  @Autowired
  AdminUserController backOfficeUserController;

  @Test
  public void testUpdatePassword_WrongFormat() throws Exception {
    EshopUserLoginDto eshopUserLoginDto = new EshopUserLoginDto();
    eshopUserLoginDto.setId(13L);
    eshopUserLoginDto.setPassword("123");
    eshopUserLoginDto.setRedirectUrl("");
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/back-office/user/password/update").content(asJsonString(eshopUserLoginDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(),
        CoreMatchers.containsString("Password wrong format"));
  }

  @Test
  public void testUpdatePassword_DirectLinkempty() throws Exception {
    EshopUserLoginDto eshopUserLoginDto = new EshopUserLoginDto();
    eshopUserLoginDto.setId(13L);
    eshopUserLoginDto.setPassword("123456@A");
    eshopUserLoginDto.setRedirectUrl("");
    getMockMvc()
        .perform(
            post("/back-office/user/password/update").content(asJsonString(eshopUserLoginDto))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
  }

  @Test
  public void testUpdatePassword_Success() throws Exception {
    EshopUserLoginDto eshopUserLoginDto = new EshopUserLoginDto();
    eshopUserLoginDto.setId(13L);
    eshopUserLoginDto.setPassword("123456@A");
    eshopUserLoginDto.setRedirectUrl("http:abc.com");
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/back-office/user/password/update").content(asJsonString(eshopUserLoginDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(),
        CoreMatchers.containsString("Update password successfully"));
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

  @Test
  public void testupdateUserSettings_UserNameWrong1() throws Exception {
    BackOfficeUserSettingDto backOfficeUserSettingDto =
        BackOfficeUserSettingDto.builder().userId(13).userName("").build();
    getMockMvc().perform(
        post("/back-office/user/settings/update").content(asJsonString(backOfficeUserSettingDto))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().is4xxClientError());
  }

  @Test
  public void testupdateUserSettings_FirstNameWrong() throws Exception {
    BackOfficeUserSettingDto backOfficeUserSettingDto =
        BackOfficeUserSettingDto.builder().userId(13).userName("nu1.gc").build();
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/back-office/user/settings/update")
                    .content(asJsonString(backOfficeUserSettingDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError()).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(),
        CoreMatchers.containsString("Surname is not valid"));
  }

  @Test
  public void testupdateUserSettings_DuplicateUserName() throws Exception {
    BackOfficeUserSettingDto backOfficeUserSettingDto =
        BackOfficeUserSettingDto.builder().userId(13).userName("nu1.gc").firstName("An")
            .email("123@").build();
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/back-office/user/settings/update")
                    .content(asJsonString(backOfficeUserSettingDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError()).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(), CoreMatchers
        .containsString("User name is not allowed the duplication in the same affiliate"));
  }

  @Test
  public void testupdateUserSettings_EMailNotValid() throws Exception {
    BackOfficeUserSettingDto backOfficeUserSettingDto =
        BackOfficeUserSettingDto.builder().userId(13).userName("nu12.gc").firstName("An")
            .email("123@").build();
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/back-office/user/settings/update")
                    .content(asJsonString(backOfficeUserSettingDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError()).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(),
        CoreMatchers.containsString("Email is not valid"));
  }

  @Test
  public void testupdateUserSettings_Success() throws Exception {
    BackOfficeUserSettingDto backOfficeUserSettingDto =
        BackOfficeUserSettingDto.builder().userId(13).userName("nu11.gc").firstName("An")
            .email("an.nguyen@bbv.vn").emailNotificationOrder(true).netPriceConfirm(true)
            .netPriceView(false).build();
    MvcResult mvcResult =
        getMockMvc()
            .perform(
                post("/back-office/user/settings/update")
                    .content(asJsonString(backOfficeUserSettingDto))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
    Assert.assertThat(mvcResult.getResponse().getContentAsString(),
        CoreMatchers.containsString("Update UserSetting success"));
  }

  @Test
  public void testGetUserSettings() throws Exception {
   getMockMvc().perform(
        get("/back-office/user/13/settings")
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(content().json("{companyName: Garage-C, orgCode: '469743'}"));

  }

}
