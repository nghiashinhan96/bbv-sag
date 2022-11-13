package com.sagag.services.tools.batch.cleaner.partner_program.user;

import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.UserSettingsRepository;

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
public class CleanPartnerProgramUserSettingWriter implements ItemWriter<Optional<UserSettings>> {

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Override
  public void write(List<? extends Optional<UserSettings>> items) throws Exception {
    List<UserSettings> userSettings = items.stream().filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    userSettings.forEach(setting -> setting.setShowHappyPoints(!setting.isShowHappyPoints()));

    if (CollectionUtils.isNotEmpty(userSettings)) {
      userSettingsRepo.saveAll(userSettings);
    }
  }
}
