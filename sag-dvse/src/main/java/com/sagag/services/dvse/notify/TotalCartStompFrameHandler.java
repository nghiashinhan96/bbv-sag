package com.sagag.services.dvse.notify;

import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import com.sagag.services.dvse.dto.dvse.TotalCartNotifyInfo;

public class TotalCartStompFrameHandler implements StompFrameHandler {

  @Override
  public Type getPayloadType(StompHeaders headers) {
    return TotalCartNotifyInfo.class;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    /* intentionally blank */
  }
}
