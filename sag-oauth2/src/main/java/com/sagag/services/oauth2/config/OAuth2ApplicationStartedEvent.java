package com.sagag.services.oauth2.config;

import com.sagag.eshop.service.config.EnableMailService;
import com.sagag.services.common.event.SagApplicationStartedEvent;
import com.sagag.services.elasticsearch.profiles.EsExternalArticleServiceMode;
import com.sagag.services.oauth2.profiles.OAuth2ExternalAuthenticatorMode;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ApplicationStartedEvent extends SagApplicationStartedEvent {

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    super.onApplicationEvent(event);
    printAnnotatedRegisteredBean(
        new Class<?>[] { OAuth2ExternalAuthenticatorMode.class,
          EsExternalArticleServiceMode.class, EnableMailService.class },
        event.getApplicationContext());
  }

}
