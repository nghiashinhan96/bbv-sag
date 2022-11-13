package com.sagag.services.tools.batch.cleaner.mdm_user;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.CustomExtOrganisation;
import com.sagag.services.tools.domain.target.CustomMdmCustomerAndUsersResponse;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.exception.MdmResponseException;
import com.sagag.services.tools.repository.target.ExternalUserRepository;
import com.sagag.services.tools.service.DvseUserService;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Component
@StepScope
@Slf4j
@Transactional
@OracleProfile
public class CleanMdmCustomerAndUsersProcessor implements ItemProcessor<CustomExtOrganisation, CustomMdmCustomerAndUsersResponse> {

  @Autowired
  private ExternalUserRepository extUserRepo;

  @Autowired
  private DvseUserService dvseUserService;

  @Override
  public CustomMdmCustomerAndUsersResponse process(CustomExtOrganisation extOrg) {
    log.debug("Processing for extOrg = {}", JsonUtils.convertObjectToPrettyJson(extOrg));
    if (extOrg == null) {
      return null;
    }

    final SupportedAffiliate affiliate = SupportedAffiliate.fromCompanyName(extOrg.getCompanyName());

    // Find all external users by customer
    final List<ExternalUser> extUsers = extUserRepo.findByOrgId(extOrg.getOrgId());
    final List<String> notWritenCustomerNumberList = new ArrayList<>();

    // Clean all found users and found customer
    try {
      dvseUserService.cleanCustomerAndUsers(extOrg, extUsers, affiliate);
    } catch (MdmResponseException e) {
      log.error("error:", e);
      notWritenCustomerNumberList.add(extOrg.getExtCustName());
    }

    // Create new customer and update new customer info into existing record
    CustomMdmCustomerAndUsersResponse response = 
        dvseUserService.createCustomerAndUsers(extOrg, extUsers, affiliate);
    response.setExtOrgId(extOrg.getId());
    response.setExtUsers(extUsers);
    response.setNotWritenCustomerNumberList(notWritenCustomerNumberList);

    return response;
  }
}
