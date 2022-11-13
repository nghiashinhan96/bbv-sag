package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.utils.SagJSONUtil;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

@UtilityClass
public final class EsLogUtils {

  public static void logJson(Logger log, Object obj) {
    logJson(log, StringUtils.EMPTY, obj);
  }

  public static void logJson(Logger log, String msg, Object obj) {
    if (log == null || obj == null) {
      return;
    }
    log.debug(StringUtils.defaultIfBlank(msg, "Result = {}"),
        SagJSONUtil.convertObjectToPrettyJson(obj));
  }
}
