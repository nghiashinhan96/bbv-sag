package com.sagag.eshop.service.api.inner;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class ExternalUserPoolHelper {

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private OrganisationService organisationService;

  @Transactional
  public void assignDvseUserForVirtualUser(EshopUser eshopUser) {
    final Long eshopUserId = eshopUser.getId();
    final Organisation organisation = organisationService.getFirstByUserId(eshopUserId)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("No organisation available for %d", eshopUserId)));
    final String orgCode = organisation.getOrgCode();
    Optional<ExternalUserDto> externalUserOpt =
        externalUserService.getAvailableVirtualUser(orgCode);
    if (!externalUserOpt.isPresent()) {
      log.warn("Cannot find available external account for virtual user of orgCode = {}", orgCode);
      return;
    }
    final ExternalUserDto externalUser = externalUserOpt.get();
    externalUser.setActive(true);
    externalUser.setEshopUserId(eshopUser.getId());
    externalUser.setLockVirtualUser(buildOccupiedLockVirtualUser(orgCode, eshopUserId));
    externalUserService.updateExternalUsers(Arrays.asList(externalUser));
  }

  public static String buildOccupiedLockVirtualUser(String orgCode, Long eshopUserId) {
    final String userIdStr =
        (eshopUserId == null) ? StringUtils.EMPTY : String.valueOf(eshopUserId);
    return new StringBuilder(orgCode).append(SagConstants.UNDERSCORE).append(userIdStr).toString();
  }

}
