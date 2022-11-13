package com.sagag.services.common.exporter;

import com.sagag.services.common.exception.ServiceException;

/**
 * Interface for streamed PDF exporter.
 *
 * @param <T> the criteria object.
 */
@FunctionalInterface
public interface StreamedPdfExporter<T> {

  /**
   * Returns the bytes object response for PDF file.
   *
   * @param criteria the criteria object
   * @return the result object
   */
  ExportStreamedResult exportPdf(T criteria) throws ServiceException;

  /**
   * Converts to result object.
   *
   * @param content the binary content
   * @param fileName the file name
   *  @return the result object
   */
  default ExportStreamedResult toPdfResult(final byte[] content, String fileName) {
    return ExportStreamedResult.of(content, fileName, SupportedExportType.PDF, true);
  }
}
