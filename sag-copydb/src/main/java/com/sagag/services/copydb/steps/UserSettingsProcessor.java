package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.UserSettings;
import com.sagag.services.copydb.domain.dest.DestUserSettings;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class UserSettingsProcessor implements ItemProcessor<UserSettings, DestUserSettings> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestUserSettings process(UserSettings item) throws Exception {
    return dozerBeanMapper.map(item, DestUserSettings.class);
  }
}
