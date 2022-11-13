package com.sagag.services.tools.support;

import com.sagag.services.tools.domain.csv.CsvAddress;
import com.sagag.services.tools.domain.csv.CsvFinalCustomer;
import com.sagag.services.tools.domain.csv.CsvOrganisationAddress;
import com.sagag.services.tools.domain.csv.CsvOrganisationLink;
import com.sagag.services.tools.domain.csv.CsvRoleAssignment;
import com.sagag.services.tools.domain.target.Currency;
import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.CurrencyRepository;
import com.sagag.services.tools.repository.target.LanguageRepository;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.service.TargetOrganisationService;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.EnvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CommonInitialDataLoader {

  private static final String IMPORT_DIR = SystemUtils.getUserDir() + "/csv/import/";

  private static final String FINAL_CUSTOMER_DIR = "/final_customer/";

  private static final String ORGANISATION_LINK_FILE = "/ORGANISATIONLINK_201711231607.csv";

  private static final String ROLE_ASSIGNMENT_FILE = "/LOGIN_ROLEASSIGNMENT_201711231607.csv";

  private static final String ORGANISATION_ADDRESS_FILE = "/ORGANISATION_ADDRESS_201711271143.csv";

  private static final String ADDRESS_FILE = "/ADDRESS_201711221747.csv";

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Value("#{'${sag.customer.numbers:}'.split(',')}")
  private List<String> customerNumberList;

  @Autowired
  private CurrencyRepository currencyRepo;

  @Autowired
  private LanguageRepository langRepo;

  @Autowired
  private MappingUserIdEblConnectRepository mappingUserIdEblConnectRepo;

  @Autowired
  private TargetOrganisationService targetOrganisationService;

  @Deprecated
  public CommonInitialResource load() throws BatchJobException {
    CommonInitialResource commonInitialResource = new CommonInitialResource();
    commonInitialResource.setCurrencyIdMap(loadCurrenciesMap());
    commonInitialResource.setLanguageMap(loadLangMap());
    // Just load real id with prod env
    if (EnvUtils.isProductionEnv(activeProfile)) {
      List<MappingUserIdEblConnect> connects = mappingUserIdEblConnectRepo.findAll();
      commonInitialResource.setOrganisationIdMap(loadOrgIdMap(connects));
      commonInitialResource.setMappingUserIdMap(loadMappingUser(connects));
      commonInitialResource.setSourceOrgSet(getSourceOrgSet(connects));
    }
    return commonInitialResource;
  }

  private Set<Long> getSourceOrgSet(final List<MappingUserIdEblConnect> connects) {
    log.info("Load source organisation id data");
    return connects.stream().map(connect -> connect.getEblOrgId()).collect(Collectors.toSet());
  }

  private Map<String, Integer> loadCurrenciesMap() {
    log.info("Load currency data");
    List<Currency> currencies = currencyRepo.findAll();
    Map<String, Integer> currenciesMap = new HashMap<>();
    for (Currency currency : currencies) {
      currenciesMap.putIfAbsent(currency.getIsoCode(), currency.getId());
    }
    return currenciesMap;
  }

  private Map<String, Integer> loadLangMap() {
    log.info("Load languages data");
    Map<String, Integer> langMap = new HashMap<>();
    langRepo.findAll().forEach(entity -> langMap.putIfAbsent(entity.getLangiso().toLowerCase(), entity.getId()));
    return langMap;
  }

  private Map<Long, MappingUserIdEblConnect> loadMappingUser(final List<MappingUserIdEblConnect> connects) {
    log.info("Load mapping user table");
    Map<Long, MappingUserIdEblConnect> map = new HashMap<>();
    for (MappingUserIdEblConnect connect : connects) {
      map.putIfAbsent(connect.getEblUserId(), connect);
    }
    return map;
  }

  private Map<Long, Integer> loadOrgIdMap(List<MappingUserIdEblConnect> connects) {
    log.info("Load organisation data");
    Map<Long, Integer> endedOrgMap = new HashMap<>();
    connects.forEach(connect -> endedOrgMap.putIfAbsent(connect.getEblOrgId(), connect.getConnectOrgId()));
    return endedOrgMap;
  }

  public Map<Long, CsvFinalCustomer> loadFinalCustomers() throws BatchJobException {
    log.info("Load final customers data");
    final File finalCustomersDir = new File(IMPORT_DIR + FINAL_CUSTOMER_DIR);
    if (!finalCustomersDir.isDirectory()) {
      throw new IllegalArgumentException("Not found any final customer dir");
    }
    final Map<Long, CsvFinalCustomer> finalCustomerMap = new HashMap<>();
    File[] files = finalCustomersDir.listFiles();
    for (File file : files) {
      List<CsvFinalCustomer> finalCustomers = CsvUtils.read(file, CsvFinalCustomer.class);
      if (CollectionUtils.isEmpty(finalCustomers)) {
        continue;
      }

      finalCustomers.forEach(csvFinalCustomer -> {
        log.debug("Final customer = {}", csvFinalCustomer);
        finalCustomerMap.putIfAbsent(csvFinalCustomer.getId(), csvFinalCustomer);
      });
    }
    return finalCustomerMap;
  }

  public Map<Long, CsvOrganisationLink> loadOrgLinks() throws BatchJobException {
    log.info("Load organisation links data");
    final File file = new File(IMPORT_DIR + ORGANISATION_LINK_FILE);
    validate(file);
    final Map<Long, CsvOrganisationLink> links = new HashMap<>();
    List<CsvOrganisationLink> csvLinks = CsvUtils.read(file, CsvOrganisationLink.class);
    csvLinks.stream().forEach(link -> {
      log.debug("Organisation link = {}", link);
      links.put(link.getClientId(), link);
    });
    return links;
  }

  public Map<Long, CsvRoleAssignment> loadRoleAssignment() throws BatchJobException {
    log.info("Load roles assignments data");
    final File file = new File(IMPORT_DIR + ROLE_ASSIGNMENT_FILE);
    validate(file);
    final Map<Long, CsvRoleAssignment> roles = new HashMap<>();
    List<CsvRoleAssignment> csvRoles = CsvUtils.read(file, CsvRoleAssignment.class);
    csvRoles.stream().filter(role -> "CLIENTINFO".equalsIgnoreCase(role.getRole())).forEach(role -> {
      log.debug("Role Assignment = {}", role);
      roles.put(role.getPersonId(), role);
    });
    return roles;
  }


  public Map<Long, Long> loadOrganisationAddressMapping() throws BatchJobException {
    final File file = new File(IMPORT_DIR + ORGANISATION_ADDRESS_FILE);
    validate(file);
    final Map<Long, Long> orgAddrMap = new HashMap<>();
    List<CsvOrganisationAddress> organisationAddresses = CsvUtils.read(file, CsvOrganisationAddress.class);

    organisationAddresses.stream().forEach(addr -> orgAddrMap.putIfAbsent(addr.getOrganisationId(), addr.getAddressId()));

    return orgAddrMap;
  }

  public Map<Long, CsvAddress> loadAddressMapping() throws BatchJobException {
    final File file = new File(IMPORT_DIR + ADDRESS_FILE);
    validate(file);
    final Map<Long, CsvAddress> addressMap = new HashMap<>();
    List<CsvAddress> organisationAddresses = CsvUtils.read(file, StandardCharsets.UTF_8, CsvAddress.class);

    organisationAddresses.stream().forEach(addr -> addressMap.putIfAbsent(addr.getId(), addr));

    return addressMap;
  }

  private void validate(File file) {
    if (!file.exists()) {
      throw new IllegalArgumentException("Not found this file");
    }
  }

  @Deprecated
  public AvailablecusCustomerResource loadAvailablecusCustomerResource() {
    if (!isValidCustomerNumberList(customerNumberList)) {
      log.warn("Not have any customers information");
      return new AvailablecusCustomerResource(Collections.emptyList());
    }
    log.debug("customer number list = {}", customerNumberList);
    final List<String> distinctCustomerNrList = customerNumberList.stream().distinct().map(StringUtils::trim).collect(Collectors.toList());
    final List<Integer> availableOrgIdList = targetOrganisationService.findOrganisationIdHasOfferPermission(distinctCustomerNrList);

    if (CollectionUtils.isEmpty(availableOrgIdList)) {
      throw new IllegalArgumentException("The given list of customer number don't have permission to use offer module function");
    }

    final List<MappingUserIdEblConnect> connects = mappingUserIdEblConnectRepo.findByConnectOrgIdIn(availableOrgIdList);
    return new AvailablecusCustomerResource(connects);
  }

  private static boolean isValidCustomerNumberList(final List<String> customerNumberList) {
    if (CollectionUtils.isEmpty(customerNumberList) || CollectionUtils.size(customerNumberList) > ToolConstants.MAX_SIZE_OF_PROCESSED_LIST) {
      return false;
    }
    for (String cusNr : customerNumberList) {
      if (StringUtils.isBlank(cusNr)) {
        return false;
      }
    }
    return true;
  }
}
