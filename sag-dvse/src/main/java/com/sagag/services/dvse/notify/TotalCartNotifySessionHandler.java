package com.sagag.services.dvse.notify;

import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.sagag.services.dvse.dto.dvse.TotalCartNotifyInfo;

@Slf4j
public class TotalCartNotifySessionHandler extends StompSessionHandlerAdapter {

  private final TotalCartNotifyInfo notifyObject;

  private final AtomicReference<Throwable> failure = new AtomicReference<>();

  public TotalCartNotifySessionHandler(TotalCartNotifyInfo notifyObject) {
    this.notifyObject = notifyObject;
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    this.failure.set(new Exception(headers.toString()));
  }

  @Override
  public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p,
    Throwable ex) {
    this.failure.set(ex);
  }

  @Override
  public void handleTransportError(StompSession session, Throwable ex) {
    this.failure.set(ex);
  }

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    log.debug("Total {}", notifyObject.getTotal());
    session.subscribe("/topic/notifyQuickViewCart" + notifyObject.getUserId(), new TotalCartStompFrameHandler());
    session.send("/store/notifyQuickViewCart", notifyObject);
    log.debug("Sent status is ok ! {}", session.isConnected());
  }

}
