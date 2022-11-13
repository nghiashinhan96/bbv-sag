package com.sagag.services.tools.utils;

import com.opencsv.ICSVParser;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sagag.services.tools.exception.BatchJobException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

  private static final String FILE_PATTERN_NOT_NULL = "The given file name pattern must not be null";

  private static final String CONTENT_NOT_EMPTY = "The given list of content must not be empty";

  private static final boolean DF_IGNORE_HEADER = false;

  /**
   * Writes the list of content to csv format.
   *
   */
  public static void write(final String filePath, final List<?> objects) throws BatchJobException {
    Assert.hasText(filePath, FILE_PATH_NOT_NULL);
    OutputStreamWriter writer = null;
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream fos = FileUtils.openOutputStream(file);
      writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
      new StatefulBeanToCsvBuilder<>(writer).build().write(objects);
    } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException ex) {
      throw new BatchJobException("Write csv has error", ex);
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  /**
   * Returns the list of content of csv file cast by custom clazz.
   *
   */
  public static <T> List<T> read(final File file, final Class<T> clazz) throws BatchJobException {
    return read(file, StandardCharsets.UTF_8, clazz);
  }

  /**
   * Returns the list of content of csv file cast by custom clazz.
   *
   */
  public static <T> List<T> read(final File file, final Charset charset,
    final Class<T> clazz) throws BatchJobException {
    return read(file, ICSVParser.DEFAULT_SEPARATOR, clazz);
  }

  /**
   * Returns the list of content of csv file with custom separator cast by custom clazz.
   *
   */
  public static <T> List<T> read(final File file, final char separator,
    final Class<T> clazz) throws BatchJobException {
    return read(file, separator, StandardCharsets.UTF_8, clazz, DF_IGNORE_HEADER);
  }

  /**
   * Returns the list of content of csv file with custom separator cast by custom clazz.
   *
   */
  public static <T> List<T> read(final File file, final char separator, final Charset charset,
    final Class<T> clazz, boolean ignoreHeader) throws BatchJobException {
    Assert.notNull(file, FILE_PATH_NOT_NULL);
    FileInputStream fileInputStream = null;
    InputStreamReader reader = null;
    try {
      fileInputStream = FileUtils.openInputStream(file);
      reader = openInputStreamReader(fileInputStream, charset);

      final CsvToBeanBuilder<T> csvBuilder = new CsvToBeanBuilder<T>(reader)
          .withSeparator(separator)
          .withType(clazz);

      if (ignoreHeader) {
        csvBuilder.withSkipLines(0);
      }

      return Collections.unmodifiableList(csvBuilder.build().parse());
    } catch (IOException ex) {
      throw new BatchJobException(ex);
    } finally {
      IOUtils.closeQuietly(fileInputStream);
      IOUtils.closeQuietly(reader);
    }
  }

  private static InputStreamReader openInputStreamReader(
    final FileInputStream fileInputStream, final Charset charset) throws BatchJobException {
    Assert.notNull(fileInputStream, FILE_PATH_NOT_NULL);
    log.debug("Default charset = {}", Charset.defaultCharset());
    if (Charset.defaultCharset() == StandardCharsets.UTF_8 || charset == null) {
      return new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
    }
    return new InputStreamReader(fileInputStream, charset);
  }

  /**
   * Returns the list of content of csv file cast by custom clazz.
   *
   */
  public static <T> List<T> readIgnoreHeader(final File file, final Class<T> clazz)
      throws BatchJobException {
    return readIgnoreHeader(file, StandardCharsets.UTF_8, clazz);
  }

  public static <T> List<T> readIgnoreHeader(final File file, final Charset charset,
      final Class<T> clazz) throws BatchJobException {
      return read(file, ICSVParser.DEFAULT_SEPARATOR, StandardCharsets.UTF_8, clazz, true);
  }

  /**
   * Splits the list of content to the bundle csv files.
   *
   */
  public static <T> void split(final String filePath,
    final List<T> content) throws BatchJobException {
    Assert.hasText(filePath, FILE_PATTERN_NOT_NULL);
    Assert.notEmpty(content, CONTENT_NOT_EMPTY);
    final List<List<T>> partitions =
      ListUtils.partition(content, ToolConstants.MAX_SIZE);
    partitions.parallelStream().forEach(partition -> {
      try {
        final String file = filePath + System.currentTimeMillis() + ToolConstants.CSV_SUFFIX;
        write(file, partition);
      } catch (BatchJobException ex) {
        log.error("BatchJobException", ex);
      }
    });
  }
}
