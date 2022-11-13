package com.sagag.services.service.feedback;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackDepartmentContactRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackStatusRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackTopicRepository;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.dto.feedback.FeedbackDataItem;
import com.sagag.services.service.dto.feedback.FeedbackMessageContentDto;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.dto.feedback.FeedbackSourceDto;
import com.sagag.services.service.dto.feedback.FeedbackTopicDto;
import com.sagag.services.service.mail.feedback.FeedbackMessageMailSenderFactory;
import com.sagag.services.service.request.FeedbackMessageRequest;

import org.mockito.Mock;

import java.util.Arrays;


public abstract class AbstractFeedbackHandlerTest {

  @Mock
  protected EshopUserRepository eshopUserRepository;

  @Mock
  protected FeedbackTopicRepository feedbackTopicRepo;

  @Mock
  protected VUserDetailRepository vUserDetailRepo;

  @Mock
  protected FeedbackStatusRepository feedbackStatusRepo;

  @Mock
  protected FeedbackRepository feedbackRepo;

  @Mock
  protected FeedbackDepartmentContactRepository feedbackDepartmentContactRepo;

  @Mock
  protected SupportedAffiliateRepository supportedAffiliateRepo;

  @Mock
  protected FeedbackMessageMailSenderFactory feedbackMessageMailSenderFactory;

  protected UserInfo buildUserInfo() {
    ContactInfo phone = ContactInfo.builder().type("Phone").value("123456789").build();
    ContactInfo email = ContactInfo.builder().type("Email").value("danhnguyen1@bbv.ch").build();

    Customer customer = Customer.builder().emailContacts(Arrays.asList(email))
        .phoneContacts(Arrays.asList(phone)).city("HCM city").nr(4130675).build();
    UserInfo userInfo = new UserInfo();
    userInfo.setId(2L);
    userInfo.setEmail("danhnguyen1@bbv.ch");
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(customer);
    userInfo.setSettings(ownSettings);
    userInfo.setRoles(Arrays.asList("NORMAL_USER"));
    userInfo.setAffiliateShortName("derendinger-at");
    userInfo.setSalesUsername("Sales_01");
    return userInfo;
  }

  protected FeedbackMessageRequest buildFeedbackMessageRequest() {

    FeedbackSourceDto source = FeedbackSourceDto.builder().title("Fehlerquelle")
        .source("Webshop des Kunden").code("SALE_ON_BEHALF").build();

    FeedbackMessageContentDto message = FeedbackMessageContentDto.builder().title("Ihr Feedback")
        .content("I can not use the free text search").build();
    FeedbackTopicDto topic =
        FeedbackTopicDto.builder().topicCode("EASE_OF_USE").title("Ease of Use").build();
    FeedbackMessageRequest request = new FeedbackMessageRequest();

    request.setTopic(topic);
    request.setAffiliateStore("d-store");
    request.setMessage(message);
    request.setTechnicalData(buildTechnicalData());
    request.setUserData(buidUserData());
    request.setSalesInfo(buildSalesData());
    request.setSource(source);
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
        FeedbackDataItem.builder().title("Email").key("EMAIL").value("danhnguyen1@bbv.ch").build();
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
