package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Utilities for XLSX format
 */
@UtilityClass
public final class ExcelUtils {

  public static void fillExcelContentRows(final List<String[]> data,
                                             final XSSFSheet sheet, final int startContentRowIndex) {

    if (CollectionUtils.isEmpty(data) || Objects.isNull(sheet)) {
      return;
    }
    int rowIndex = startContentRowIndex;
    for (final Object[] rowData : data) {
      final Row rowExcel = sheet.createRow(rowIndex++);
      int cellNum = 0;
      for (final Object obj : rowData) {
        final Cell cell = rowExcel.createCell(cellNum++);

        if (obj instanceof Date) {
          cell.setCellValue((Date) obj);
        } else if (obj instanceof Boolean) {
          cell.setCellValue((Boolean) obj);
        } else if (obj instanceof String) {
          cell.setCellValue((String) obj);
        } else if (obj instanceof Double) {
          cell.setCellValue((Double) obj);
        } else if (obj instanceof Long) {
          cell.setCellValue((Long) obj);
        }
      }
    }
  }

}
