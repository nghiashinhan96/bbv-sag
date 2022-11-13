package com.sagag.eshop.service.sso;

import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SsoLoginProfileServiceImpl implements SsoLoginProfileService {

  private static final String SAG_COLLECTION_SHORTNAME = "sag";

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private SsoLoginProfileServiceCaseOne ssoLoginProfileServiceCaseOne;

  @Autowired
  private SsoLoginProfileServiceCaseTwo ssoLoginProfileServiceCaseTwo;

  @Override
  public SsoLoginProfileResponseDto createProfile(SsoLoginProfileRequestDto request) {
    String ssoCaseOneValue =
        orgCollectionService
            .findSettingValueByCollectionShortnameAndKey(SAG_COLLECTION_SHORTNAME,
                SettingsKeys.Affiliate.Settings.SSO_CASE_ONE.toLowerName())
            .orElse(StringUtils.EMPTY);
    if (Boolean.parseBoolean(ssoCaseOneValue)) {
      return ssoLoginProfileServiceCaseOne.process(request);
    }
    return ssoLoginProfileServiceCaseTwo.process(request);
  }
}
