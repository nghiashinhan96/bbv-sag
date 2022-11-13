package com.sagag.services.common.profiles;

import com.sagag.services.common.enums.country.ErpType;
import com.sagag.services.common.profiles.condition.ErpProfileCondition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(ErpProfileCondition.class)
public @interface ErpProfile {

  ErpType[] value();

}
