package com.sagag.services.service.exporter.shoppingcart;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedWordExporter;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Execution class for shopping cart report.
 */
@Component
@Slf4j
public class ShoppingCartWordExporter extends ShoppingCartFileExporter
    implements StreamedWordExporter<List<ShoppingCartExportItemDto>> {

  /**
   * Returns the binary result of shopping cart report file with *.docx format.
   *
   * @param data the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportWord(final List<ShoppingCartExportItemDto> data)
      throws ServiceException {
    Assert.notEmpty(data, EMPTY_CART_ITEMS_MSG);

    final byte[] bytes;
    try (final XWPFDocument document = new XWPFDocument()) {
      final XWPFTable table = document.createTable();
      createWordHeader(table.getRow(HEADER_ROW_INDEX), getHeaderColumns(data));

      List<String[]> shoppingCartItemsInfo = getShoppingCartItemsInfo(data);
      for (String[] items : shoppingCartItemsInfo) {
        final XWPFTableRow row = table.createRow();
        for (int colIndex = 0; colIndex < items.length; colIndex++) {
          row.getCell(colIndex).setText(items[colIndex]);
        }
      }
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      document.write(baos);
      bytes = baos.toByteArray();
      baos.close();

      return toWordResult(bytes, FILE_NAME);
    } catch (IOException ex) {
      final String message = "Export word file has exception";
      log.error(message, ex);
      throw new ServiceException(message, ex);
    }
  }

  private static void createWordHeader(XWPFTableRow headerRow, final String[] headers) {
    for (int cellIndex = 0; cellIndex < headers.length; cellIndex++) {
      if (cellIndex == 0) {
        headerRow.getCell(0).setText(headers[cellIndex]);
      } else {
        headerRow.addNewTableCell().setText(headers[cellIndex]);
      }
    }
  }

}
