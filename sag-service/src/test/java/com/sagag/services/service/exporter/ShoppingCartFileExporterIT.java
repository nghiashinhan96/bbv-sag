package com.sagag.services.service.exporter;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartCsvExporter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

/**
 * IT for ShoppingCartFileExporter.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Ignore("Just verify at local machine instead CI/CD instance")
public class ShoppingCartFileExporterIT {

  @Autowired
  private ShoppingCartCsvExporter exporter;

  @Test(expected = IllegalArgumentException.class)
  public void testExportCsv() throws Exception {
    exporter.exportCsv(Collections.emptyList());
  }

}
