package com.sagag.services.tools.batch.user;

import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.UserSettingsRepository;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@StepScope
@SagsysProfile
public class UserSettingItemWriter implements ItemWriter<UserSettings> {

  @Autowired
  private UserSettingsRepository userSettingsRepository;

  @Override
  public void write(List<? extends UserSettings> items) throws Exception {
    userSettingsRepository.saveAll(items.stream().filter(Objects::nonNull)
        .collect(Collectors.toList()));
  }

}
