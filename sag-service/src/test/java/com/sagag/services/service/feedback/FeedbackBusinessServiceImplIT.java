package com.sagag.services.service.feedback;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.api.FeedbackBusinessService;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;
import com.sagag.services.service.dto.feedback.FeedbackDataItem;
import com.sagag.services.service.dto.feedback.FeedbackMessageContentDto;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.dto.feedback.FeedbackTopicDto;
import com.sagag.services.service.request.FeedbackMessageRequest;
import com.sagag.services.service.resource.FeedbackMasterDataResource;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Integration test class for FeedbackBusinessService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore
public class FeedbackBusinessServiceImplIT {

  @Autowired
  private FeedbackBusinessService feedbackService;

  @Test
  public void create_shouldCreateFeedBackAndSendEmail_givenCriteria() throws Exception {
    UserInfo userInfo = buildUserInfo();
    FeedbackMessageRequest criteria = buildFeedbackMessageRequest();
    feedbackService.createCustomerFeedback(userInfo, criteria);
  }

  @Test
  public void getMasterData_shouldGetMasterData_givenUserInfo() throws Exception {
    FeedbackMasterDataResource<FeedBackCustomerUserDataDto> masterData =
        feedbackService.getFeedbackCustomerUserData(buildUserInfo());
    List<String> topicCodes = masterData.getTopicCodes();
    assertThat(topicCodes.size(), Matchers.is(8));
    assertThat(topicCodes.get(0), Matchers.is("PRICING"));

    FeedBackCustomerUserDataDto userData = masterData.getUserData();
    assertThat(userData.getCustomerNr(), Matchers.is(4130675L));
  }


  private UserInfo buildUserInfo() {
    ContactInfo phone = ContactInfo.builder().type("Phone").value("123456789").build();
    ContactInfo email = ContactInfo.builder().type("Email").value("danhnguyen@bbv.ch").build();

    Customer customer = Customer.builder().emailContacts(Arrays.asList(email))
        .phoneContacts(Arrays.asList(phone)).city("HCM city").nr(4130675).build();
    UserInfo userInfo = new UserInfo();
    userInfo.setId(2L);
    userInfo.setEmail("danhnguyen@bbv.ch");
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(customer);
    userInfo.setSettings(ownSettings);
    userInfo.setRoles(Arrays.asList("NORMAL_USER"));
    userInfo.setAffiliateShortName("derendinger-at");
    return userInfo;
  }


  private FeedbackMessageRequest buildFeedbackMessageRequest() {
    FeedbackMessageContentDto message = FeedbackMessageContentDto.builder()
        .title("Feedback-Nachricht").content("I can not use the free text search").build();
    FeedbackMessageRequest request = new FeedbackMessageRequest();
    FeedbackTopicDto topic = FeedbackTopicDto.builder().title("Feedback-Thema").topic("Preise")
        .topicCode("PRICING").build();
    request.setTopic(topic);
    request.setAffiliateStore("d-store");
    request.setMessage(message);
    request.setTechnicalData(buildTechnicalData());
    request.setSalesInfo(buildSalesData());
    request.setUserData(buidUserData());
    return request;
  }

  private FeedbackParentDataItem buildSalesData() {
    FeedbackDataItem salesInfoItem = FeedbackDataItem.builder().title("Verkäufer").key("SALES_DATA")
        .value("20097 - danhnguyen@bbv.ch").build();
    FeedbackParentDataItem salesInfo = FeedbackParentDataItem.builder().title("Verkäuferdaten")
        .items(Arrays.asList(salesInfoItem)).build();
    return salesInfo;
  }

  private FeedbackParentDataItem buidUserData() {
    FeedbackDataItem userDataEmail =
        FeedbackDataItem.builder().title("Email").key("EMAIL").value("danhnguyen@bbv.ch").build();
    FeedbackDataItem userDataPhone =
        FeedbackDataItem.builder().title("Phone").key("PHONE").value("0123445677").build();

    FeedbackDataItem defaultBranch = FeedbackDataItem.builder().title("Branch")
        .key("DEFAULT_BRANCH").value("derendinger-at").build();

    FeedbackDataItem customerInfo = FeedbackDataItem.builder().title("Customer")
        .key("CUSTOMER_INFO").value("4130675, n/a, HCM city").build();

    FeedbackParentDataItem userdata = FeedbackParentDataItem.builder().title("Kundendaten")
        .items(Arrays.asList(customerInfo, userDataEmail, userDataPhone, defaultBranch)).build();
    return userdata;
  }

  private FeedbackParentDataItem buildTechnicalData() {
    FeedbackDataItem websiteItem = FeedbackDataItem.builder().key("WEBSITE").title("Website")
        .value("Home page").isShortTechnicalData(true).build();

    FeedbackDataItem vehicleSearchItem = FeedbackDataItem.builder().key("VEHICLE_SEARCH_FIELDS")
        .title("Vehicle search fields").value("VIN: zfa19800004361472 , Typenschein : 1TA491")
        .isShortTechnicalData(true).build();

    FeedbackDataItem invoiceTypeSetting = FeedbackDataItem.builder().key("INVOICE_TYPE")
        .title("Invoice Type").value("Invoice type 1").build();
    FeedbackDataItem deliveryAddressSetting = FeedbackDataItem.builder().key("DELIVERY_ADDRESS")
        .title("Delivery address").value("Anna building").build();
    FeedbackDataItem settingItem =
        FeedbackDataItem.builder().childs(Arrays.asList(invoiceTypeSetting, deliveryAddressSetting))
            .key("customerSettings").title("Customer settings").build();

    FeedbackParentDataItem technicalItem =
        FeedbackParentDataItem.builder().title("Technische Daten")
            .items(Arrays.asList(websiteItem, vehicleSearchItem, settingItem)).build();
    return technicalItem;
  }
}
