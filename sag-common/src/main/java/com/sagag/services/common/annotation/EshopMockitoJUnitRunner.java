package com.sagag.services.common.annotation;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class indicating the Unit Test runner customize for Eshop.
 * <p>
 * TODO(use Silent mode to avoid correct all other places of UTs when upgrading from mockito 1.x to
 * 2.x). However, should use StrictStubs mode as Mockito's recommendation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RunWith(MockitoJUnitRunner.Silent.class)
public @interface EshopMockitoJUnitRunner {
}
