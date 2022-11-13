package com.sagag.services.common.exporter;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * POJO class for export result.
 */
@Data
public class ExportStreamedResult {

  public static final String CONTENT_TYPE = "Content-Type";

  public static final String CONTENT_DISPOSITION = "Content-Disposition";

  public static final String ATTACHMENT_DISPOSITION = "attachment;";

  public static final String FILE_NAME_PATTERN = "filename=";

  private final byte[] content;

  private String fileName;

  private String contentTypeValue;

  private String contentDispositionValue;

  public int getContentLength() {
    if (content == null) {
      return NumberUtils.INTEGER_ZERO;
    }
    return content.length;
  }

  public static ExportStreamedResult of(byte[] content, String fileName,
      SupportedExportType exportType, boolean isAttachment) {
    final ExportStreamedResult result = new ExportStreamedResult(content);
    result.setContentTypeValue(exportType.getContentType());
    final String fileNameWithExt = fileName + exportType.getSuffix();
    result.setFileName(fileNameWithExt);
    final String contentDispositionValue =
        (isAttachment ? ATTACHMENT_DISPOSITION : StringUtils.EMPTY) + FILE_NAME_PATTERN + fileNameWithExt;
    result.setContentDispositionValue(contentDispositionValue);
    return result;
  }

  public ResponseEntity<byte[]> buildResponseEntity() {
    final HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, getContentTypeValue());
    headers.set(HttpHeaders.CONTENT_DISPOSITION, getContentDispositionValue());
    headers.setContentLength(getContentLength());
    return new ResponseEntity<>(getContent(), headers, HttpStatus.OK);
  }

}
