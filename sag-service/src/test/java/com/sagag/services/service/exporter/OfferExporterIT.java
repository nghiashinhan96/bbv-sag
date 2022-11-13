package com.sagag.services.service.exporter;

import com.sagag.eshop.repo.api.offer.OfferRepository;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.ax.utils.AxAddressUtils;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.exception.OfferExportException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IT for Offer Report.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
@Ignore("Just verify at local machine instead CI/CD instance")
public class OfferExporterIT {

  private static final String DEST_DIR = "C:/dev/export_result";

  private static final long OFFER_ID = 25L; // offer number is 123

  private static final SupportedExportType WORD_TYPE = SupportedExportType.WORD;

  private static final SupportedExportType PDF_TYPE = SupportedExportType.PDF;

  private static final SupportedExportType RTF_TYPE = SupportedExportType.RTF;

  @Autowired
  private OfferRepository offerRepo;

  @Autowired
  private OfferExporter offerExporter;

  private static OfferDto offer;

  private static UserInfo user;

  @Before
  public void init() {
    File file = new File(DEST_DIR);
    if (!file.exists()) {
      file.mkdirs();
    }
    offer = new OfferDto(offerRepo.findById(OFFER_ID).orElse(null));
    log.debug("Offer info = {}", SagJSONUtil.convertObjectToPrettyJson(offer));
    user = new UserInfo();
    user.setOrganisationId(16);

    // set dummy address
    Address addresse = new Address();
    addresse.setAddressTypeCode("DEFAULT");
    addresse.setCompanyName("Addr companyName CH");
    addresse.setStreet("street 1");
    addresse.setCity("Zurich");
    addresse.setCountryDesc("country desc CH");
    addresse.setPostCode("post code");
    List<Address> addresses = new ArrayList<>();
    addresses.add(addresse);
    user.setAddresses(addresses);

    // set dummy customer
    List<ContactInfo> emailContacts = new ArrayList<>();
    emailContacts.add(new ContactInfo("abc@gmail.com", null, AxAddressUtils.EMAIL_CONTACT_TYPE, true));
    List<ContactInfo> phoneContacts = new ArrayList<>();
    phoneContacts.add(new ContactInfo("+8411111111", null, AxAddressUtils.PHONE_CONTACT_TYPE, false));
    phoneContacts.add(new ContactInfo("+8400000000", null, AxAddressUtils.PHONE_CONTACT_TYPE, true));
    List<ContactInfo> faxContacts = new ArrayList<>();
    faxContacts.add(new ContactInfo("+8422222222", null, AxAddressUtils.FAX_CONTACT_TYPE, true));
    Customer customer = new Customer();
    customer.setFaxContacts(faxContacts);
    customer.setPhoneContacts(phoneContacts);
    customer.setEmailContacts(emailContacts);
    customer.setCompanyName("CompanyName CH");
    customer.setVatNr("123 332");
    customer.setCurrency("CHF");
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
  }

  @Test
  public void testExportAsWord_With_Locale_DE() throws IOException, OfferExportException {
    user.setLanguage("de");
    writeByteArrayToFile(offerExporter.export(user, offer, WORD_TYPE));
  }
  
  @Test
  public void testExportAsPdf_With_Locale_DE() throws IOException, OfferExportException {
    user.setLanguage("de");
    writeByteArrayToFile(offerExporter.export(user, offer, PDF_TYPE));
  }

  @Test
  public void testExportAsRtf_With_Locale_DE() throws IOException, OfferExportException {
    user.setLanguage("de");
    writeByteArrayToFile(offerExporter.export(user, offer, RTF_TYPE));
  }
  
  @Test
  public void testExportAsWord_With_Locale_IT() throws IOException, OfferExportException {
    user.setLanguage("it");
    writeByteArrayToFile(offerExporter.export(user, offer, WORD_TYPE));
  }

  @Test
  public void testExportAsPdf_With_Locale_IT() throws IOException, OfferExportException {
    user.setLanguage("it");
    writeByteArrayToFile(offerExporter.export(user, offer, PDF_TYPE));
  }

  @Test
  public void testExportAsRtf_With_Locale_IT() throws IOException, OfferExportException {
    user.setLanguage("it");
    writeByteArrayToFile(offerExporter.export(user, offer, RTF_TYPE));
  }
  
  @Test
  public void testExportAsWord_With_Locale_FR() throws IOException, OfferExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(offerExporter.export(user, offer, WORD_TYPE));
  }

  @Test
  public void testExportAsPdf_With_Locale_FR() throws IOException, OfferExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(offerExporter.export(user, offer, PDF_TYPE));
  }

  @Test
  public void testExportAsRtf_With_Locale_FR() throws IOException, OfferExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(offerExporter.export(user, offer, RTF_TYPE));
  }

  private static void writeByteArrayToFile(ExportStreamedResult result)
      throws IOException, OfferExportException {
    final File exportedFile = new File(DEST_DIR, result.getFileName());
    exportedFile.createNewFile();
    FileUtils.writeByteArrayToFile(exportedFile, result.getContent());
  }


  @Test
  public void testExportAsPdf_ExtendInfo_With_Locale_DE() throws IOException, OfferExportException {
    user.setLanguage("de");
    writeByteArrayToFile(offerExporter.export(user, offer, PDF_TYPE));
  }

  @Test
  public void testExportAsRtf_With_ExtendInfo_Locale_DE() throws IOException, OfferExportException {
    user.setLanguage("de");
    writeByteArrayToFile(offerExporter.export(user, offer, RTF_TYPE));
  }

  @Test
  public void testExportAsPdf_ExtendInfo_With_Locale_FR() throws IOException, OfferExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(offerExporter.export(user, offer, PDF_TYPE));
  }

  @Test
  public void testExportAsRtf_With_ExtendInfo_Locale_FR() throws IOException, OfferExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(offerExporter.export(user, offer, RTF_TYPE));
  }

  @Test
  public void testExportAsPdf_ExtendInfo_With_Locale_IT() throws IOException, OfferExportException {
    user.setLanguage("it");
    writeByteArrayToFile(offerExporter.export(user, offer, PDF_TYPE));
  }

  @Test
  public void testExportAsRtf_With_ExtendInfo_Locale_IT() throws IOException, OfferExportException {
    user.setLanguage("it");
    writeByteArrayToFile(offerExporter.export(user, offer, RTF_TYPE));
  }

}
