package com.sagag.services.tools.batch.cleaner.partner_program.customer;

import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.repository.target.CustomerSettingsRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@StepScope
@Component
public class CleanPartnerProgramCustomerSettingWriter implements ItemWriter<Optional<CustomerSettings>> {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepository;

  @Override
  public void write(List<? extends Optional<CustomerSettings>> items) throws Exception {
    List<CustomerSettings> customerSettings = items.stream().filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    customerSettings.forEach(setting -> setting.setHasPartnerprogramView(!setting.isHasPartnerprogramView()));

    if (CollectionUtils.isNotEmpty(customerSettings)) {
      customerSettingsRepository.saveAll(customerSettings);
    }
  }
}
