package com.sagag.services.mdm.user.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.mdm.api.impl.DvseUserServiceImpl;
import com.sagag.services.mdm.client.MdmClient;
import com.sagag.services.mdm.client.MdmResponseException;
import com.sagag.services.mdm.client.MdmUpdateMode;
import com.sagag.services.mdm.dto.DvseCustomer;
import com.sagag.services.mdm.dto.DvseCustomerUser;
import com.sagag.services.mdm.dto.DvseCustomerUserInfo;
import com.sagag.services.mdm.dto.DvseMainModule;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Class to verify DVSE user service in mockup.
 *
 */
@EshopMockitoJUnitRunner
public class DvseUserServiceImplTest {

  @InjectMocks
  private DvseUserServiceImpl dvseService;

  @Mock
  private MdmClient client;

  @Mock
  private DvseCustomer customer;

  private String mdmUsername;

  private String mdmPassword;

  private String mdmTemplateCustomerId;

  private String sessionId = StringUtils.EMPTY;

  @Before
  public void init() {
    when(client.getSessionId(mdmUsername, mdmPassword)).thenReturn(sessionId);
    when(client.invalidateSessionId(sessionId)).thenReturn(true);
    ReflectionTestUtils.setField(dvseService, "catalogUri", "https://web1.dvse.de/loginh.aspx?SID=435001");
  }

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreateCustomer() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, mdmTemplateCustomerId)).thenReturn(customer);
    when(client.createOrDeleteCustomer(anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class)))
        .thenReturn(expectedCustomerId);

    final String customerId =
        dvseService.createCustomer(StringUtils.EMPTY, SupportedAffiliate.DERENDINGER_AT);

    Assert.assertEquals(expectedCustomerId, customerId);

    verify(client, times(1)).getCustomer(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomer(
        anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateCustomerWithNullCustomer() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, mdmTemplateCustomerId)).thenReturn(null);
    when(client.createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE))
        .thenReturn(expectedCustomerId);

    dvseService.createCustomer(StringUtils.EMPTY, SupportedAffiliate.DERENDINGER_AT);

    verify(client, times(1)).getCustomer(sessionId, mdmTemplateCustomerId);
    verify(client, times(0)).createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE);
  }

  @Test
  public void testRemoveCustomer() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, expectedCustomerId)).thenReturn(customer);
    when(client.createOrDeleteCustomer(anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class)))
        .thenReturn(expectedCustomerId);

    dvseService.removeCustomer(expectedCustomerId);

    verify(client, times(1)).getCustomer(sessionId, expectedCustomerId);
    verify(client, times(1)).createOrDeleteCustomer(
        anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class));
  }

  @Test(expected = NoSuchElementException.class)
  public void testRemoveCustomerWithNotExistingCustomer() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, mdmTemplateCustomerId)).thenReturn(null);
    when(client.createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE))
        .thenReturn(expectedCustomerId);

    dvseService.createCustomer(StringUtils.EMPTY, SupportedAffiliate.DERENDINGER_AT);

    verify(client, times(1)).getCustomer(sessionId, mdmTemplateCustomerId);
    verify(client, times(0)).createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE);
  }

  @Test
  @Ignore("NoSuchElementException")
  public void testCreateUser() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Arrays.asList(buildDummyDvseCustomerUser()));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    DvseCustomerUserInfo info = dvseService.createUser(expectedCustomerId, username, password,
        SupportedAffiliate.DERENDINGER_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getUsername());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateUserWithNotFoundTemplateUser() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Collections.emptyList());
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    DvseCustomerUserInfo info = dvseService.createUser(expectedCustomerId, username, password,
        SupportedAffiliate.DERENDINGER_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getUsername());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateUserWithEmptyModules() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    final DvseCustomerUser cusUser = buildDummyDvseCustomerUser();
    cusUser.setModules(Collections.emptyList());

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Arrays.asList(cusUser));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    DvseCustomerUserInfo info = dvseService.createUser(expectedCustomerId, username, password,
        SupportedAffiliate.DERENDINGER_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getUsername());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test(expected = MdmResponseException.class)
  @Ignore("NoSuchElementException")
  public void testCreateUserWithNoCustomerUserInfosResponse() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Arrays.asList(buildDummyDvseCustomerUser()));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Collections.emptyList());

    DvseCustomerUserInfo info = dvseService.createUser(expectedCustomerId, username, password,
        SupportedAffiliate.DERENDINGER_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getPassword());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test
  public void testRemoveUser() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = "test";
    final String password = "test";

    when(client.getCustomer(sessionId, expectedCustomerId))
        .thenReturn(buildDummyCustomer(username, password));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.DELETE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    dvseService.removeUser(expectedCustomerId, username, password);

    verify(client, times(1)).getCustomer(sessionId, expectedCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.DELETE));
  }

  @Test
  public void givenNotFoundExistingUserShouldNotCallMdmDeleteService() {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = "test";
    final String password = "test";

    when(client.getCustomer(sessionId, expectedCustomerId))
        .thenReturn(buildDummyCustomer("test1", "test"));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.DELETE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    dvseService.removeUser(expectedCustomerId, username, password);

    verify(client, times(1)).getCustomer(sessionId, expectedCustomerId);
    verify(client, times(0)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.DELETE));
  }

  @Test
  public void testGetDvseCatalogLink() throws Exception {
    final String username = "test";
    final String password = "test";

    final String uri = dvseService.getDvseCatalogUri(SupportedAffiliate.DERENDINGER_AT, username, password, "12");

    Assert.assertThat(uri, Matchers.containsString(username));
    Assert.assertThat(uri, Matchers.containsString(password));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetDvseCatalogLinkWithEmptyUsername() throws Exception {

    final String username = StringUtils.EMPTY;
    final String password = "test";

    dvseService.getDvseCatalogUri(SupportedAffiliate.DERENDINGER_AT, username, password, "12");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetDvseCatalogLinkWithEmptyPassword() throws Exception {

    final String username = "test";
    final String password = StringUtils.EMPTY;

    dvseService.getDvseCatalogUri(SupportedAffiliate.DERENDINGER_AT, username, password, "12");
  }

  private DvseCustomerUser buildDummyDvseCustomerUser() {
    DvseCustomerUser customerUser = new DvseCustomerUser();
    customerUser.setSeqNumber("1");

    DvseMainModule module = new DvseMainModule();
    module.setId("-1");
    customerUser.setModules(Arrays.asList(module));
    return customerUser;
  }

  private DvseCustomerUserInfo buildDummyCustomerUserInfo(String username, String password) {
    DvseCustomerUserInfo info = new DvseCustomerUserInfo();
    info.setUsername(username);
    info.setPassword(password);
    return info;
  }

  private DvseCustomer buildDummyCustomer(String username, String password) {
    final DvseCustomer customer = new DvseCustomer();
    DvseCustomerUserInfo userInfo = new DvseCustomerUserInfo();
    userInfo.setUsername(username);
    userInfo.setPassword(password);
    customer.setUsersInfos(Arrays.asList(userInfo));
    return customer;
  }

  @Test
  public void testCreateCustomerMatik() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, mdmTemplateCustomerId)).thenReturn(customer);
    when(client.createOrDeleteCustomer(anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class)))
        .thenReturn(expectedCustomerId);

    final String customerId =
        dvseService.createCustomer(StringUtils.EMPTY, SupportedAffiliate.MATIK_AT);

    Assert.assertEquals(expectedCustomerId, customerId);

    verify(client, times(1)).getCustomer(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomer(
        anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateCustomerWithNullCustomerMatik() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, mdmTemplateCustomerId)).thenReturn(null);
    when(client.createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE))
        .thenReturn(expectedCustomerId);

    dvseService.createCustomer(StringUtils.EMPTY, SupportedAffiliate.MATIK_AT);

    verify(client, times(1)).getCustomer(sessionId, mdmTemplateCustomerId);
    verify(client, times(0)).createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE);
  }

  @Test
  public void testRemoveCustomerMatik() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, expectedCustomerId)).thenReturn(customer);
    when(client.createOrDeleteCustomer(anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class)))
        .thenReturn(expectedCustomerId);

    dvseService.removeCustomer(expectedCustomerId);

    verify(client, times(1)).getCustomer(sessionId, expectedCustomerId);
    verify(client, times(1)).createOrDeleteCustomer(
        anyString(), any(DvseCustomer.class), any(MdmUpdateMode.class));
  }

  @Test(expected = NoSuchElementException.class)
  public void testRemoveCustomerWithNotExistingCustomerMatik() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";

    when(client.getCustomer(sessionId, mdmTemplateCustomerId)).thenReturn(null);
    when(client.createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE))
        .thenReturn(expectedCustomerId);

    dvseService.createCustomer(StringUtils.EMPTY, SupportedAffiliate.MATIK_AT);

    verify(client, times(1)).getCustomer(sessionId, mdmTemplateCustomerId);
    verify(client, times(0)).createOrDeleteCustomer(sessionId, customer, MdmUpdateMode.CREATE);
  }

  @Test
  @Ignore("NoSuchElementException")
  public void testCreateUserMatik() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Arrays.asList(buildDummyDvseCustomerUser()));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    DvseCustomerUserInfo info =
        dvseService.createUser(expectedCustomerId, username, password, SupportedAffiliate.MATIK_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getUsername());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateUserMatikWithNotFoundTemplateUser() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Collections.emptyList());
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    DvseCustomerUserInfo info =
        dvseService.createUser(expectedCustomerId, username, password, SupportedAffiliate.MATIK_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getUsername());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateUserMatikWithEmptyModules() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    final DvseCustomerUser cusUser = buildDummyDvseCustomerUser();
    cusUser.setModules(Collections.emptyList());

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Arrays.asList(cusUser));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Arrays.asList(buildDummyCustomerUserInfo(username, password)));

    DvseCustomerUserInfo info =
        dvseService.createUser(expectedCustomerId, username, password, SupportedAffiliate.MATIK_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getUsername());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

  @Test(expected = MdmResponseException.class)
  @Ignore("NoSuchElementException")
  public void testCreateUserMatikWithNoCustomerUserInfosResponse() throws Exception {

    final String expectedCustomerId = "CUSTOMER_ID_01";
    final String username = anyString();
    final String password = anyString();

    when(client.getCustomerUsers(sessionId, mdmTemplateCustomerId))
        .thenReturn(Arrays.asList(buildDummyDvseCustomerUser()));
    when(client.createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE)))
            .thenReturn(Collections.emptyList());

    DvseCustomerUserInfo info =
        dvseService.createUser(expectedCustomerId, username, password, SupportedAffiliate.MATIK_AT);

    Assert.assertEquals(username, info.getUsername());
    Assert.assertEquals(password, info.getPassword());

    verify(client, times(1)).getCustomerUsers(sessionId, mdmTemplateCustomerId);
    verify(client, times(1)).createOrDeleteCustomerUsers(eq(sessionId), anyString(),
        anyList(), eq(MdmUpdateMode.CREATE));
  }

}
