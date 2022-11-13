package com.sagag.services.dvse.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.dvse.dto.dvse.TotalCartNotifyInfo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DvseNotifyEndpoint {

  @Autowired
  private SimpMessagingTemplate template;

  @MessageMapping("/notifyQuickViewCart")
  public void notifyQuickViewCart(TotalCartNotifyInfo dto) {
    log.info("notifyQuickViewCart method invoked with param \n{}",
      SagJSONUtil.convertObjectToPrettyJson(dto));
    template.convertAndSend("/topic/notifyQuickViewCart/" + dto.getUserId(), dto.getTotal());
  }
}
