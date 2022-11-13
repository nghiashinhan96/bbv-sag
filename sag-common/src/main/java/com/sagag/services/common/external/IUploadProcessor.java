package com.sagag.services.common.external;

import com.sagag.services.common.contants.SagConstants;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * Basic upload processor interface
 */
public interface IUploadProcessor {

  /**
   * Upload a {@link MultipartFile} to destination
   * @param file
   * @return FileUploadDto contain the url to access the uploaded file
   * @throws IOException
   */
  Optional<FileUploadDto> upload(MultipartFile file) throws IOException;

  /**
   * Removes a file base on filename
   *
   * @param filename
   * @throws IOException
   */
  void remove(String filename) throws IOException;

  /**
   * Method to determine which processor is use for that type
   * @param type
   * @return boolean
   */
  boolean isSupport(FileUploadType type);

  default String buildFileName(String extension) {
    return Instant.now().toEpochMilli() + RandomStringUtils.randomAlphanumeric(4) + SagConstants.DOT + extension;
  }
}
