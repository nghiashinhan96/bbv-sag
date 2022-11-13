package com.sagag.services.dvse.profiles;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile({ "country-at", "country-ch", "country-sb", "country-autonet", "country-ax-cz" })
public @interface DefaultDvseProfile {

}
