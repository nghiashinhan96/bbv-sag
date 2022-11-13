package com.sagag.services.haynespro.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@HaynesProProfile
@ConfigurationProperties(prefix = "haynespro")
public class HaynesProInternalProperties {

  private String callbackUrl;

  private HaynesProLicenseProperty license;
}
