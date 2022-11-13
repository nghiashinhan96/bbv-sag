package com.sagag.services.tools.service.impl;


import com.sagag.services.tools.client.MdmClient;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.mdm.DvseCustomer;
import com.sagag.services.tools.domain.mdm.DvseCustomerSearchResult;
import com.sagag.services.tools.domain.mdm.DvseCustomerUser;
import com.sagag.services.tools.domain.mdm.DvseCustomerUserInfo;
import com.sagag.services.tools.domain.mdm.DvseMainModule;
import com.sagag.services.tools.domain.target.CustomExtOrganisation;
import com.sagag.services.tools.domain.target.CustomMdmCustomerAndUsersResponse;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.exception.MdmResponseException;
import com.sagag.services.tools.service.DvseUserService;
import com.sagag.services.tools.support.MdmUpdateMode;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.utils.DvseUserUtils;
import com.sagag.services.tools.utils.MdmUtils;
import com.sagag.services.tools.utils.UserModuleBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * The service to create customer and user for DVSE.
 * </p>
 *
 */
@Service
@OracleProfile
public class DvseUserServiceImpl implements DvseUserService {

  private static final String SEARCH_ONLINE_USER = "3";

  private static final int NO_DATA_FOUND_ERROR_CODE = -998005;

  @Value("${external.webservice.dvse.mdm_username}")
  private String mdmUsername;

  @Value("${external.webservice.dvse.mdm_password}")
  private String mdmPassword;

  @Value("${external.webservice.dvse.mdm_template_customer_id}")
  private String mdmTemplateCustomerId;

  @Value("${external.webservice.dvse.catalog_uri}")
  private String catalogUri;

  @Value("${external.webservice.dvse.mdm_template_customer_id_matik}")
  private String mdmTemplateCustomerIdMatik;

  @Value("${external.webservice.dvse.mdm_template_customer_id_matik_ch}")
  private String mdmTemplateCustomerIdMatikCh;

  @Autowired
  private MdmClient mdmClient;

  @Override
  public DvseCustomerUserInfo createUser(final String customerId, final String username, final String password, final SupportedAffiliate affiliate) {
    return createNewSession(sessionId -> {

      // Load template user
      final DvseCustomerUser templateCustomerUser = loadTemplateUser(sessionId, affiliate);
      final List<DvseMainModule> templateMainModules = createModulesForUser(username, password, templateCustomerUser);

      // Create new user based on template
      final DvseCustomerUser newCustomerUser = new DvseCustomerUser();
      newCustomerUser.setModules(templateMainModules);
      newCustomerUser.setSubModules(templateCustomerUser.getSubModules());

      return createUser(sessionId, customerId, newCustomerUser);
    });
  }

  private static List<DvseMainModule> createModulesForUser(final String username, final String password, final DvseCustomerUser templateUser) {
    if (CollectionUtils.isEmpty(templateUser.getModules())) {
      throw new NoSuchElementException("Template module must not be empty");
    }

    return templateUser.getModules().stream().map(module -> {

      if (UserModuleBuilder.isUserModule(module)) {
        // replace user module with new one
        return UserModuleBuilder.build(username, password);
      } else {

        // we need to modify "fullFrom" of a module to current time
        // or nothing will work for the user
        if (module.getFullFrom() != null) {
          module.setFullFrom(MdmUtils.fullFrom());
        }

        return module;
      }
    }).collect(Collectors.toList());
  }


  private DvseCustomerUser loadTemplateUser(final String sessionId, final SupportedAffiliate supportedAffiliate) {
    return mdmClient.getCustomerUsers(sessionId, loadTemplateCustomerId(supportedAffiliate)).stream()
        .filter(user -> StringUtils.equalsIgnoreCase("1", user.getSeqNumber())).findFirst()
        .orElseThrow(() -> new NoSuchElementException("Not found template user from DVSE"));
  }

  private String loadTemplateCustomerId(final SupportedAffiliate affiliate) {
    switch (affiliate) {
      case DERENDINGER_AT:
        return mdmTemplateCustomerId;
      case MATIK_AT:
        return mdmTemplateCustomerIdMatik;
      case MATIK_CH:
        return mdmTemplateCustomerIdMatikCh;
      default:
        throw new UnsupportedOperationException("Not support for this affiliate");
    }
  }

  private DvseCustomerUserInfo createUser(final String sessionId, final String customerId, final DvseCustomerUser user) {
    try {
      List<DvseCustomerUserInfo> userInfos = mdmClient.createOrDeleteCustomerUsers(sessionId, customerId, Arrays.asList(user), MdmUpdateMode.CREATE);
      if (CollectionUtils.isEmpty(userInfos)) {
        throw new Exception("Response the list of user infos has empty");

      }
      return userInfos.get(0);
    } catch (Exception e) {
    }
    return new DvseCustomerUserInfo();
  }

  @Override
  public boolean existDvseCustomerId(String dvseCusomerId) {
    return createNewSession(sessionId -> {
      try {
        return !Objects.isNull(mdmClient.getCustomer(sessionId, dvseCusomerId));
      } catch (Exception e) {
        return false;
      }
    });
  }

  private <T> T createNewSession(final Function<String, T> function) {
    final String sessionId = mdmClient.getSessionId(mdmUsername, mdmPassword);
    try {
      return function.apply(sessionId);
    } finally {
      mdmClient.invalidateSessionId(sessionId);

    }
  }

