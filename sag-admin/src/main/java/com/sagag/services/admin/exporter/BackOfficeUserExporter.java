package com.sagag.services.admin.exporter;

import com.sagag.services.admin.exception.UserExportException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.IExportAttributes;
import com.sagag.services.common.exporter.StreamedExcelExporter;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * BackOfficeUserExporter.
 */
@Component
@Slf4j
public class BackOfficeUserExporter implements StreamedExcelExporter<BackOfficeUserExportCriteria> {

  private static final String USER_SHEET_NAME = "users";
  private static final int FIRST_ROW_INDEX = 0;
  private static final String FILE_NAME = "users";
  private static final int DEFAULT_COLUMN_WIDTH = 30;

  @Autowired
  private IExportAttributes<BackOfficeUserExportAttributeType> userExportAttributes;

  @Override
  public ExportStreamedResult exportExcel(final BackOfficeUserExportCriteria criteria)
      throws UserExportException {
    Assert.notNull(criteria, "The given criteria must not be null");

    final List<ExportingUserDto> users = criteria.getUsers();
    final List<BackOfficeUserExportAttributeType> attrs = userExportAttributes.getExportAttributes();
    if (CollectionUtils.isEmpty(users) || CollectionUtils.isEmpty(attrs)) {
      throw new IllegalArgumentException("Missing data to create file");
    }

    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (Workbook workbook = new XSSFWorkbook()) {
      final Sheet sheet = workbook.createSheet(USER_SHEET_NAME);
      sheet.setDefaultColumnWidth(DEFAULT_COLUMN_WIDTH);

      buildHeader(sheet, attrs);
      buildBody(sheet, attrs, users);

      workbook.write(bos);
      bos.close();
      return toExcelResult(bos.toByteArray(), FILE_NAME);
    } catch (IOException ex) {
      final String msg = "Can not export file";
      log.error(msg, ex);
      throw new UserExportException(msg, ex);
    }
  }

  private void buildHeader(final Sheet sheet, final List<BackOfficeUserExportAttributeType> attrs) {
    Row header = sheet.createRow(FIRST_ROW_INDEX);
    for (int i = 0; i < attrs.size(); i++) {
      header.createCell(i).setCellValue(attrs.get(i).getValue());
    }
  }

  private void buildBody(final Sheet sheet, final List<BackOfficeUserExportAttributeType> attrs,
      final List<ExportingUserDto> users) {
    Row row;
    String attrValue;
    for (int i = 0; i < users.size(); i++) {
      row = sheet.createRow(i + 1);
      for (int j = 0; j < attrs.size(); j++) {
        attrValue = attrs.get(j).getAttrValue(users.get(i));
        row.createCell(j).setCellValue(StringUtils.defaultString(attrValue));
      }
    }
  }

}
