package com.sagag.services.gtmotive.config;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile({ "gtmotive-at-dev", "gtmotive-at-pre", "gtmotive-at-prod",
  "gtmotive-ch-dev", "gtmotive-ch-pre", "gtmotive-ch-prod",
  "gtmotive-cz-dev", "gtmotive-cz-pre", "gtmotive-cz-prod"})
public @interface GtmotiveProfile {

}
