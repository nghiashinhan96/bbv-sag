package com.sagag.services.service.exporter;

import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.service.exporter.shoppingcart.ShortShoppingCartCsvExporter;
import com.sagag.services.service.exporter.shoppingcart.ShortShoppingCartExportItemDto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UT for ShortShoppingCartCsvExporter.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ShortShoppingCartCsvExporterTest {

  @InjectMocks
  private ShortShoppingCartCsvExporter csvExporter;

  @Test(expected = IllegalArgumentException.class)
  public void testExportCsv_WithEmptyCartItems() throws Exception {
    csvExporter.exportCsv(Collections.emptyList());
  }

  @Test
  public void testExportCsv_WithItems() throws Exception {
    List<ShortShoppingCartExportItemDto> items = new ArrayList<>();
    items.add(new ShortShoppingCartExportItemDto());
    ExportStreamedResult result = csvExporter.exportCsv(items);
    Assert.assertNotNull(result);
  }

}
