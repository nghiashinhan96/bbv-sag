package com.sagag.services.tools.batch.sales.dvse_user;


import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.mdm.ExternalUserDto;
import com.sagag.services.tools.domain.target.ExternalOrganisationDto;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.domain.target.VUserDetail;
import com.sagag.services.tools.exception.MdmCustomerNotFoundException;
import com.sagag.services.tools.exception.UserValidationException;
import com.sagag.services.tools.exception.UserValidationException.UserErrorCase;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.service.DvseUserService;
import com.sagag.services.tools.service.ExternalOrganisationService;
import com.sagag.services.tools.service.ExternalUserService;
import com.sagag.services.tools.support.ExternalApp;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.utils.DvseUserUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

@Component
@StepScope
@Slf4j
@Transactional
@OracleProfile
public class RegisterDvseUserProcessor implements ItemProcessor<VUserDetail, VUserDetail> {

  @Autowired
  private ExternalOrganisationService externalOrganisationService;

  @Autowired
  private OrganisationRepository organisationRepository;

  @Autowired
  private DvseUserService dvseUserService;

  @Autowired
  private ExternalUserService externalUserService;

  @Override
  public VUserDetail process(final VUserDetail vUserDetail) throws Exception {
    final Optional<Organisation> affOrgOpt = organisationRepository.findOneById(vUserDetail.getOrgParentId());

    final Optional<SupportedAffiliate> aff = SupportedAffiliate.fromDesc(affOrgOpt.get().getShortname());
    if (!aff.isPresent() || !aff.get().isDvseAffiliate()) {
      log.warn("This customer {} is not belongs to DVSE affiliate: ", affOrgOpt.get().getShortname());
      return vUserDetail;
    }
    createDvseUserInfo(vUserDetail, aff.get());
    return vUserDetail;
  }

  private void createDvseUserInfo(VUserDetail vUserDetail, final SupportedAffiliate affiliate) throws UserValidationException, MdmCustomerNotFoundException {
    final int orgId = vUserDetail.getOrgId();
    final Optional<ExternalOrganisationDto> exOrganisationOpt = externalOrganisationService.getExternalOrganisationByOrgId(orgId);
    if (!exOrganisationOpt.isPresent()) {
      return;
    }
    final ExternalOrganisationDto exOrganisation = exOrganisationOpt.get();
    try {
      final String extCustId = exOrganisation.getExternalCustomerId();
      if (!dvseUserService.existDvseCustomerId(extCustId)) {
        return;
      }
      final String username = generateUniqueUserName(exOrganisation.getExternalCustomerId());
      final String password = DvseUserUtils.generateRandomPassword();

      dvseUserService.createUser(exOrganisation.getExternalCustomerId(), username, password, affiliate);

      final ExternalUserDto externalUserDto = ExternalUserDto.builder().eshopUserId(vUserDetail.getUserId()).externalApp(ExternalApp.DVSE).username(username).password(password)
          .active(false).createdDate(Calendar.getInstance().getTime()).build();
      externalUserService.addExternalUser(externalUserDto);
    } catch (final Exception ex) {
      log.error("Create new MDM user for customer has error: ", ex);
      throw new UserValidationException(UserErrorCase.UE_MUC_001, "Create new MDM user failed");
    }
  }

  private String generateUniqueUserName(String dvseCustomerId) throws UserValidationException {
    String username = null;
    boolean isValidUsername = false;

    for (int i = 0; i < DvseUserUtils.MAX_UNIQUE_NAME_RETRY; i++) {
      username = DvseUserUtils.generateRandomUsername();
      isValidUsername = !externalUserService.isUsernameExisted(username) && !dvseUserService.existDvseUsername(dvseCustomerId, username);
      if (isValidUsername) {
        break;
      }
    }
    if (!isValidUsername) {
      throw new UserValidationException(UserErrorCase.UE_MUC_001, "Failed to generate unique user name");
    }
    return username;
  }


}
