package com.sagag.services.tools.batch.user.happy_points;

import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.sagsys.SourceUserSettings;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.UserSettingsRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
@SagsysProfile
@Slf4j
public class HappyPointUserSettingsMigrationItemProcessor
    implements ItemProcessor<SourceUserSettings, UserSettings> {

  @Autowired
  private UserSettingsRepository targetRepo;

  @Override
  public UserSettings process(SourceUserSettings item) throws Exception {
    log.debug("The user setting ID = {}", item.getId());
    UserSettings target = targetRepo.findById(item.getId())
        .orElse(null);

    if (target == null) {
      return null;
    }
    target.setAcceptHappyPointTerm(item.getHasPartnerProgramLogin());
    target.setShowHappyPoints(item.getHasPartnerProgramView());
    return target;
  }

}
