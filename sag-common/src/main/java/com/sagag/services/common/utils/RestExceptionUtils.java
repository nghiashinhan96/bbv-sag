package com.sagag.services.common.utils;

import com.sagag.services.common.exception.ResultNotFoundException;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utilities for handle result with null-safe.
 */
@UtilityClass
public final class RestExceptionUtils {

  private static final String DF_NOT_FOUND_MSG = "Search result not found";

  /**
   * Throws <code>ResultNotFoundException</code> when the <code>records</code> is empty.
   *
   * @param records the data to check empty.
   * @throws ResultNotFoundException throws when <code>records</code> is empty.
   */
  public static <T> Page<T> doSafelyReturnNotEmptyRecords(final Page<T> records)
      throws ResultNotFoundException {
    return doSafelyReturnNotEmptyRecords(records, StringUtils.EMPTY);
  }

  /**
   * Throws <code>ResultNotFoundException</code> when the <code>records</code> is empty.
   *
   * @param records the data to check empty.
   * @throws ResultNotFoundException throws when <code>records</code> is empty.
   */
  public static <T> Page<T> doSafelyReturnNotEmptyRecords(final Page<T> records, final String msg)
      throws ResultNotFoundException {
    if (records == null || !records.hasContent()) {
      throwResultNotFoundException(msg);
    }
    return records;
  }

  /**
   * Throws <code>ResultNotFoundException</code> when the <code>records</code> is empty.
   *
   * @param records the list of data to check empty.
   * @param msg the requested message to throw exception
   * @throws ResultNotFoundException throws when <code>records</code> is empty.
   */
  public static <T> List<T> doSafelyReturnNotEmptyRecords(final List<T> records, final String msg)
      throws ResultNotFoundException {
    if (CollectionUtils.isEmpty(records)) {
      throwResultNotFoundException(msg);
    }
    return records;
  }

  public static <T> List<T> doSafelyReturnNotEmptyRecords(final List<T> records)
      throws ResultNotFoundException {
    return doSafelyReturnNotEmptyRecords(records, StringUtils.EMPTY);
  }

  public static <K, V> Map<K, V> doSafelyReturnNotEmptyRecords(final Map<K, V> records) throws ResultNotFoundException {
    return doSafelyReturnNotEmptyRecords(records, DF_NOT_FOUND_MSG);
  }

  public static <K, V> Map<K, V> doSafelyReturnNotEmptyRecords(final Map<K, V> records,
      final String msg) throws ResultNotFoundException {
    if (MapUtils.isEmpty(records)) {
      throwResultNotFoundException(msg);
    }
    return records;
  }

  public static <T> T doSafelyReturnOptionalRecord(final Optional<T> record)
    throws ResultNotFoundException {
    return doSafelyReturnOptionalRecord(record, DF_NOT_FOUND_MSG);
  }

  public static <T> T doSafelyReturnOptionalRecord(final Optional<T> record, String msg)
      throws ResultNotFoundException {
      if (!record.isPresent()) {
        throwResultNotFoundException(msg);
      }
      return record.get();
    }

  private static void throwResultNotFoundException(final String msg) throws ResultNotFoundException {
    throw new ResultNotFoundException(StringUtils.defaultIfBlank(msg, DF_NOT_FOUND_MSG));
  }

}
