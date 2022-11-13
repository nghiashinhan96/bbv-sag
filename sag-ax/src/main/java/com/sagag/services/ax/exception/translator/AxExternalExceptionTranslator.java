package com.sagag.services.ax.exception.translator;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.MoreObjects;
import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.ErrorInfo;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AxExternalExceptionTranslator
    implements Function<Exception, AxExternalException> {

  protected AxExternalExceptionTranslator() {
    // do nothing
  }

  protected static Function<byte[], ErrorInfo> errorInfoConverter() {
    return bytes -> {
      if (ArrayUtils.getLength(bytes) == 0) {
        return new ErrorInfo();
      }
      final String errorStr = new String(bytes, StandardCharsets.UTF_8);
      log.debug("Error content from AX: {}", errorStr);
      return MoreObjects.firstNonNull(SagJSONUtil.convertJsonToObject(errorStr, ErrorInfo.class),
          new ErrorInfo());
    };
  }
}
