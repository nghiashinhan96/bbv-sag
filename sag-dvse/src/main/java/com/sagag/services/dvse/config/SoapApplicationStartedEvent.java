package com.sagag.services.dvse.config;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

import com.sagag.eshop.service.config.EnableMailService;
import com.sagag.services.common.event.SagApplicationStartedEvent;
import com.sagag.services.elasticsearch.profiles.EsExternalArticleServiceMode;

@Component
public class SoapApplicationStartedEvent extends SagApplicationStartedEvent {

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    super.onApplicationEvent(event);
    printAnnotatedRegisteredBean(
        new Class<?>[] { EsExternalArticleServiceMode.class, EnableMailService.class },
        event.getApplicationContext());
  }

}
