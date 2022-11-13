package com.sagag.services.mdm.api.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.mdm.api.DvseUserService;
import com.sagag.services.mdm.client.MdmClient;
import com.sagag.services.mdm.client.MdmResponseException;
import com.sagag.services.mdm.client.MdmUpdateMode;
import com.sagag.services.mdm.config.MdmAccountConfigData;
import com.sagag.services.mdm.dto.DvseCustomer;
import com.sagag.services.mdm.dto.DvseCustomerSearchResult;
import com.sagag.services.mdm.dto.DvseCustomerUser;
import com.sagag.services.mdm.dto.DvseCustomerUserInfo;
import com.sagag.services.mdm.dto.DvseMainModule;
import com.sagag.services.mdm.exception.MdmUserNotFoundException;
import com.sagag.services.mdm.utils.DvseUriBuilder;
import com.sagag.services.mdm.utils.MdmUtils;
import com.sagag.services.mdm.utils.UserModuleBuilder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * The service to create customer and user for DVSE.
 * </p>
 *
 */
@Service
@Slf4j
public class DvseUserServiceImpl implements DvseUserService, InitializingBean {

  private static final String SEARCH_ONLINE_USER = "3";

  private static final int NO_DATA_FOUND_ERROR_CODE = -998005;

  @Autowired
  private MdmAccountConfigData mdmAccConfigData;

  @Autowired
  private MdmClient mdmClient;

  private String mdmUsername;

  private String mdmPassword;

  private String catalogUri;

  private Map<String, String> templateCustomerIdMap;

  private Map<String, String> catalogIdMap;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.mdmUsername = this.mdmAccConfigData.getMdmUsername();
    this.mdmPassword = this.mdmAccConfigData.getMdmPassword();
    this.catalogUri = this.mdmAccConfigData.getCatalogUri();
    this.templateCustomerIdMap = MapUtils.emptyIfNull(
        this.mdmAccConfigData.getTemplateCustomerIdMap());
    this.catalogIdMap = MapUtils.emptyIfNull(
        this.mdmAccConfigData.getCatalogIdMap());
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

  @Override
  public void removeCustomer(final String customerId) {

    createNewSession(sessionId -> {

      // Load customer data to get his trader id.
      final DvseCustomer existingCustomer = mdmClient.getCustomer(sessionId, customerId);

      if (existingCustomer == null) {
        throw new NoSuchElementException("Not found customer with customer id = " + customerId);
      }

      // Delete selected customer based on template
      final DvseCustomer tobeDeletedCustomer = new DvseCustomer();
      tobeDeletedCustomer.setTraderId(existingCustomer.getTraderId());
      tobeDeletedCustomer.setCustomerId(customerId);

      return mdmClient.createOrDeleteCustomer(sessionId, tobeDeletedCustomer, MdmUpdateMode.DELETE);
    });
  }

  @Override
  public DvseCustomerUserInfo createUser(final String customerId, final String username,
      final String password, final SupportedAffiliate affiliate) {

    return createNewSession(sessionId -> {

      // Load template user
      final DvseCustomerUser templateCustomerUser = loadTemplateUser(sessionId, affiliate);
      final List<DvseMainModule> templateMainModules =
          createModulesForUser(username, password, templateCustomerUser);

      // Create new user based on template
      final DvseCustomerUser newCustomerUser = new DvseCustomerUser();
      newCustomerUser.setModules(templateMainModules);
      newCustomerUser.setSubModules(templateCustomerUser.getSubModules());

      return createUser(sessionId, customerId, newCustomerUser);
    });
  }

  private DvseCustomerUserInfo createUser(final String sessionId, final String customerId,
      final DvseCustomerUser user) {

    final List<DvseCustomerUserInfo> userInfos = mdmClient.createOrDeleteCustomerUsers(sessionId,
        customerId, Arrays.asList(user), MdmUpdateMode.CREATE);

    if (CollectionUtils.isEmpty(userInfos)) {
      throw new MdmResponseException("Response the list of user infos has empty");
    }

    return userInfos.get(0);
  }

