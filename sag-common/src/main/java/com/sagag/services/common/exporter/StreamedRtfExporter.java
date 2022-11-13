package com.sagag.services.common.exporter;

import com.sagag.services.common.exception.ServiceException;

/**
 * Interface for streamed RTF exporter.
 *
 * @param <T> the criteria object.
 */
@FunctionalInterface
public interface StreamedRtfExporter<T> {

  /**
   * Returns the bytes object response for RTF file.
   *
   * @param criteria the criteria object
   * @return the result object
   */
  ExportStreamedResult exportRtf(T criteria) throws ServiceException;

  /**
   * Converts to result object.
   *
   * @param content the binary content
   * @param fileName the file name
   * @return the result object
   */
  default ExportStreamedResult toRtfResult(final byte[] content, final String fileName) {
    return ExportStreamedResult.of(content, fileName, SupportedExportType.RTF, true);
  }
}
