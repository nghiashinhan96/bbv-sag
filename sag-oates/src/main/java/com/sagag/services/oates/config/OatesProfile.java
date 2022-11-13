package com.sagag.services.oates.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile({ "oates-prod-ch" , "oates-prod-at", "oates-pre-ch", "oates-pre-at",
  "oates-prod-cz", "oates-pre-cz", "oates-prod-sb", "oates-pre-sb" })
public @interface OatesProfile {

}
