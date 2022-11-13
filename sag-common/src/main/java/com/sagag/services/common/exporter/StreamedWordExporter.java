package com.sagag.services.common.exporter;

import com.sagag.services.common.exception.ServiceException;

/**
 * Interface for streamed Word exporter.
 *
 * @param <T> the criteria object.
 */
@FunctionalInterface
public interface StreamedWordExporter<T> {

  /**
   * Returns the bytes object response for WORD file.
   *
   * @param criteria the criteria object
   * @return the result object
   */
  ExportStreamedResult exportWord(T criteria) throws ServiceException;

  /**
   * Converts to result object.
   *
   * @param content the binary content
   * @param fileName the file name
   * @return the result object
   */
  default ExportStreamedResult toWordResult(final byte[] content, String fileName) {
    return ExportStreamedResult.of(content, fileName, SupportedExportType.WORD, true);
  }
}
