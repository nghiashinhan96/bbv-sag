package com.sagag.services.ax;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * The http client configuration for Ax component.
 * </p>
 *
 * <pre>NOTE: Scope for this class just only sag-ax component</pre>
 *
 */
@Configuration
@Profile("test")
@ComponentScan(value = "com.sagag")
public class AxTestConfiguartion {

}
