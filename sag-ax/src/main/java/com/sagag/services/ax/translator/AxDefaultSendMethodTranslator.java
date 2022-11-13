package com.sagag.services.ax.translator;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.SendMethod;
import com.sagag.services.common.profiles.DynamicAxProfile;

/**
 * The mapper class to map AX definition of send method to Connect payment.
 *
 */
@Component
@DynamicAxProfile
public class AxDefaultSendMethodTranslator extends AxSendMethodTranslator {

  private static final SendMethod[] AX_TOUR_SEND_METHOD =
      new SendMethod[] { SendMethod.TOUR };

  private static final SendMethod[] AX_PICKUP_SEND_METHOD =
      new SendMethod[] { SendMethod.ABH };

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
    return Stream.of(sendMethods)
        .anyMatch(sendMethod -> StringUtils.equalsIgnoreCase(sendMethod.name(), axSendMethod));
  }

}