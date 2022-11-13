package com.sagag.services.oauth2.profiles;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@OAuth2ExternalAuthenticatorMode("autonet")
public @interface AutonetExternalAuthenticatorMode {

}
