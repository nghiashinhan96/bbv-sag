package com.sagag.services.ax.translator.wint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.wint.WtSendMethod;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.profiles.WintProfile;

import lombok.extern.slf4j.Slf4j;

@Component
@WintProfile
@Slf4j
public class WintSendMethodTranslator extends AxSendMethodTranslator {

  @Override
  public String translateToConnect(String wtSendMethod) {
    log.debug("Map wint send method = {} to connect payment type for customer", wtSendMethod);
    if (StringUtils.isBlank(wtSendMethod)) {
      final SendMethodType defaultSendMethod = SendMethodType.PICKUP;
      log.warn("The given Wint send method is empty - default to {}", defaultSendMethod);
      return defaultSendMethod.name();
    }

    switch (WtSendMethod.findByCode(wtSendMethod)) {
      case TOUR:
        return SendMethodType.TOUR.name();
      case PICKUP:
      default:
        return SendMethodType.PICKUP.name();
    }
  }

}