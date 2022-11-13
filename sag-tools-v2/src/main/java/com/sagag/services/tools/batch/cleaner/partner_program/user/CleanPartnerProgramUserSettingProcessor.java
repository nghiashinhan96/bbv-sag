package com.sagag.services.tools.batch.cleaner.partner_program.user;

import com.sagag.services.tools.domain.csv.CsvPartnerProgramSetting;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.UserSettingsRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@StepScope
public class CleanPartnerProgramUserSettingProcessor
    implements ItemProcessor<CsvPartnerProgramSetting, Optional<UserSettings>> {

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Override
  public Optional<UserSettings> process(CsvPartnerProgramSetting item) throws Exception {
    List<UserSettings> userSettings = userSettingsRepo
        .findUserSettingByPartnerProgram(!item.getUserSettingHasPartnerProgram(), item.getUserName(),
            item.getOrgCode());

    if (CollectionUtils.size(userSettings) > 1) {
      userSettings.stream().forEach(setting -> setting.setShowHappyPoints(!setting.isShowHappyPoints()));
      userSettingsRepo.saveAll(userSettings);
    } else if (CollectionUtils.size(userSettings) == 1) {
      return Optional.of(userSettings.get(0));
    }
    return Optional.empty();
  }
}
