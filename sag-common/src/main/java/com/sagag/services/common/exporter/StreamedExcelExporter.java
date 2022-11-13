package com.sagag.services.common.exporter;

import com.sagag.services.common.exception.ServiceException;

/**
 * Interface for streamed Excel exporter.
 *
 * @param <T> the criteria object.
 */
@FunctionalInterface
public interface StreamedExcelExporter<T> {

  /**
   * Returns the bytes object response for Excel file.
   *
   * @param criteria the criteria object
   * @return the result object
   */
  ExportStreamedResult exportExcel(T criteria) throws ServiceException;

  /**
   * Converts to result object.
   *
   * @param content the binary content
   * @param fileName the file name
   * @return the result object
   */
  default ExportStreamedResult toExcelResult(final byte[] content, String fileName) {
    return ExportStreamedResult.of(content, fileName, SupportedExportType.EXCEL, true);
  }
}
