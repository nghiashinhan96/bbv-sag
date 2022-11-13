package com.sagag.services.mdm.client;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.mdm.MdmDataTestUtils;
import com.sagag.services.mdm.app.MdmApplication;
import com.sagag.services.mdm.dto.DvseCustomer;
import com.sagag.services.mdm.dto.DvseCustomerSearchResult;
import com.sagag.services.mdm.dto.DvseCustomerUser;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

/**
 * Class to verify DVSE user client web services in real time.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { MdmApplication.class })
@EshopIntegrationTest
public class MdmClientIT {

  @Autowired
  private MdmClient mdmClient;

  @Value("${external.webservice.dvse.mdm_username}")
  private String mdmUsername;

  @Value("${external.webservice.dvse.mdm_password}")
  private String mdmPassword;

  @Value("${external.webservice.dvse.mdm_template_customer_id}")
  private String templateCustomerId;

  @Before
  public void init() {
  }

  private String getSessionId(String username, String password) {
    return mdmClient.getSessionId(mdmUsername, mdmPassword);
  }

  @Test
  public void testGetSessionIdSuccessful() throws Exception {

    final String sessionId = mdmClient.getSessionId(mdmUsername, mdmPassword);

    Assert.assertNotNull(sessionId);
  }

  @Test(expected = MdmResponseException.class)
  public void testGetSessionIdFailed() throws Exception {

    final String password = StringUtils.EMPTY;

    final String sessionId = mdmClient.getSessionId(mdmUsername, password);

    Assert.assertNotNull(sessionId);
  }

  @Test
  public void testInvalidateSessionIdSuccessful() throws Exception {

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    final boolean isSuccess = mdmClient.invalidateSessionId(sessionId);

    Assert.assertEquals(true, isSuccess);
  }

  @Test(expected = MdmResponseException.class)
  public void testInvalidateSessionIdFailed() throws Exception {

    final String sessionId = StringUtils.EMPTY;

    mdmClient.invalidateSessionId(sessionId);
  }

  @Test
  public void testGetCustomerSuccessful() throws Exception {

    final String expectedCustomerId = "649556";

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    final DvseCustomer customer = mdmClient.getCustomer(sessionId, templateCustomerId);

    Assert.assertNotNull(customer);
    Assert.assertEquals(expectedCustomerId, customer.getCustomerId());
  }

  @Test(expected = MdmResponseException.class)
  public void testGetCustomerFailed() throws Exception {

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    mdmClient.getCustomer(sessionId, StringUtils.EMPTY);
  }

  @Ignore
  @Test
  public void testGetCustomerUsersSuccessful() throws Exception {

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    List<DvseCustomerUser> users = mdmClient.getCustomerUsers(sessionId, templateCustomerId);

    Assert.assertNotNull(users);
    Assert.assertThat(1, Matchers.greaterThanOrEqualTo(users.size()));
  }

  @Test(expected = MdmResponseException.class)
  public void testGetCustomerUsersFailed() throws Exception {

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    mdmClient.getCustomerUsers(sessionId, StringUtils.EMPTY);
  }

  @Ignore
  @Test
  public void testCreateOrDeleteCustomerSuccessful() throws Exception {

    // You will generate a name for the new customer upfront and store it in the database.
    // We normally use randomly generated hex string (length 10).
    final String customerName = MdmDataTestUtils.randomString(5);

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    final DvseCustomer templateCustomer =
        mdmClient.getCustomer(sessionId, templateCustomerId);

    final DvseCustomer newCustomer = new DvseCustomer();
    newCustomer.setCustomerName(customerName);
    newCustomer.setTraderId(templateCustomer.getTraderId());
    newCustomer.setModules(templateCustomer.getModules());
    final String customerId =
        mdmClient.createOrDeleteCustomer(sessionId, newCustomer, MdmUpdateMode.CREATE);

    Assert.assertNotNull(customerId);

    final DvseCustomer customer = mdmClient.getCustomer(sessionId, customerId);

    Assert.assertThat(customer.getCustomerName(), Matchers.equalTo(customerName));

  }

  @Ignore
  @Test(expected = MdmResponseException.class)
  public void testCreateOrDeleteCustomerFailed() throws Exception {

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    mdmClient.createOrDeleteCustomer(sessionId, new DvseCustomer(), MdmUpdateMode.CREATE);
  }

  @Ignore
  @Test(expected = MdmResponseException.class)
  public void testCreateOrDeleteCustomerUsersFailed() throws Exception {

    final String sessionId = getSessionId(mdmUsername, mdmPassword);

    mdmClient.createOrDeleteCustomerUsers(sessionId, StringUtils.EMPTY, Collections.emptyList() , MdmUpdateMode.CREATE);
  }

  @Ignore
  @Test
  public void getCustomerSearchResult_shouldReturnSearchResults_givenExistedUserName() throws Exception {
    final String dvseUsername = "af6aa08bf74f486e";
    final String sessionId = getSessionId(mdmUsername, mdmPassword);
    List<DvseCustomerSearchResult> res = mdmClient.getCustomerSearchResult(sessionId, "792", "3", dvseUsername);
    Assert.assertThat(res.size(), Matchers.is(1));
    Assert.assertThat(res.get(0).getColumn3(), Matchers.is(dvseUsername));
    System.out.println(res.size());
  }
}
