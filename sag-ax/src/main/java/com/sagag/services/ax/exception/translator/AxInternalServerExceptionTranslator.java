package com.sagag.services.ax.exception.translator;

import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxTimeoutException;
import java.net.SocketTimeoutException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.stereotype.Component;

@Component
public class AxInternalServerExceptionTranslator extends DefaultAxExternalExceptionTranslator {

  private static final String ERROR_MESSAGE_DETAILS_KEY = "Message details:";

  @Override
  public AxExternalException apply(Exception ex) {
    return getUnauthorizedRequestException(ex).orElseGet(() -> {
      if (ex.getCause() instanceof ConnectTimeoutException
          || ex.getCause() instanceof SocketTimeoutException) {
        return AxTimeoutException.connectionTimeoutException(
            "The AX request got connection timeout");
      }
      return AxExternalException.connectInternalServerError(
          buildAxCustomErrorMessageBody(ex.getMessage()));
    });
  }

  private static String buildAxCustomErrorMessageBody(String rawAxErrorMessageBody) {
    final String keyText = ERROR_MESSAGE_DETAILS_KEY;
    if (!StringUtils.containsIgnoreCase(rawAxErrorMessageBody, keyText)) {
      return rawAxErrorMessageBody;
    }
    final String[] errors = StringUtils.splitByWholeSeparator(rawAxErrorMessageBody, keyText);
    if (ArrayUtils.getLength(errors) <= 1) {
      return rawAxErrorMessageBody;
    }
    return errors[ArrayUtils.getLength(errors) - 1];
  }
}
