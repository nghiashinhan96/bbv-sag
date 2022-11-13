package com.sagag.services.service.exporter.wss;

import com.opencsv.CSVWriter;
import com.sagag.services.common.contants.SagConstants;
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
 * Execution class for WSS final customer order CSV export.
 */
@Component
@Slf4j
public class WssFinalCustomerOrderCsvExporter extends WssFinalCustomerOrderFileExporter
    implements StreamedCsvExporter<List<WssFinalCustomerOrderExportItemDto>> {

  /**
   * Returns the binary result of WSS final customer order report file with *.csv format.
   *
   * @param orderExportItemDtos the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportCsv(
      List<WssFinalCustomerOrderExportItemDto> orderExportItemDtos) throws ServiceException {
    Assert.notEmpty(orderExportItemDtos, "The given order items must not be empty");
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final List<String[]> csvExportList = new LinkedList<>();

      // Add default csv header
      csvExportList.add(getFinalCustomerOrderCsvExportHeader(orderExportItemDtos));

      // Add data
      csvExportList.addAll(getFinalCustomerOrderItemsInfo(orderExportItemDtos));

      final OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
      final CSVWriter writer = new CSVWriter(osw, SagConstants.CSV_DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
          CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
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