  @Override
  public boolean existDvseUsername(String dvseCusomerId, String dvseUsername) {
    return createNewSession(sessionId -> {
      final DvseCustomer customer = mdmClient.getCustomer(sessionId, dvseCusomerId);
      try {
        List<DvseCustomerSearchResult> dvseCustomerSearchResult =
            mdmClient.getCustomerSearchResult(sessionId, customer.getTraderId(), SEARCH_ONLINE_USER, dvseUsername);
        return !CollectionUtils.isEmpty(dvseCustomerSearchResult);
      } catch (MdmResponseException e) {
        if (e.getErrorCode() == NO_DATA_FOUND_ERROR_CODE) {
          return false;
        }
        throw e;
      }
    });
  }

  @Override
  public boolean cleanCustomerAndUsers(CustomExtOrganisation extOrg, List<ExternalUser> externalUsers, SupportedAffiliate affiliate) {
    return createNewSession(sessionId -> {

      // Load template user
      final DvseCustomerUser templateCustomerUser = loadTemplateUser(sessionId, affiliate);

      final List<DvseCustomerUser> deletedUsers = externalUsers.stream().map(extUser -> {
        // Create new user based on template
        final List<DvseMainModule> templateMainModules = createModulesForUser(extUser.getUsername(), extUser.getPassword(), templateCustomerUser);
        final DvseCustomerUser deletedCustomerUser = new DvseCustomerUser();
        deletedCustomerUser.setModules(templateMainModules);
        deletedCustomerUser.setSubModules(templateCustomerUser.getSubModules());
        return deletedCustomerUser;
      }).collect(Collectors.toList());

      final String custId = extOrg.getExtCustomerId();

      List<DvseCustomerUserInfo> userInfos = mdmClient.createOrDeleteCustomerUsers(sessionId, custId, deletedUsers, MdmUpdateMode.DELETE);
      if (CollectionUtils.isEmpty(userInfos)) {
        return false;
      }

      // Load customer data to get his trader id.
      final DvseCustomer existingCustomer = mdmClient.getCustomer(sessionId, custId);
      if (existingCustomer == null) {
        throw new NoSuchElementException("Not found customer with customer id = " + custId);
      }

      // Delete selected customer based on template
      final DvseCustomer tobeDeletedCustomer = new DvseCustomer();
      tobeDeletedCustomer.setTraderId(existingCustomer.getTraderId());
      tobeDeletedCustomer.setCustomerId(custId);
      final String tobeDeletedCustId = mdmClient.createOrDeleteCustomer(sessionId, tobeDeletedCustomer, MdmUpdateMode.DELETE);

      return StringUtils.isNotBlank(tobeDeletedCustId);
    });
  }

  @Override
  public CustomMdmCustomerAndUsersResponse createCustomerAndUsers(CustomExtOrganisation extOrg, List<ExternalUser> externalUsers, SupportedAffiliate affiliate) {
    return createNewSession(sessionId -> {

      // Create customer
      // Load template customer on which new customer will be based.
      final DvseCustomer templateCustomer =
          mdmClient.getCustomer(sessionId, loadTemplateCustomerId(affiliate));

      if (templateCustomer == null) {
        throw new NoSuchElementException("Not found template customer info");
      }

      CustomMdmCustomerAndUsersResponse res = new CustomMdmCustomerAndUsersResponse();

      final String custName = DvseUserUtils.generateRandomCustomerName();
      // Create new customer based on template
      final DvseCustomer newCustomer = new DvseCustomer();
      newCustomer.setCustomerName(custName);
      newCustomer.setTraderId(templateCustomer.getTraderId());
      newCustomer.setModules(templateCustomer.getModules());
      final String custId = mdmClient.createOrDeleteCustomer(sessionId, newCustomer, MdmUpdateMode.CREATE);
      res.addCustomer(custId, custName);

      final Map<Long , DvseCustomerUser> dvseCustUsers = new HashMap<>();
      final List<Long> userIds = externalUsers.stream().map(ExternalUser::getEshopUserId).collect(Collectors.toList());
      for (Long userId : userIds) {

        final String username = DvseUserUtils.generateRandomUsername();
        final String password = DvseUserUtils.generateRandomPassword();
        // Create users
        // Load template user
        final DvseCustomerUser templateCustomerUser = loadTemplateUser(sessionId, affiliate);
        final List<DvseMainModule> templateMainModules =
            createModulesForUser(username, password, templateCustomerUser);

        // Create new user based on template
        final DvseCustomerUser newCustomerUser = new DvseCustomerUser();
        newCustomerUser.setModules(templateMainModules);
        newCustomerUser.setSubModules(templateCustomerUser.getSubModules());
        dvseCustUsers.putIfAbsent(userId, newCustomerUser);
      }

      res.setDvseCustUsers(dvseCustUsers);

      final List<DvseCustomerUserInfo> userInfos = mdmClient.createOrDeleteCustomerUsers(sessionId,
          custId, dvseCustUsers.values().stream().collect(Collectors.toList()), MdmUpdateMode.CREATE);

      for (DvseCustomerUserInfo user : userInfos) {
        res.addUser(user);
      }

      return res;
    });
  }

  @Override
  public String createCustomer(final String customerName, final SupportedAffiliate affiliate) {

    return createNewSession(sessionId -> {

      // Load template customer on which new customer will be based.
      final DvseCustomer templateCustomer =
          mdmClient.getCustomer(sessionId, loadTemplateCustomerId(affiliate));

      if (templateCustomer == null) {
        throw new NoSuchElementException("Not found template customer info");
      }

      // Create new customer based on template
      final DvseCustomer newCustomer = new DvseCustomer();
      newCustomer.setCustomerName(customerName);
      newCustomer.setTraderId(templateCustomer.getTraderId());
      newCustomer.setModules(templateCustomer.getModules());

      return mdmClient.createOrDeleteCustomer(sessionId, newCustomer, MdmUpdateMode.CREATE);
    });
  }

}
