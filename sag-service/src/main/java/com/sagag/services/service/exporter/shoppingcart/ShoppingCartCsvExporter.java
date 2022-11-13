package com.sagag.services.service.exporter.shoppingcart;

import com.opencsv.CSVWriter;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedCsvExporter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * Execution class for shopping cart report.
 */
@Component
@Slf4j
public class ShoppingCartCsvExporter extends ShoppingCartFileExporter
    implements StreamedCsvExporter<List<ShoppingCartExportItemDto>> {

  /**
   * Returns the binary result of shopping cart report file with *.csv format.
   *
   * @param data the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportCsv(List<ShoppingCartExportItemDto> data)
      throws ServiceException {
    Assert.notEmpty(data, EMPTY_CART_ITEMS_MSG);

    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final List<String[]> csvExportList = new LinkedList<>();

      // Add default csv header
      csvExportList.add(getHeaderColumns(data));

      // Add data
      csvExportList.addAll(getShoppingCartItemsInfo(data));

      final OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
      final CSVWriter writer = new CSVWriter(osw, CSV_DELIMITER,
          CSVWriter.DEFAULT_QUOTE_CHARACTER,
          CSVWriter.DEFAULT_ESCAPE_CHARACTER,
          CSVWriter.DEFAULT_LINE_END);
      writer.writeAll(csvExportList, false);
      writer.close();
      return toCsvResult(baos.toByteArray(), FILE_NAME);
    } catch (IOException ex) {
      final String message = "Export csv file has exception";
      log.error(message, ex);
      throw new ServiceException(message, ex);
    }
  }

}
