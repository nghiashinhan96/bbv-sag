package com.sagag.services.common.profiles;

import com.sagag.services.common.enums.country.ErpType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is the default profile of Connect if not specify with other profiles.
 *
 * <pre>When we have multiple implementations for the specific interface by profiles.
 * Please mark the one of them with {@link AxProfile} as default at runtime.</pre>
 *
 * @see {@link AtProfile} for Austria profile
 * @see {@link ChProfile} for Swiss profile
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ErpProfile({ ErpType.DYNAMIC_AX, ErpType.WINT })
public @interface AxProfile {

}
