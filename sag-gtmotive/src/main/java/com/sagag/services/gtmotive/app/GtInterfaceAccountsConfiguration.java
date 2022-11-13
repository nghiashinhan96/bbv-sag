package com.sagag.services.gtmotive.app;

import com.sagag.services.gtmotive.config.GtmotiveProfile;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("external.webservice.gtmotive")
@GtmotiveProfile
public class GtInterfaceAccountsConfiguration extends GtAccountsConfiguration {
}
