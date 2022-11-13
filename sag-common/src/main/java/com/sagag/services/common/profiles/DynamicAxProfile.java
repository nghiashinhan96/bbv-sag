package com.sagag.services.common.profiles;

import com.sagag.services.common.enums.country.ErpType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ErpProfile({ ErpType.DYNAMIC_AX })
public @interface DynamicAxProfile {

}
