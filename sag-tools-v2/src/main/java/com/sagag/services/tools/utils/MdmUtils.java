package com.sagag.services.tools.utils;

import java.time.Clock;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.tools.domain.mdm.DvseRequestHeader;
import com.sagag.services.tools.support.SagConstants;

import lombok.experimental.UtilityClass;

/**
 * Class to provide some utility features in MDM web services.
 *
 */
@UtilityClass
public final class MdmUtils {

  public static final String SYSTEM = "testing";

  public static final String FUNCTION_PREFIX = "MDM.KndVerw.V3.Extern.WebMethods.Customer";

  public static String fullFuctionName(final String functionName) {
    return StringUtils.join(ArrayUtils.toArray(FUNCTION_PREFIX,
        stripFunctionVersion(functionName), functionName), SagConstants.DOT);
  }

  public static String stripFunctionVersion(final String functionName) {
    return functionName.replaceFirst("_V\\d+", "");
  }

  public static String now() {
    return LocalDateTime.now(Clock.systemUTC()).toString();
  }

  public static String fullFrom() {
    return LocalDateTime.now(Clock.systemUTC()).minusMinutes(1).toString();
  }

  public static <T> String functionFromAnnotation(Class<T> clazz) {
    return clazz.getAnnotation(XmlRootElement.class).name();
  }

  public static <T> String toHeader(Class<T> clazz, String timestamp, String sessionId) {

    final DvseRequestHeader header = new DvseRequestHeader();
    header.setSid(sessionId);
    header.setTimestamp(timestamp);
    header.setFunction(fullFuctionName(functionFromAnnotation(clazz)));

    return XmlUtils.marshal(header);
  }

}
