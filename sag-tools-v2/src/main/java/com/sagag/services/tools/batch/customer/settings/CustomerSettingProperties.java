package com.sagag.services.tools.batch.customer.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sag.change.customer-settings")
public class CustomerSettingProperties {

  private String affiliate;

  private String settingColumn;

  private String settingValue;
}
