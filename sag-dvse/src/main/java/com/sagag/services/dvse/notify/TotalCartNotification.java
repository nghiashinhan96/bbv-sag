package com.sagag.services.dvse.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.sagag.services.dvse.config.SoapProperties;
import com.sagag.services.dvse.dto.dvse.TotalCartNotifyInfo;

@Component
public class TotalCartNotification {

  @Autowired
  @Qualifier("stompClient")
  private WebSocketStompClient stompClient;

  @Autowired
  private SoapProperties soapProps;

  public void notifyQuickViewCart(final String userId, final int total) {
    Assert.hasText(userId, "The given user id must not be empty");
    final TotalCartNotifyInfo dto = new TotalCartNotifyInfo();
    dto.setUserId(userId);
    dto.setTotal(total);
    stompClient.connect(soapProps.getWebSocket().getEndpoint(), new WebSocketHttpHeaders(),
      new TotalCartNotifySessionHandler(dto));
  }

}
