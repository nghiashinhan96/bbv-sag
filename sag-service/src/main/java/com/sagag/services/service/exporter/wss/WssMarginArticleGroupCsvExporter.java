package com.sagag.services.service.exporter.wss;

import com.opencsv.CSVWriter;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedCsvExporter;
import com.sagag.services.common.utils.CsvUtils;

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
 * Execution class for WSS margin article group report.
 */
@Component
@Slf4j
public class WssMarginArticleGroupCsvExporter
    implements StreamedCsvExporter<List<WssMarginArticleGroupExportItemDto>> {

  private static String[] ARTICLE_GROUP_CSV_EXPORT_HEADER =
      { "SAG_ART_GROUP_ID", "SAG_ART_GROUP_DESC", "CUSTOMER_ART_GROUP_ID", "CUSTOMER_ART_GROUP_TEXT", "MARGIN_1",
          "MARGIN_2", "MARGIN_3", "MARGIN_4", "MARGIN_5", "MARGIN_6", "MARGIN_7" };
  protected static final String FILE_NAME = "WssMarginArticleGroup";

  /**
   * Returns the binary result of WSS margin article group report file with *.csv format.
   *
   * @param data the input data
   * @return the binary result
   */
  @Override
  public ExportStreamedResult exportCsv(List<WssMarginArticleGroupExportItemDto> data)
      throws ServiceException {
    Assert.notNull(data, "Export Wss margin article group data must not be null");
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final List<String[]> csvExportList = new LinkedList<>();

      // Add default CSV header
      csvExportList.add(ARTICLE_GROUP_CSV_EXPORT_HEADER);

      // Add data
      csvExportList.addAll(getWssMarginArticleGroupItemsInfo(data));

      final OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
      final CSVWriter writer = new CSVWriter(osw, SagConstants.CSV_SEMICOLON_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
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

  private List<String[]> getWssMarginArticleGroupItemsInfo(
      final List<WssMarginArticleGroupExportItemDto> exportItems) {
    List<String[]> dataForExport = new ArrayList<>();
    exportItems.stream().forEach(item -> dataForExport.add(getWssMarginArticleGroupItemInfo(item)));
    return dataForExport;
  }

  private String[] getWssMarginArticleGroupItemInfo(WssMarginArticleGroupExportItemDto exportItem) {
    final List<String> marginArticleGroupInfo = new ArrayList<>();
    marginArticleGroupInfo.add(CsvUtils.wrapTextForExcel(exportItem.getSagArticleGroup()));
    marginArticleGroupInfo.add(exportItem.getSagArticleGroupDesc());
    marginArticleGroupInfo.add(CsvUtils.wrapTextForExcel(exportItem.getCustomArticleGroup()));
    marginArticleGroupInfo.add(CsvUtils.wrapTextForExcel(exportItem.getCustomArticleGroupDesc()));
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
