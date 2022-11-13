package com.sagag.services.service.exporter;

import com.sagag.services.service.exporter.shoppingcart.ShoppingCartCsvExporter;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartExcelExporter;
import com.sagag.services.service.exporter.shoppingcart.ShoppingCartWordExporter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

/**
 * UT for ShoppingCartFileExporter.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartFileExporterTest {

  @InjectMocks
  private ShoppingCartCsvExporter csvExporter;

  @InjectMocks
  private ShoppingCartWordExporter wordExporter;

  @InjectMocks
  private ShoppingCartExcelExporter excelExporter;

  @Test(expected = IllegalArgumentException.class)
  public void testExportCsv_WithEmptyCartItems() throws Exception {
    csvExporter.exportCsv(Collections.emptyList());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExportWord_WithEmptyCartItems() throws Exception {
    wordExporter.exportWord(Collections.emptyList());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExportExcel_WithEmptyCartItems() throws Exception {
    excelExporter.exportExcel(Collections.emptyList());
  }

}
