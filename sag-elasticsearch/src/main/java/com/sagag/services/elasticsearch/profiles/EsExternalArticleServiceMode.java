package com.sagag.services.elasticsearch.profiles;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(EsExternalArticleServiceModeCondition.class)
public @interface EsExternalArticleServiceMode {

  String value() default "default";
}