  @Override
  public void removeUser(final String customerId, final String username, final String password) {

    createNewSession(sessionId -> {

      // Before we can delete a user we need to get his sequence number.
      final DvseCustomer customer = mdmClient.getCustomer(sessionId, customerId);
      DvseCustomerUserInfo userInfo = findFirstUserInfo(customer, username);
      if (userInfo == null) {
        return Collections.emptyList();
      }
      final DvseCustomerUser tobeDeletedCustomerUser = new DvseCustomerUser();
      tobeDeletedCustomerUser.setSeqNumber(userInfo.getSeqNumber());
      tobeDeletedCustomerUser
          .setModules(Arrays.asList(UserModuleBuilder.build(username, password)));

      return mdmClient.createOrDeleteCustomerUsers(sessionId, customerId,
          Arrays.asList(tobeDeletedCustomerUser), MdmUpdateMode.DELETE);
    });
  }

  private DvseCustomerUserInfo findFirstUserInfo(DvseCustomer customer, String username) {
    try {
      return customer.getUsersInfos().stream().filter(filterByUsernameIgnoreCase(username))
          .findFirst().orElseThrow(() -> new MdmUserNotFoundException(username));
    } catch (MdmUserNotFoundException mdmNfEx) {
      final String warningMsg = String.format("Username %s was not found in MDM sytem.", username);
      log.warn(warningMsg, mdmNfEx); // log warning and continue
      return null; // user not exist in MDM system. we will not request to MDM to delete anymore
    }
  }

  private static Predicate<DvseCustomerUserInfo> filterByUsernameIgnoreCase(final String username) {
    return info -> StringUtils.equalsIgnoreCase(info.getUsername(), username);
  }

  @Override
  public String getDvseCatalogUri(SupportedAffiliate affiliate, String username, String password,
      String saleId) {
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.hasText(username, "The given username must not be empty");
    Assert.hasText(password, "The given password must not be empty");
    if (StringUtils.isBlank(catalogUri)) {
      return StringUtils.EMPTY;
    }
    return DvseUriBuilder.builder().uri(catalogUri)
        .sid(loadCatalogSid(affiliate))
        .username(username)
        .password(password)
        .saleId(saleId)
        .build().getUri();
  }

  private <T> T createNewSession(final Function<String, T> function) {
    final String sessionId = mdmClient.getSessionId(mdmUsername, mdmPassword);
    try {
      return function.apply(sessionId);
    } finally {
      mdmClient.invalidateSessionId(sessionId);
    }
  }

  private static List<DvseMainModule> createModulesForUser(final String username,
      final String password, final DvseCustomerUser templateUser) {
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

  private DvseCustomerUser loadTemplateUser(final String sessionId,
      final SupportedAffiliate supportedAffiliate) {
    return mdmClient.getCustomerUsers(sessionId, loadTemplateCustomerId(supportedAffiliate))
        .stream().filter(user -> StringUtils.equalsIgnoreCase("1", user.getSeqNumber())).findFirst()
        .orElseThrow(() -> new NoSuchElementException("Not found template user from DVSE"));
  }

  private String loadTemplateCustomerId(final SupportedAffiliate affiliate) {
    return Optional.ofNullable(this.templateCustomerIdMap.get(affiliate.getAffiliate()))
        .orElseThrow(() -> new UnsupportedOperationException("Not support for this affiliate"));
  }

  @Override
  public boolean existDvseUsername(String dvseCusomerId, String dvseUsername) {
    return createNewSession(sessionId -> {
      final DvseCustomer customer = mdmClient.getCustomer(sessionId, dvseCusomerId);
      try {
        List<DvseCustomerSearchResult> dvseCustomerSearchResult = mdmClient.getCustomerSearchResult(
            sessionId, customer.getTraderId(), SEARCH_ONLINE_USER, dvseUsername);
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
  public boolean existDvseCustomerId(String dvseCusomerId) {
    return createNewSession(sessionId -> {
      try {
        return !Objects.isNull(mdmClient.getCustomer(sessionId, dvseCusomerId));
      } catch (MdmResponseException e) {
        return false;
      }
    });
  }

  private String loadCatalogSid(SupportedAffiliate affiliate) {
    return Optional.ofNullable(this.catalogIdMap.get(affiliate.getAffiliate()))
        .orElseThrow(() -> new UnsupportedOperationException("Not support affiliate"));
  }

}
