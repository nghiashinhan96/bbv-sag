package com.sagag.services.rest.authorization.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@PreAuthorize("hasPermission(#request, 'isAccessibleUrl')")
public @interface IsAccessibleUrlPreAuthorization {

}
