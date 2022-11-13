package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.service.api.ExternalOrganisationService;
import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.ExternalOrganisationDto;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.mdm.client.MdmResponseException;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.mdm.utils.DvseUserUtils;
import com.sagag.services.service.api.DvseBusinessService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceClientException;

import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class DvseBusinessServiceImpl implements DvseBusinessService {

  @Autowired
  private ExternalOrganisationService externalOrganisationService;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private DvseUserService dvseUserService;

  @Autowired
  private ExternalOrganisationRepository extOrganisationRepo;

  @Override
  public void createDvseUserInfo(long userId, final SupportedAffiliate affiliate,
      final int orgId) throws UserValidationException, MdmCustomerNotFoundException {

    final String extCustId = extOrganisationRepo
        .findExternalCustomerIdByOrgIdAndExternalApp(orgId, ExternalApp.DVSE);
    try {
      if (!dvseUserService.existDvseCustomerId(extCustId)) {
        throw new MdmCustomerNotFoundException(extCustId);
      }
      final String username = generateUniqueUserName(extCustId);
      final String password = DvseUserUtils.generateRandomPassword();

      dvseUserService.createUser(extCustId, username, password,
          affiliate);

      final ExternalUserDto externalUserDto = ExternalUserDto.builder().eshopUserId(userId)
          .externalApp(ExternalApp.DVSE).username(username).password(password).active(false)
          .createdDate(Calendar.getInstance().getTime()).build();
      externalUserService.addExternalUser(externalUserDto);
    } catch (final WebServiceClientException | MdmResponseException ex) {
      log.error("Create new MDM user for customer has error: ", ex);
      throw new UserValidationException(UserErrorCase.UE_MUC_001, "Create new MDM user failed");
    }
  }

  private String generateUniqueUserName(String dvseCustomerId) throws UserValidationException {
    String username = null;
    boolean isValidUsername = false;

    for (int i = 0; i < DvseUserUtils.MAX_UNIQUE_NAME_RETRY; i++) {
      username = DvseUserUtils.generateRandomUsername();
      isValidUsername = !externalUserService.isUsernameExisted(username)
          && !dvseUserService.existDvseUsername(dvseCustomerId, username);
      if (isValidUsername) {
        break;
      }
    }
    if (!isValidUsername) {
      throw new UserValidationException(UserErrorCase.UE_MUC_001,
          "Failed to generate unique user name");
    }

    return username;
  }

  @Override
  public void createVirtualDvseUserPool(final int orgId, final String externalCustomerId,
      SupportedAffiliate affiliate, int virtualPoolSize) {
    Date now = Calendar.getInstance().getTime();
    final String orgCode = organisationService.findOrgCodeById(orgId);
    final int virtualUserExisted = externalUserService.countVirtualUserExisted(orgCode);
    if (virtualUserExisted >= virtualPoolSize) {
      return;
    }

    log.info("Start creating virtual DvseUser Pool for customer {}", orgCode);
    for (int i = 0; i < virtualPoolSize - virtualUserExisted; i++) {
      final String username = DvseUserUtils.generateRandomUsername();
      final String password = DvseUserUtils.generateRandomPassword();
      dvseUserService.createUser(externalCustomerId, username, password, affiliate);

      final ExternalUserDto externalUserDto = ExternalUserDto.builder().eshopUserId(null)
          .externalApp(ExternalApp.DVSE).username(username).password(password).active(false)
          .createdDate(now).lockVirtualUser(buildLockVirtualUser(orgCode)).build();
      externalUserService.addExternalUser(externalUserDto);
    }
  }

  private static String buildLockVirtualUser(String orgCode) {
    return new StringBuilder(orgCode).append(SagConstants.UNDERSCORE).append(StringUtils.EMPTY)
        .toString();
  }

  @Override
  public String createDvseCustomerInfo(int organisationId, SupportedAffiliate affiliate) {

    final String customerName = DvseUserUtils.generateRandomCustomerName();
    final String customerId = dvseUserService.createCustomer(customerName, affiliate);

    final ExternalOrganisationDto externalOrg = new ExternalOrganisationDto();
    externalOrg.setOrgId(organisationId);
    externalOrg.setExternalCustomerId(customerId);
    externalOrg.setExternalCustomerName(customerName);
    externalOrg.setExternalApp(ExternalApp.DVSE);
    externalOrganisationService.addExternalOrganisation(externalOrg);
    return customerId;
  }
}
