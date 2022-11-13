package com.sagag.services.oauth2.config;

import com.sagag.services.common.cors.SagCorsFilter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OAuth2CorsFilter extends SagCorsFilter {

}
