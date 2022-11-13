package com.sagag.services.common.exporter;

import com.sagag.services.common.exception.ServiceException;

/**
 * Interface for streamed CSV exporter.
 *
 * @param <T> the criteria object.
 */
@FunctionalInterface
public interface StreamedCsvExporter<T> {

  /**
   * Returns the bytes object response for CSV file.
   *
   * @param criteria the criteria object
   * @return the result object
   */
  ExportStreamedResult exportCsv(T criteria) throws ServiceException;

  /**
   * Converts to result object.
   *
   * @param content the binary content
   * @param fileName the file name
   *  @return the result object
   */
  default ExportStreamedResult toCsvResult(final byte[] content, String fileName) {
    return ExportStreamedResult.of(content, fileName, SupportedExportType.CSV, true);
  }

}
