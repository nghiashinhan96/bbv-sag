package com.sagag.services.service.exporter.wss;

import com.opencsv.CSVWriter;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedCsvExporter;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Execution class for WSS margin brand report.
 */
@Component
@Slf4j
public class WssMarginBrandCsvExporter
    implements StreamedCsvExporter<List<WssMarginBrandExportItemDto>> {

  private static String[] MARGIN_BRAND_CSV_EXPORT_HEADER = { "BRAND_ID", "BRAND_NAME", "MARGIN_1", "MARGIN_2",
      "MARGIN_3", "MARGIN_4", "MARGIN_5", "MARGIN_6", "MARGIN_7" };
  protected static final char CSV_DELIMITER = ';';
  protected static final String FILE_NAME = "WssMarginBrand";

  /**
   * Returns the binary result of WSS margin brand report file with *.csv format.
   *
   * @param data the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportCsv(List<WssMarginBrandExportItemDto> data)
      throws ServiceException {
    Assert.notNull(data, "Export Wss margin brand data must not be null");
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final List<String[]> csvExportList = new LinkedList<>();

      csvExportList.add(MARGIN_BRAND_CSV_EXPORT_HEADER);

      csvExportList.addAll(getWssMarginBrandItemsInfo(data));

      final OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
      final CSVWriter writer = new CSVWriter(osw, CSV_DELIMITER, CSVWriter.DEFAULT_QUOTE_CHARACTER,
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

  private List<String[]> getWssMarginBrandItemsInfo(
      final List<WssMarginBrandExportItemDto> exportItems) {
    List<String[]> dataForExport = new ArrayList<>();
    exportItems.stream().forEach(item -> dataForExport.add(getWssMarginBrandItemInfo(item)));
    return dataForExport;
  }

  private String[] getWssMarginBrandItemInfo(WssMarginBrandExportItemDto exportItem) {
    final List<String> marginArticleGroupInfo = new ArrayList<>();
    marginArticleGroupInfo.add(Objects.toString(exportItem.getBrandId(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(exportItem.getBrandName());
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin1(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin2(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin3(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin4(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin5(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin6(), StringUtils.EMPTY));
    marginArticleGroupInfo.add(Objects.toString(exportItem.getMargin7(), StringUtils.EMPTY));

    return marginArticleGroupInfo.toArray(new String[marginArticleGroupInfo.size()]);
  }

}
