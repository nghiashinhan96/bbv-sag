package com.sagag.services.mdm.user.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.mdm.MdmDataTestUtils;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.mdm.app.MdmApplication;
import com.sagag.services.mdm.client.MdmResponseException;
import com.sagag.services.mdm.dto.DvseCustomerUserInfo;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Class to verify DVSE user service in real time.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MdmApplication.class})
@EshopIntegrationTest
@Slf4j
@Ignore
public class DvseUserServiceImplIT {

  @Autowired
  private DvseUserService dvseUserService;

  @Before
  public void init() {}

  @Test
  public void testCreateCustomerSuccessful() throws Exception {

    final String customerName = MdmDataTestUtils.randomString(5);

    final String customerId = dvseUserService.createCustomer(customerName, SupportedAffiliate.MATIK_CH);
    log.info("{} - {}", customerId, customerName);
    Assert.assertNotNull(customerId);
  }

  @Test(expected = MdmResponseException.class)
  public void testCreateCustomerFailed() throws Exception {

    final String customerName = MdmDataTestUtils.randomString(6);

    dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);
  }

  @Test(expected = MdmResponseException.class)
  public void testCreateCustomerFailedWithEmptyCustomerName() throws Exception {

    final String customerName = StringUtils.EMPTY;

    dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);
  }

  @Test
  public void testRemoveCustomerSuccessful() throws Exception {

    final String customerName = MdmDataTestUtils.randomString(5);

    final String customerId = dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertNotNull(customerId);

    dvseUserService.removeCustomer(customerId);
  }

  @Test(expected = MdmResponseException.class)
  public void testRemoveCustomerFailedWithEmptyCustomerId() throws Exception {

    final String customerName = MdmDataTestUtils.randomString(5);

    final String customerId = dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertNotNull(customerId);

    dvseUserService.removeCustomer(StringUtils.EMPTY);
  }

  @Test
  public void testCreateUserSuccessful() throws Exception {

    final String username = MdmDataTestUtils.randomString(8);
    final String password = MdmDataTestUtils.randomString(8);

    final String customerId = "1410831";

    Assert.assertNotNull(customerId);

    final DvseCustomerUserInfo userInfo = dvseUserService.createUser(customerId, username, password, SupportedAffiliate.MATIK_CH);
    log.info("{} - {}", username, password);
    Assert.assertThat(userInfo.getUsername(), Matchers.equalToIgnoringCase(username));
    Assert.assertThat(userInfo.getPassword(), Matchers.equalTo(password));
  }

  @Test(expected = MdmResponseException.class)
  public void testCreateUserFailedWithEmptyUsername() throws Exception {

    final String customerName = MdmDataTestUtils.randomString(5);
    final String username = MdmDataTestUtils.randomString(8);
    final String password = MdmDataTestUtils.randomString(8);

    final String customerId = dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertNotNull(customerId);

    final DvseCustomerUserInfo userInfo = dvseUserService.createUser(customerId, StringUtils.EMPTY, password, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertThat(userInfo.getUsername(), Matchers.equalTo(username));
    Assert.assertThat(userInfo.getPassword(), Matchers.equalTo(password));
  }

  @Test
  public void testRemoveUserSuccessful() throws Exception {
    final String customerName = MdmDataTestUtils.randomString(5);
    final String username = MdmDataTestUtils.randomString(8);
    final String password = MdmDataTestUtils.randomString(8);

    final String customerId = dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertNotNull(customerId);

    final DvseCustomerUserInfo userInfo = dvseUserService.createUser(customerId, username, password, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertThat(userInfo.getUsername(), Matchers.equalToIgnoringCase(username));
    Assert.assertThat(userInfo.getPassword(), Matchers.equalTo(password));

    dvseUserService.removeUser(customerId, username, password);
  }

  @Test
  public void givenEmptyUsernameShouldNotCallMdmDeletionService() throws Exception {
    final String customerName = MdmDataTestUtils.randomString(5);
    final String username = MdmDataTestUtils.randomString(8);
    final String password = MdmDataTestUtils.randomString(8);

    final String customerId =
        dvseUserService.createCustomer(customerName, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertNotNull(customerId);

    final DvseCustomerUserInfo userInfo =
        dvseUserService.createUser(customerId, username, password, SupportedAffiliate.DERENDINGER_AT);
    Assert.assertThat(userInfo.getUsername(), Matchers.equalToIgnoringCase(username));
    Assert.assertThat(userInfo.getPassword(), Matchers.equalTo(password));

    dvseUserService.removeUser(customerId, StringUtils.EMPTY, password);
  }

  @Test
  public void testGetDvseCatalogLink() throws Exception {

    final String username = "32B9C495F1F8B91D";
    final String password = "7104FEBAF7561A73";

    final String uri = dvseUserService.getDvseCatalogUri(SupportedAffiliate.DERENDINGER_AT, username, password, "12");

    Assert.assertThat(uri, Matchers.containsString(username));
    Assert.assertThat(uri, Matchers.containsString(password));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetDvseCatalogLinkWithEmptyUsername() throws Exception {

    final String username = StringUtils.EMPTY;
    final String password = "7104FEBAF7561A73";
    dvseUserService.getDvseCatalogUri(SupportedAffiliate.DERENDINGER_AT, username, password, "12");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetDvseCatalogLinkWithEmptyPassword() throws Exception {

    final String username = "32B9C495F1F8B91D";
    final String password = StringUtils.EMPTY;

    dvseUserService.getDvseCatalogUri(SupportedAffiliate.DERENDINGER_AT, username, password, "12");
  }

  @Test
  public void existDvseUsername_shouldReturnTrue_givenExistedDvseUsername() throws Exception {
    boolean isExisted = dvseUserService.existDvseUsername("841832", "1F255CF3F5337E5F");
    Assert.assertTrue(isExisted);
  }

  @Test
  public void existDvseUsername_shouldReturnFalse_givenNotExistedDvseUsername() throws Exception {
    boolean isExisted = dvseUserService.existDvseUsername("841832", "24E1FC2F4F148C00");
    Assert.assertFalse(isExisted);
  }

  @Test
  public void existDvseCustomerId_shouldReturnFalse_givenNotExistedDvseCustomerId() throws Exception {
    boolean isExisted = dvseUserService.existDvseCustomerId("1000000");
    Assert.assertFalse(isExisted);
  }
}
