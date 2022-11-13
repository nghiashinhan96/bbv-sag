package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LicenseSettings;
import com.sagag.services.copydb.domain.dest.DestLicenseSettings;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LicenseSettingsProcessor implements ItemProcessor<LicenseSettings, DestLicenseSettings> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestLicenseSettings process(LicenseSettings item) throws Exception {
    return dozerBeanMapper.map(item, DestLicenseSettings.class);
  }
}
