package com.sagag.services.tools.batch.cleaner.mdm_user;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.CustomMdmCustomerAndUsersResponse;
import com.sagag.services.tools.domain.target.CustomMdmCustomerAndUsersResponse.ExtMdmUser;
import com.sagag.services.tools.domain.target.ExternalOrganisation;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.repository.target.ExternalOrganisationRepository;
import com.sagag.services.tools.repository.target.ExternalUserRepository;
import com.sagag.services.tools.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Component
@StepScope
@Slf4j
@Transactional
@OracleProfile
public class CleanMdmCustomerAndUsersItemWriter implements ItemWriter<CustomMdmCustomerAndUsersResponse> {

  private static final String FILE_PATH = SystemUtils.getUserDir() + "/csv/not_writen_cust_name.txt";

  @Autowired
  private ExternalOrganisationRepository extOrgRepo;

  @Autowired
  private ExternalUserRepository extUserRepo;

  @Override
  public void write(List<? extends CustomMdmCustomerAndUsersResponse> responses) throws Exception {
    log.debug("Start writing the result list = {}", JsonUtils.convertObjectToPrettyJson(responses));
    if (CollectionUtils.isEmpty(responses)) {
      return;
    }

    final String notWritenStrList = responses.stream()
        .flatMap(item -> item.getNotWritenCustomerNumberList().stream())
        .collect(Collectors.joining("\n"));

    writeTxt(notWritenStrList);

    // Update new customer info into existing record and save them
    final List<ExternalOrganisation> savedExtOrgs = new ArrayList<>();
    final List<ExternalUser> savedExtUsers = new ArrayList<>();
    for (CustomMdmCustomerAndUsersResponse response : responses) {
      ExternalOrganisation foundExtOrg = extOrgRepo.findById(response.getExtOrgId()).orElse(null);
      Assert.notNull(foundExtOrg, "The found ext org must not be null");
      foundExtOrg.setExternalCustomerId(response.getCustId());
      foundExtOrg.setExternalCustomerName(response.getCustName());
      savedExtOrgs.add(foundExtOrg);

      for (ExternalUser extUser : response.getExtUsers()) {
        final ExtMdmUser user = response.getUserById(extUser.getEshopUserId());
        extUser.setUsername(user.getUsername());
        extUser.setPassword(user.getPassword());
        extUser.setActive(false);
        extUser.setCreatedDate(Calendar.getInstance().getTime());
        savedExtUsers.add(extUser);
      }
    }

    List<ExternalUser> outputUsers = extUserRepo.saveAll(savedExtUsers);
    log.debug("Saving the ext user list = {}", JsonUtils.convertObjectToPrettyJson(outputUsers));

    List<ExternalOrganisation> outputOrgs = extOrgRepo.saveAll(savedExtOrgs);
    log.debug("Saving the ext org list = {}", JsonUtils.convertObjectToPrettyJson(outputOrgs));
  }

  private void writeTxt(final String notWritenStrList) throws IOException {
    File file = new File(FILE_PATH);
    if (!file.exists()) {
      file.createNewFile();
    } else {
      file.delete();
      file.createNewFile();
    }
    FileUtils.writeStringToFile(file, notWritenStrList, StandardCharsets.UTF_8, true);
  }

}
