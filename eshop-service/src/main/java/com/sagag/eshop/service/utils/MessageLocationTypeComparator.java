package com.sagag.eshop.service.utils;

import com.sagag.eshop.service.enums.MessageLocationTypeEnum;
import com.sagag.services.domain.eshop.message.dto.MessageDto;

import java.util.Comparator;

/**
 * MessageLocationType Comparator
 *
 */
public class MessageLocationTypeComparator implements Comparator<MessageDto> {

  @Override
  public int compare(MessageDto m1, MessageDto m2) {
    // prefer smaller MessageLocationType
    return MessageLocationTypeEnum.fromDesc(m2.getLocationType())
        .compareTo(MessageLocationTypeEnum.fromDesc(m1.getLocationType()));
  }

}
