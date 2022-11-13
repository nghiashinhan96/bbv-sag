package com.sagag.services.service.exporter;

import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.exception.ExportException;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IT for Shopping basket content report.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Just verify at local machine instead CI/CD instance")
public class BasketExporterIT {

  private static final String DEST_DIR = "D:/dev/export_result";

  private static final SupportedExportType WORD_TYPE = SupportedExportType.WORD;

  private static final SupportedExportType RTF_TYPE = SupportedExportType.RTF;

  @Autowired
  private BasketExporter basketExporter;

  private static UserInfo user;

  private static EshopContext eshopContext;

  @Before
  public void init() {
    user = new UserInfo();

    Customer customer = new Customer();
    customer.setCurrency("EUR");
    customer.setNr(1111101L);
    customer.setDefaultBranchId("1001");
    customer.setName("Reini Landtechnik");
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);

    OwnSettings settings = new OwnSettings();
    Map<String, String> affSettings = new HashMap<>();
    affSettings.put(SettingsKeys.Affiliate.Settings.DEFAULT_VAT_RATE.toLowerName(), "20");
    settings.setAffSettings(affSettings);

    eshopContext = new EshopContext();
    final EshopBasketContext basketContext = new EshopBasketContext();
    basketContext.setDeliveryType(DeliveryTypeDto.builder().descCode("tour").build());
    basketContext.setPaymentMethod(PaymentMethodDto.builder().descCode("credit").build());
    eshopContext.setEshopBasketContext(basketContext);

    final ShoppingCartItem item = new ShoppingCartItem();
    final ArticleDocDto articleItem = new ArticleDocDto();
    articleItem.setArtnrDisplay("LX 1006/1D");
    articleItem.setFreetextDisplayDesc("Luftfilter MAHLE KNECHT LX 1006/1D");
    articleItem.setAmountNumber(1);

    item.setArticle(articleItem);
    List<ShoppingCartItem> listItem = new ArrayList<>();
    listItem.add(item);
  }

  @Test
  public void testExportAsRtf_With_Locale_DE() throws IOException, ExportException {
    user.setLanguage("de");
    writeByteArrayToFile(basketExporter.export(user, eshopContext, false, RTF_TYPE));
  }

  @Test
  public void testExportAsWord_With_Locale_DE() throws IOException, ExportException {
    user.setLanguage("de");
    writeByteArrayToFile(basketExporter.export(user, eshopContext, false, WORD_TYPE));
  }
  
  @Test
  public void testExportAsWord_With_Locale_IT() throws IOException, ExportException {
    user.setLanguage("it");
    writeByteArrayToFile(basketExporter.export(user, eshopContext, false, WORD_TYPE));
  }
  
  @Test
  public void testExportAsRtf_With_Locale_IT() throws IOException, ExportException {
    user.setLanguage("it");
    writeByteArrayToFile(basketExporter.export(user, eshopContext, false, RTF_TYPE));
  }
  
  @Test
  public void testExportAsWord_With_Locale_FR() throws IOException, ExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(basketExporter.export(user, eshopContext, false, WORD_TYPE));
  }

  @Test
  public void testExportAsRtf_With_Locale_FR() throws IOException, ExportException {
    user.setLanguage("fr");
    writeByteArrayToFile(basketExporter.export(user, eshopContext, false, RTF_TYPE));
  }

  private static void writeByteArrayToFile(ExportStreamedResult result)
      throws IOException, ExportException {
    final File exportedFile = new File(DEST_DIR, result.getFileName());
    exportedFile.createNewFile();
    FileUtils.writeByteArrayToFile(exportedFile, result.getContent());
  }
}
