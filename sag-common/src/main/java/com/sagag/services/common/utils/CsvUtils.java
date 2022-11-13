package com.sagag.services.common.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for CSV format
 */
@UtilityClass
@Slf4j
public final class CsvUtils {

  private static final String FILE_PATH_NOT_NULL = "The given file must not be null";

  public static final int MAX_SIZE = 1000;

  public static final String CSV_SUFFIX = ".csv";

  /**
   * Returns the list of content of csv file cast by custom clazz.
   *
   */
  public static <T> List<T> read(final MultipartFile file, final Class<T> clazz, char separator,
      boolean useDefaultCharset)
      throws CsvServiceException {
    InputStreamReader reader = null;
    InputStream inputStream = null;
    try {
      inputStream = file.getInputStream();
      reader = useDefaultCharset ? openDefaultCharsetInputStreamReader(inputStream)
          : openInputStreamReader(inputStream);
      return Collections.unmodifiableList(new CsvToBeanBuilder<T>(reader).withSeparator(separator)
          .withQuoteChar(CSVWriter.DEFAULT_QUOTE_CHARACTER).withType(clazz).build().parse());
    } catch (Exception ex) {
      throw new CsvServiceException(ex);
    } finally {
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(reader);
    }
  }

  public static String wrapTextForExcel(String text) {
    StringBuilder builder = new StringBuilder();
    builder.append(SagConstants.EQUAL).append(SagConstants.DOUBLE_QUOTE_CHAR)
        .append(StringUtils.defaultString(text)).append(SagConstants.DOUBLE_QUOTE_CHAR);
    return builder.toString();
  }

  public static String unwrapExcelText(String text) {
    if (StringUtils.isBlank(text)) {
      return text;
    }
    if (text.startsWith(SagConstants.EQUAL)) {
      text = text.substring(1);
    }
    if (text.startsWith(SagConstants.DOUBLE_QUOTE) && text.endsWith(SagConstants.DOUBLE_QUOTE)) {
      text =
          StringUtils.substringBetween(text, SagConstants.DOUBLE_QUOTE, SagConstants.DOUBLE_QUOTE);
    }
    return text;
  }

  private static InputStreamReader openInputStreamReader(final InputStream inputStream)
      throws CsvServiceException {
    Assert.notNull(inputStream, FILE_PATH_NOT_NULL);
    log.debug("Default charset = {}", Charset.defaultCharset());
    if (Charset.defaultCharset() == StandardCharsets.UTF_8) {
      return openInputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
    }
    return new InputStreamReader(inputStream);
  }

  private static InputStreamReader openDefaultCharsetInputStreamReader(
      final InputStream inputStream)
      throws CsvServiceException {
    Assert.notNull(inputStream, FILE_PATH_NOT_NULL);
    log.debug("Default charset = {}", Charset.defaultCharset());
    return openInputStreamReader(inputStream, Charset.defaultCharset());
  }

  private static InputStreamReader openInputStreamReader(final InputStream inputStream,
      final Charset charset) {
    Assert.notNull(inputStream, FILE_PATH_NOT_NULL);
    return new InputStreamReader(inputStream, charset);
  }

}
