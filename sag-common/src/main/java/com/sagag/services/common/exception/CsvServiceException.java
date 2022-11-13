package com.sagag.services.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * General csv import exception.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CsvServiceException extends Exception {

  private static final long serialVersionUID = -7552502793980527440L;

  private String message;

  public CsvServiceException() {
    super();
    this.message = "CSV Importing has exception";
  }

  public CsvServiceException(String msg) {
    super(msg);
    this.message = msg;
  }

  public CsvServiceException(Throwable cause) {
    super(cause);
  }

  public CsvServiceException(String msg, Throwable cause) {
    super(msg, cause);
    this.message = msg;
  }
  

  /**
   * Csv error cases enumeration definition.
   */
  public enum CsvErrorCase implements IBusinessCode {
    CSV_ERR_001("CSV_SERVICE_ERROR");

    private String key;

    CsvErrorCase(String key) {
      this.key = key;
    }

    @Override
    public String code() {
      return this.name();
    }

    @Override
    public String key() {
      return this.key;
    }

  }
}
