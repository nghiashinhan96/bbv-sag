package com.sagag.services.tools.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The generic business service exception thrown from Service layer.
 * <p>
 * All methods from classes which are annotated as <code>@Service</code> should throw this kind of
 * exception in case of business violation.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends Exception implements IServiceException {

  private static final long serialVersionUID = 1460780114390369442L;

  protected static final String INFO_KEY_USERNAME = "USER_NAME";
  protected static final String INFO_KEY_AFFILIATE = "AFFILIATE";

  /** the business error code for specific case. */
  private String code;

  /** the error message key. */
  private String key;

  /**
   * more information to throw so that the caller method can continue, for example the key message
   * parameters to build the complete message at frontend.
   */
  private Map<String, Object> moreInfos;

  /**
   * Constructs the service exception instance from the cause <code>throwable</code>.
   * 
   * @param throwable the root cause tracking of the exception.
   */
  public ServiceException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Constructs the service exception instance from the message.
   * <p>
   * The error <code>message</code> thrown from this exception is designed for logging and
   * debugging.
   * 
   * @param message the non-multilingual error message for the exception.
   */
  public ServiceException(String message) {
    super(message);
  }

  /**
   * Constructs the service exception instance from the <code>message</code> and
   * <code>throwable</code>.
   * <p>
   * The error <code>message</code> thrown from this exception is designed for logging and
   * debugging.
   * 
   * @param message the non-multilingual error message for the exception.
   * @param throwable the root cause tracking of the exception.
   */
  public ServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return null; // not implementation
  }
  
  @Override
  public String toLogMessage() {
    final boolean hasCode = !StringUtils.isBlank(this.getCode());
    final boolean hasKey = !StringUtils.isBlank(this.getKey());
    final boolean hasMessage = !StringUtils.isBlank(this.getMessage());
    if (!hasCode && !hasKey && !hasMessage) {
      return StringUtils.EMPTY;
    }
    final StringBuilder msgBuilder = new StringBuilder("Error [");
    final List<String> params = new ArrayList<>();
    if (hasCode) {
      msgBuilder.append("%s, ");
      params.add(this.getCode());
    }
    if (hasKey) {
      msgBuilder.append("Key = %s");
      params.add(this.getKey());
    }
    if (hasCode) {
      msgBuilder.append("]: ");
    }
    if (hasMessage) {
      msgBuilder.append("%s");
      params.add(this.getMessage());
    }
    return String.format(msgBuilder.toString(), params.toArray(new Object[] {}));
  }
}
