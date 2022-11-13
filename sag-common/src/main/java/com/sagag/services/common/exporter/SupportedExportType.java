package com.sagag.services.common.exporter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * Enumeration of supported export type.
 */
@Getter
@AllArgsConstructor
public enum SupportedExportType {

  PDF(".pdf", "application/pdf"), RTF(".rtf", "application/rtf"),
  EXCEL(".xlsx", "application/excel"), WORD(".docx", "application/msword"),
  CSV(".csv", "application/csv");

  private String suffix;

  private String contentType;

  public static SupportedExportType getExportType(String exportTypeStr) {
    return Stream.of(values())
        .filter(val -> StringUtils.equalsIgnoreCase(val.name(), exportTypeStr)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not support this type for exporting offer"));
  }
}
