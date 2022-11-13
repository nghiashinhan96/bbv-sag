package com.sagag.services.admin.app;

import com.sagag.eshop.service.config.EnableMailService;
import com.sagag.services.common.event.SagApplicationStartedEvent;
import com.sagag.services.elasticsearch.profiles.EsExternalArticleServiceMode;
import com.sagag.services.service.profiles.CategoryConverterMode;
import com.sagag.services.service.profiles.UserLoaderModeProfile;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class AdminApplicationStartedEvent extends SagApplicationStartedEvent {

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    super.onApplicationEvent(event);
    printAnnotatedRegisteredBean(
        new Class<?>[] { CategoryConverterMode.class, UserLoaderModeProfile.class,
          EsExternalArticleServiceMode.class, EnableMailService.class },
        event.getApplicationContext());
  }

}
