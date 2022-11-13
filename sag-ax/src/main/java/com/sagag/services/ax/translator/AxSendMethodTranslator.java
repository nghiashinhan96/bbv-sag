package com.sagag.services.ax.translator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.SendMethod;

/**
 * The mapper class to map AX definition of send method to Connect payment.
 *
 */
@Component
public abstract class AxSendMethodTranslator implements AxDataTranslator<String, String> {

  protected static final String CONNECT_TOUR_SEND_METHOD = "TOUR";

  protected static final String CONNECT_PICKUP_SEND_METHOD = "PICKUP";

  protected static final String CONNECT_COURIER_SEND_METHOD = "COURIER";

  @Override
  public String translateToAx(String connectSendMethod) {
    if (StringUtils.equals(CONNECT_PICKUP_SEND_METHOD, connectSendMethod)) {
      return SendMethod.ABH.name();
    } else if (StringUtils.equals(CONNECT_TOUR_SEND_METHOD, connectSendMethod)) {
      return SendMethod.TOUR.name();
    } else if (StringUtils.equals(CONNECT_COURIER_SEND_METHOD, connectSendMethod)) {
      return SendMethod.COURIER.name();
    }

    throw new UnsupportedOperationException("No support this send method = " + connectSendMethod);
  }

}