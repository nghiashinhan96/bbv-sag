package com.sagag.services.tools.batch.cleaner.partner_program.customer;

import com.sagag.services.tools.domain.csv.CsvPartnerProgramSetting;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.repository.target.CustomerSettingsRepository;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@StepScope
public class CleanPartnerProgramCustomerSettingProcessor
    implements ItemProcessor<CsvPartnerProgramSetting, Optional<CustomerSettings>> {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Override
  public Optional<CustomerSettings> process(CsvPartnerProgramSetting item) throws Exception {
    return customerSettingsRepo
        .findCustomerSettingByPartnerProgramAndOrgCode(!item.getCustomerSettingHasPartnerProgram(), item.getOrgCode());
  }
}
