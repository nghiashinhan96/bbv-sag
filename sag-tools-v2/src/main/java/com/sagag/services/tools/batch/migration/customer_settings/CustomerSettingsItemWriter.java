package com.sagag.services.tools.batch.migration.customer_settings;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.repository.target.CustomerSettingsRepository;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@StepScope
@OracleProfile
public class CustomerSettingsItemWriter implements ItemWriter<CustomerSettings> {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepository;

  @Override
  public void write(List<? extends CustomerSettings> items) throws Exception {
    customerSettingsRepository.saveAll(items.stream().filter(Objects::nonNull).collect(Collectors.toList()));
  }
}
