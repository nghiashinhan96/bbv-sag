package com.sagag.services.tools.ax.translator;

import java.util.stream.Stream;

import com.sagag.services.tools.support.SendMethod;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The mapper class to map AX definition of send method to Connect payment.
 *
 */
@Component
public class AxSendMethodTranslator implements AxDataTranslator<String, String> {

  private static final SendMethod[] AX_TOUR_SEND_METHOD =
      new SendMethod[] { SendMethod.TOUR };

  private static final SendMethod[] AX_PICKUP_SEND_METHOD =
      new SendMethod[] { SendMethod.ABH };

  private static final String CONNECT_TOUR_SEND_METHOD = "TOUR";

  private static final String CONNECT_PICKUP_SEND_METHOD = "PICKUP";

  @Override
  public String translateToConnect(String axSendMethod) {

    if (anyMatchSendMethod(AX_TOUR_SEND_METHOD, axSendMethod)) {
      return CONNECT_TOUR_SEND_METHOD;
    } else if (anyMatchSendMethod(AX_PICKUP_SEND_METHOD, axSendMethod)) {
      return CONNECT_PICKUP_SEND_METHOD;
    }

    return StringUtils.EMPTY;
  }

  private static boolean anyMatchSendMethod(SendMethod[] sendMethods, String axSendMethod) {
    return Stream.of(sendMethods).anyMatch(sendMethod -> StringUtils.equalsIgnoreCase(sendMethod.name(), axSendMethod));
  }

  @Override
  public String translateToAx(String connectSendMethod) {
    if (StringUtils.equals(CONNECT_PICKUP_SEND_METHOD, connectSendMethod)) {
      return SendMethod.ABH.name();
    } else if (StringUtils.equals(CONNECT_TOUR_SEND_METHOD, connectSendMethod)) {
      return SendMethod.TOUR.name();
    }

    throw new UnsupportedOperationException("No support this send method = " + connectSendMethod);
  }

}
