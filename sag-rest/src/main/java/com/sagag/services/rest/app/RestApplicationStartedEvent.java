package com.sagag.services.rest.app;

import com.sagag.eshop.service.config.EnableMailService;
import com.sagag.services.common.event.SagApplicationStartedEvent;
import com.sagag.services.common.profiles.DisableForProfile;
import com.sagag.services.elasticsearch.profiles.EsExternalArticleServiceMode;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.haynespro.config.HaynesProProfile;
import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.oates.config.OatesProfile;
import com.sagag.services.service.profiles.CategoryConverterMode;
import com.sagag.services.service.profiles.UserLoaderModeProfile;
import com.sagag.services.thule.config.ThuleProfile;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class RestApplicationStartedEvent extends SagApplicationStartedEvent {

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    super.onApplicationEvent(event);
    printAnnotatedRegisteredBean(
        new Class<?>[] { CategoryConverterMode.class, UserLoaderModeProfile.class,
          EsExternalArticleServiceMode.class, GtmotiveProfile.class,
          OatesProfile.class, HaynesProProfile.class, IncentiveProfile.class,
          ThuleProfile.class, EnableMailService.class, DisableForProfile.class },
        event.getApplicationContext());

  }

}
