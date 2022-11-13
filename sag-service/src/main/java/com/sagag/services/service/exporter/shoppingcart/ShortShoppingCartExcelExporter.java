package com.sagag.services.service.exporter.shoppingcart;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedExcelExporter;

import com.sagag.services.common.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
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
public class ShortShoppingCartExcelExporter extends ShoppingCartFileExporter
    implements StreamedExcelExporter<List<ShortShoppingCartExportItemDto>> {

  private static final int CONTENT_ROW_INDEX = 0;

  /**
   * Returns the binary result of shopping cart report file with *.xlsx format.
   *
   * @param data the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportExcel(List<ShortShoppingCartExportItemDto> data)
      throws ServiceException {
    return exportExcel(data, DEFAULT_SHEET_NAME);
  }

  public ExportStreamedResult exportExcel(List<ShortShoppingCartExportItemDto> data,
      String sheetName) throws ServiceException {
    Assert.notEmpty(data, EMPTY_CART_ITEMS_MSG);

    final byte[] bytes;
    try (final XSSFWorkbook workbook = new XSSFWorkbook()) {
      XSSFSheet sheet =
          workbook.createSheet(StringUtils.defaultIfBlank(sheetName, DEFAULT_SHEET_NAME));

      List<String[]> shoppingCartItemsInfo = getShortShoppingCartItemsInfo(data);

      ExcelUtils.fillExcelContentRows(shoppingCartItemsInfo, sheet, CONTENT_ROW_INDEX);

      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      // Write workbook to response.
      workbook.write(baos);
      bytes = baos.toByteArray();
      baos.close();
      return toExcelResult(bytes, FILE_NAME);
    } catch (IOException ex) {
      final String MESSAGE = "Export excel file has exception";
      log.error(MESSAGE, ex);
      throw new ServiceException(MESSAGE, ex);
    }
  }

}
