package com.sagag.services.service.exporter.shoppingcart;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedExcelExporter;

import com.sagag.services.common.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class ShoppingCartExcelExporter extends ShoppingCartFileExporter
    implements StreamedExcelExporter<List<ShoppingCartExportItemDto>> {

  private static final int CONTENT_ROW_INDEX = 1;

  /**
   * Returns the binary result of shopping cart report file with *.xlsx format.
   *
   * @param data the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportExcel(List<ShoppingCartExportItemDto> data)
      throws ServiceException {
    Assert.notEmpty(data, EMPTY_CART_ITEMS_MSG);

    final byte[] bytes;
    try (final XSSFWorkbook workbook = new XSSFWorkbook()) {
      XSSFSheet sheet = workbook.createSheet(DEFAULT_SHEET_NAME);

      createExcelHeader(sheet.createRow(HEADER_ROW_INDEX), getHeaderColumns(data));
      List<String[]> shoppingCartItemsInfo = getShoppingCartItemsInfo(data);
      ExcelUtils.fillExcelContentRows(shoppingCartItemsInfo, sheet, CONTENT_ROW_INDEX);

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos); // Write workbook to response.
      bytes = baos.toByteArray();
      baos.close();
      return toExcelResult(bytes, FILE_NAME);
    } catch (IOException ex) {
      final String message = "Export excel file has exception";
      log.error(message, ex);
      throw new ServiceException(message, ex);
    }
  }

  private static void createExcelHeader(final Row row, final String[] headers) {
    for (int cellIndex = 0; cellIndex < headers.length; cellIndex++) {
      final Cell cell = row.createCell(cellIndex);
      cell.setCellValue(headers[cellIndex]);
    }
  }

}
