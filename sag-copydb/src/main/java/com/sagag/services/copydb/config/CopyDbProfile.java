package com.sagag.services.copydb.config;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile({ "copydb" })
public @interface CopyDbProfile {
}
