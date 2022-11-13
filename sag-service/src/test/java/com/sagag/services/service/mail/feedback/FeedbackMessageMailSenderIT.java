package com.sagag.services.service.mail.feedback;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.service.dto.feedback.FeedbackDataItem;
import com.sagag.services.service.dto.feedback.FeedbackMessageContentDto;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.dto.feedback.FeedbackSourceDto;
import com.sagag.services.service.dto.feedback.FeedbackTopicDto;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
//@Ignore
public class FeedbackMessageMailSenderIT {

  @Autowired
  private FeedbackMessageMailSenderFactory feedbackMessageMailSender;

  @Test
  public void sendMailConfirmationBackToSender_shouldSendEmailToCustomer_givenCriteria()
      throws Exception {
    feedbackMessageMailSender.sendMailConfirmationBackToSender(buildCriteria());
  }

  @Test
  public void sendMailToRecipient_shouldSendEmailToDepartment_givenCriteria() throws Exception {
    feedbackMessageMailSender.sendMailToRecipient(buildCriteria());
  }

  @Test
  public void salesSendMailToRecipient_shouldSendEmailToDepartment_givenCriteria()
      throws Exception {
    feedbackMessageMailSender.salesSendMailToRecipient(buildCriteria());
  }

  @Test
  public void salesSendMailConfirmationBackToSender_shouldSendEmailToCustomer_givenCriteria()
      throws Exception {
    feedbackMessageMailSender.salesSendMailConfirmationBackToSender(buildCriteria());
  }

  @Test
  public void salesSendMailToRecipient_shouldSendEmailToDepartment_givenCriteria_fr()
      throws Exception {
    FeedbackMessageCriteria criteria = buildCriteria();
    Locale locale = new Locale("fr");
    criteria.setLocale(locale);
    feedbackMessageMailSender.salesSendMailToRecipient(criteria);
  }

  @Test
  public void salesSendMailConfirmationBackToSender_shouldSendEmailToCustomer_givenCriteria_fr()
      throws Exception {
    FeedbackMessageCriteria criteria = buildCriteria();
    Locale locale = new Locale("fr");
    criteria.setLocale(locale);
    feedbackMessageMailSender.salesSendMailConfirmationBackToSender(criteria);
  }

  @Test
  public void salesSendMailToRecipient_shouldSendEmailToDepartment_givenCriteria_it()
      throws Exception {
    FeedbackMessageCriteria criteria = buildCriteria();
    Locale locale = new Locale("it");
    criteria.setLocale(locale);
    feedbackMessageMailSender.salesSendMailToRecipient(criteria);
  }

  @Test
  public void salesSendMailConfirmationBackToSender_shouldSendEmailToCustomer_givenCriteria_it()
      throws Exception {
    FeedbackMessageCriteria criteria = buildCriteria();
    Locale locale = new Locale("it");
    criteria.setLocale(locale);
    feedbackMessageMailSender.salesSendMailConfirmationBackToSender(criteria);
  }

  private FeedbackMessageCriteria buildCriteria() {
    String feedbackDepartmentEmail = "danhnguyen@bbv.ch";
    String affiliateEmail = "noreply@technomag.ch";
    String branch = "Technomag";
    FeedbackTopicDto topic = FeedbackTopicDto.builder().title("Feedback-Thema").topic("Preise")
        .topicCode("PRICING").build();
    FeedbackSourceDto source = FeedbackSourceDto.builder().title("Fehlerquelle")
        .source("Webshop Verkauf").code("SALES_NOT_ON_BEHALF").build();
    FeedbackMessageContentDto message = FeedbackMessageContentDto.builder()
        .title("Feedback-Nachricht").content("I can not use the free text search").build();
    Locale locale = new Locale("de");
    FeedbackMessageCriteria criteria = FeedbackMessageCriteria.builder()
        .toEmail(feedbackDepartmentEmail).fromEmail(affiliateEmail).topic(topic).source(source)
        .defaultBranch(branch).feedbackId(1L).message(message).salesInfo(buildSalesData())
        .userData(buidUserData()).technicalData(buildTechnicalData()).locale(locale)
        .subjectParams(ArrayUtils.EMPTY_STRING_ARRAY)
        .build();
    return criteria;
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

    FeedbackDataItem defaultBranch =
        FeedbackDataItem.builder().title("Branch").key("DEFAULT_BRANCH").value("technomag").build();

    FeedbackDataItem customerInfo = FeedbackDataItem.builder().title("Customer")
        .key("CUSTOMER_INFO").value("4130675, n/a, HCM city").build();

    FeedbackParentDataItem userdata = FeedbackParentDataItem.builder().title("Kundendaten")
        .items(Arrays.asList(customerInfo, userDataEmail, userDataPhone, defaultBranch)).build();
    return userdata;
  }

  private FeedbackParentDataItem buildTechnicalData() {
    FeedbackDataItem allowBillingChanged = FeedbackDataItem.builder().key("ALLOW_BILLING_CHANGED")
        .title("Allow view billing changed").build();
    FeedbackDataItem websiteItem =
        FeedbackDataItem.builder().key("WEBSITE").title("Website").value("Home page").build();
    FeedbackDataItem invoiceTypeSetting = FeedbackDataItem.builder().key("INVOICE_TYPE")
        .title("Invoice Type").value("Invoice type 1").build();
    FeedbackDataItem deliveryAddressSetting = FeedbackDataItem.builder().key("DELIVERY_ADDRESS")
        .title("Delivery address").value("Anna building").build();
    FeedbackDataItem settingItem = FeedbackDataItem.builder()
        .childs(Arrays.asList(allowBillingChanged, invoiceTypeSetting, deliveryAddressSetting))
        .key("customerSettings").title("Customer settings").build();

    FeedbackParentDataItem technicalItem = FeedbackParentDataItem.builder()
        .title("Technische Daten").items(Arrays.asList(websiteItem, settingItem)).build();
    return technicalItem;
  }
}
