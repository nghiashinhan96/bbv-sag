package com.sagag.services.service.cart.support;

import com.sagag.eshop.repo.hz.entity.ShopType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ShopTypeDefault {

  ShopType value() default ShopType.DEFAULT_SHOPPING_CART;

}
