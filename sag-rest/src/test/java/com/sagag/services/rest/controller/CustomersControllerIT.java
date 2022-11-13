package com.sagag.services.rest.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.eshop.service.api.MailService;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.rest.app.RestApplication;
import com.sagag.services.service.request.CustomerSearchForm;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests class for Customer REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore
public class CustomersControllerIT extends AbstractControllerIT {

  private static final int CUST_NR_1100005 = 1100005;

  private static final String DAT_TEL = "0725273257";

  private static final String CUSTOMER_USERS = "/affiliate/customers";

  private static final String CUSTOMER_SEARCH = "/customers/search";

  @Mock
  private MailService mailService;

  private CustomerSearchForm searchForm;

  @Before
  public void initSearchForm() {
    searchForm = new CustomerSearchForm();
    searchForm.setAffiliate(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
  }

  @After
  public void releaseSearchForm() {
    searchForm.setAffiliate(null);
    searchForm.setCustomerNr(null);
    searchForm.setTelephone(null);
    searchForm.setText(null);
    searchForm = null;
  }

  @Test
  @Ignore("runing duplication cause failed")
  public void testCreateRegistrationUserTechnomag() throws Exception {
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setCustomerNumber("4407696");
    userRegistrationDto.setAffiliate("technomag");
    userRegistrationDto.setFirstName("Phong");
    userRegistrationDto.setSurName("Nguyen");
    userRegistrationDto.setEmail("phong.nguyen@bbv.vn");
    userRegistrationDto.setUserName("phong.test");
    userRegistrationDto.setAddress("");
    userRegistrationDto.setOptionalAddress("");
    userRegistrationDto.setPostCode("3185");
    userRegistrationDto.setCity("");
    userRegistrationDto.setPhoneNumber("");
    userRegistrationDto.setFaxNumber("");
    userRegistrationDto.setHomePage("");
    userRegistrationDto.setDescription("");
    performPost(CUSTOMER_USERS + "/register", userRegistrationDto)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
  }

  @Test
  @Ignore("runing duplication cause failed")
  public void testCreateRegistrationUserDerendinger() throws Exception {
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setCustomerNumber("4100155");
    userRegistrationDto.setAffiliate("derendinger-ch");
    userRegistrationDto.setFirstName("Phong");
    userRegistrationDto.setSurName("Nguyen");
    userRegistrationDto.setEmail("phong.nguyen@bbv.vn");
    userRegistrationDto.setUserName("phong.test");
    userRegistrationDto.setAddress("");
    userRegistrationDto.setOptionalAddress("");
    userRegistrationDto.setPostCode("9001");
    userRegistrationDto.setCity("");
    userRegistrationDto.setPhoneNumber("");
    userRegistrationDto.setFaxNumber("");
    userRegistrationDto.setHomePage("");
    userRegistrationDto.setDescription("");

    performPost(CUSTOMER_USERS + "/register", userRegistrationDto)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
  }

  @Test
  public void givenCustomerNr_shouldGetCustomer() throws Exception {
    // "customers/search"
    final String custNr = "1100005";
    searchForm.setCustomerNr(custNr);
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.customer.nr").value(Matchers.is(Integer.valueOf(custNr))))
        .andExpect(jsonPath("$.admin").value(Matchers.is("truong.ax.ad")));
  }

  @Test
  public void givenInvalidCustomerNr_shouldNotGetCustomer() throws Exception {
    final String custNr = "9990436";
    searchForm.setCustomerNr(custNr);
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void givenValidTelephone_shouldGetCustomer() throws Exception {
    searchForm.setTelephone(DAT_TEL);
    callAndAssertResult(searchForm);
  }

  @Test
  public void givenValidTelephone_shouldGetCustomer2() throws Exception {
    searchForm.setTelephone("+4372527 / 3257");
    callAndAssertResult(searchForm);
  }

  @Test
  public void givenValidTelephone_shouldGetCustomer3() throws Exception {
    searchForm.setTelephone("0043725273257");
    callAndAssertResult(searchForm);
  }

  @Test
  public void givenValidTelephone_shouldGetCustomer4() throws Exception {
    searchForm.setTelephone("+43(072527) / 3257");
    callAndAssertResult(searchForm);
  }

  private void callAndAssertResult(final CustomerSearchForm searchForm) throws Exception {
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.customer.nr").value(Matchers.is(CUST_NR_1100005)))
        .andExpect(jsonPath("$.customer.companyName").value(Matchers.is("Aigner R. GesmbH")))
        .andExpect(jsonPath("$.admin").value(Matchers.is("truong.ax.ad")));
  }

  @Test
  public void givenValidTelephone_shouldNotGetCustomer() throws Exception {
    searchForm.setTelephone(DAT_TEL);
    searchForm.setAffiliate(SupportedAffiliate.MATIK_AT.getAffiliate());
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void givenInvalidTelephone_shouldGetTelephoneFormatException() throws Exception {
    searchForm.setTelephone("+457237 / 2265");
    searchForm.setAffiliate(SupportedAffiliate.MATIK_AT.getAffiliate());
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isBadRequest());
  }

  @Test
  public void givenFreetext_shouldGetCustomer() throws Exception {
    searchForm.setText("Aigner R. GesmbH");
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.customer.nr").value(Matchers.is(CUST_NR_1100005)))
        .andExpect(jsonPath("$.admin").value(Matchers.is("truong.ax.ad")));
  }

  @Test
  public void givenFreetext_shouldNotGetCustomer() throws Exception {
    searchForm.setText("Saigon");
    performPost(CUSTOMER_SEARCH, searchForm).andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }
}
