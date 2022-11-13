package com.sagag.eshop.service.exporter;

import com.opencsv.CSVWriter;
import com.sagag.eshop.service.exception.LicenseExportException;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedCsvExporter;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Builder
public class LicenseExporter implements StreamedCsvExporter<List<BackOfficeLicenseDto>> {

  @Override
  public ExportStreamedResult exportCsv(List<BackOfficeLicenseDto> data) throws ServiceException {
    final String[] csvExportHeader = { "CustomerNumber", "LicenseType", "LicenseName",
        "From", "To", "Quantity", "NumberUsed" };
    final List<String[]> csvExportList = new LinkedList<>();

    csvExportList.add(csvExportHeader);
    csvExportList.addAll(getLicenseItemInfos(data));
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

      final OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
      final CSVWriter writer = new CSVWriter(osw, SagConstants.CSV_DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
          CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
      writer.writeAll(csvExportList, false);
      writer.close();
      return toCsvResult(baos.toByteArray(), getExportFileName());
    } catch (IOException ex) {
      final String message = "Export csv file has exception";
      log.error(message, ex);
      throw new ServiceException(message, ex);
    }
  }

  private List<String[]> getLicenseItemInfos(final List<BackOfficeLicenseDto> exportItems) {
    List<String[]> dataForExport = new ArrayList<>();
    exportItems.stream().forEach(item -> dataForExport.add(getLisenceItemInfo(item)));
    return dataForExport;
  }

  private String[] getLisenceItemInfo(BackOfficeLicenseDto exportItem) {
    final List<String> marginCustomerTokenInfo = new ArrayList<>();
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getCustomerNr(), StringUtils.EMPTY));
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getTypeOfLicense(), StringUtils.EMPTY));
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getPackName(), StringUtils.EMPTY));
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getBeginDate(), StringUtils.EMPTY));
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getEndDate(), StringUtils.EMPTY));
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getQuantity(), StringUtils.EMPTY));
    marginCustomerTokenInfo.add(Objects.toString(exportItem.getQuantityUsed(), StringUtils.EMPTY));

    return marginCustomerTokenInfo.toArray(new String[marginCustomerTokenInfo.size()]);
  }

  private String getExportFileName() {
    final String exportDatePattern = "ddMMyyyy";
    return "license_list_" + DateUtils.toStringDate(Calendar.getInstance().getTime(), exportDatePattern);
  }

  public ExportStreamedResult exportExcel(final List<BackOfficeLicenseDto> listExport) throws LicenseExportException {
    if (Objects.isNull(listExport) || CollectionUtils.isEmpty(listExport)) {
      throw new IllegalArgumentException("Missing data to create file");
    }
    final String sheetName = "licenses";
    final int firstRowIndex = 0;
    final int columnWidth = 30;

    final List<String> headers = Arrays.asList( "Customer Number", "License Type", "License Name",
        "From", "To", "Quantity", "Number Used");

    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (Workbook workbook = new XSSFWorkbook()) {
      final Sheet sheet = workbook.createSheet(sheetName);
      sheet.setDefaultColumnWidth(columnWidth);

      buildHeader(sheet, headers, firstRowIndex);
      buildBody(sheet, listExport, headers);

      workbook.write(bos);
      bos.close();
      return ExportStreamedResult.of(bos.toByteArray(), getExportFileName(), SupportedExportType.EXCEL, true);
    } catch (IOException ex) {
      final String msg = "Can not export excel file";
      log.error(msg, ex);
      throw new LicenseExportException(msg, ex);
    }
  }

  private void buildHeader(final Sheet sheet, final List<String> headers, final int firstRowIndex) {
    Row header = sheet.createRow(firstRowIndex);
    for (int i = 0; i < headers.size(); i++) {
      header.createCell(i).setCellValue(headers.get(i));
    }
  }

  private void buildBody(final Sheet sheet, final List<BackOfficeLicenseDto> listExport,
      List<String> headers) {
    Row row;
    String attrValue;
    for (int i = 0; i < listExport.size(); i++) {
      row = sheet.createRow(i + 1);
      for (int j = 0; j < headers.size(); j++) {
        attrValue = getAttrValue(listExport.get(i), j);
        row.createCell(j).setCellValue(StringUtils.defaultString(attrValue));
      }
    }
  }

  private String getAttrValue(BackOfficeLicenseDto license, int columnNumber) {
    switch(columnNumber) {
      case 0: return String.valueOf(license.getCustomerNr());
      case 1: return license.getTypeOfLicense();
      case 2: return  license.getPackName();
      case 3: return license.getBeginDate();
      case 4: return license.getEndDate();
      case 5: return String.valueOf(license.getQuantity());
      case 6: return String.valueOf(license.getQuantityUsed());
      default: return StringUtils.EMPTY;
    }
  }
}
