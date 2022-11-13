package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.AddressTypeRepository;
import com.sagag.eshop.repo.api.EshopAddressRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopPermissionRepository;
import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.GroupRoleRepository;
import com.sagag.eshop.repo.api.OrganisationAddressRepository;
import com.sagag.eshop.repo.api.OrganisationGroupRepository;
import com.sagag.eshop.repo.api.OrganisationPropertyRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.OrganisationTypeRepository;
import com.sagag.eshop.repo.entity.AddressType;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopAddress;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationAddress;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.repo.entity.OrganisationProperty;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.builder.AddressBuilder;
import com.sagag.eshop.service.converter.OrganisationConverters;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.CustomerSearchResultItemDto;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;
import com.sagag.services.domain.eshop.criteria.CustomerProfileSearchCriteria;
import com.sagag.services.domain.eshop.criteria.CustomerSearchSortableColumn;
import com.sagag.services.domain.eshop.dto.OrgPropertyOfferDto;
import com.sagag.services.domain.eshop.dto.OrganisationPropertyDto;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

@Service
public class OrganisationServiceImpl implements OrganisationService {

  private static final String CUSTOMER_NUMBER_EXPRESSION = "orgCode";

  private static final String ORGANISATION_NAME_EXPRESSION = "name";

  private static final String AFFILIATE_NAME_EXPRESSION = "parent.shortname";

  /* Common error message */
  private static final String CUSTOMER_ORG_NOT_NULL = "The given customer must not be null";

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private OrganisationPropertyRepository orgPropertyRepo;

  @Autowired
  private OrganisationTypeRepository orgTypeRepo;

  @Autowired
  private EshopRoleRepository roleRepo;

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private EshopGroupRepository groupRepo;

  @Autowired
  private GroupRoleRepository groupRoleRepo;

  @Autowired
  private OrganisationGroupRepository orgGroupRepo;

  @Autowired
  private EshopAddressRepository addressRepo;

  @Autowired
  private AddressTypeRepository addressTypeRepo;

  @Autowired
  private OrganisationAddressRepository orgAddressRepo;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private EshopPermissionRepository eshopPermissionRepo;

  @Override
  @Transactional
  public Optional<Organisation> findOrganisationByOrgCode(String orgCode) {
    return orgRepo.findOneByOrgCode(orgCode);
  }

  @Override
  @Transactional
  public Page<CustomerSearchResultItemDto> searchCustomerProfile(
      CustomerProfileSearchCriteria customerSearchForm) {
    if (customerSearchForm == null) {
      customerSearchForm = new CustomerProfileSearchCriteria();
    }

    Pageable pageable = createPaging(customerSearchForm);
    Page<Organisation> organisations = orgRepo.findByNameAndOrgCodeAndAffiliate(
        StringUtils.defaultString(customerSearchForm.getCompanyName()),
        StringUtils.defaultString(customerSearchForm.getCustomerNr()),
        StringUtils.defaultString(customerSearchForm.getAffiliate()), pageable);
    return organisations.map(OrganisationConverters.customerSearchConverter());
  }

  @Override
  @Transactional
  public Optional<Organisation> getFirstByUserId(Long userId) {
    if (userId == null) {
      return Optional.empty();
    }
    return orgRepo.findAllByUserId(userId).stream().findFirst();
  }

  @Override
  @Transactional
  public Optional<Organisation> getByOrgId(int id) {
    return orgRepo.findByOrgId(id);
  }

  @Override
  @Transactional
  public List<Organisation> findOrganisationByTypeName(String orgTypeName) {
    if (StringUtils.isBlank(orgTypeName)) {
      return Collections.emptyList();
    }
    return orgRepo.findOrganisationsByType(orgTypeName);
  }

  @Override
  @Transactional
  public Optional<String> searchCompanyName(String shortname) {
    if (StringUtils.isBlank(shortname)) {
      return Optional.empty();
    }
    return orgRepo.findCompanyNameByShortname(shortname);
  }

  @Override
  @Transactional
  public OrgPropertyOfferDto findOrganisationPropertiesById(final Long id) {
    final List<OrganisationProperty> properties = orgPropertyRepo.findByOrganisationId(id);
    final List<OrganisationPropertyDto> orgPropertiesDto =
        properties.stream().map(orgProp -> SagBeanUtils.map(orgProp, OrganisationPropertyDto.class))
            .collect(Collectors.toList());

    return new OrgPropertyOfferDto(orgPropertiesDto);
  }

  private Pageable createPaging(CustomerProfileSearchCriteria criteria) {
    return PageRequest.of(criteria.getPage(), criteria.getSize(), getSort(criteria.getSorting()));
  }

  private Sort getSort(CustomerSearchSortableColumn sorting) {

    Sort result = Sort.unsorted();
    if (sorting != null) {
      List<Order> builder = new ArrayList<>();

      builder.add(getOrder(sorting.getOrderByCustomerNumberDesc(), CUSTOMER_NUMBER_EXPRESSION));
      builder.add(getOrder(sorting.getOrderByOrganisationNameDesc(), ORGANISATION_NAME_EXPRESSION));
      builder.add(getOrder(sorting.getOrderByAffiliateNameDesc(), AFFILIATE_NAME_EXPRESSION));
      List<Order> orders = builder.stream().filter(Objects::nonNull).collect(Collectors.toList());

      if (!orders.isEmpty()) {
        result = Sort.by(orders);
      }

    }
    return result;
  }

  private Order getOrder(Boolean isDescending, String columnExpression) {
    Direction sortDirection = Direction.ASC; // default direction
    if (isDescending != null && isDescending.booleanValue()) {
      sortDirection = Direction.DESC;
    }
    return new Order(sortDirection, columnExpression);
  }

  @Override
  @Transactional
  public Organisation createCustomer(SupportedAffiliate affiliate, String customerNr,
      String companyName, CustomerSettings custSettings) {
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.hasText(customerNr, "The given custNr must not be empty");
    Assert.notNull(custSettings, "The given cust settings must not be null");

    final String shortname = "customer-" + customerNr;

    final Integer affiliateId = orgRepo.findIdByShortName(affiliate.getAffiliate())
        .orElseThrow(() -> new IllegalArgumentException(""));

    final Organisation customer = new Organisation();
    customer.setName(companyName);
    customer.setOrgCode(customerNr);
    customer.setOrganisationType(orgTypeRepo.getCustomerOrgType());
    customer.setParentId(affiliateId);
    customer.setDescription(companyName);
    customer.setShortname(shortname);
    customer.setCustomerSettings(custSettings);
    return orgRepo.save(customer);
  }

  @Override
  @Transactional
  public void assignCustomerGroupAndDefaultPermission(Organisation customer,
      List<PermissionEnum> defaultPermission) {
    Assert.notNull(customer, CUSTOMER_ORG_NOT_NULL);

    final Map<EshopUserCreateAuthority, EshopGroup> eshopGroups =
        Stream.of(EshopUserCreateAuthority.normalValues()).collect(Collectors.toMap(
            authority -> authority, authority -> EshopGroup.buildGroupBy(customer, authority)));
    final List<EshopGroup> savedUserGroups = groupRepo.saveAll(eshopGroups.values());

    permissionService.setPermissions(savedUserGroups, defaultPermission, true);

    final List<String> roles = Arrays.asList(EshopUserCreateAuthority.normalValues()).stream()
        .map(EshopUserCreateAuthority::name).collect(Collectors.toList());
    final List<EshopRole> eshopRoles = roleRepo.findEshopRolesByName(roles);
    if (CollectionUtils.isEmpty(eshopRoles)) {
      return;
    }

    final List<GroupRole> eshopGroupRoles =
        eshopRoles.stream().map(groupRoleConverter(eshopGroups)).collect(Collectors.toList());
    groupRoleRepo.saveAll(eshopGroupRoles);

    final List<OrganisationGroup> orgGroups = Stream.of(EshopUserCreateAuthority.normalValues())
        .map(organisationGroupConverter(eshopGroups, customer)).collect(Collectors.toList());
    orgGroupRepo.saveAll(orgGroups);
  }

  private static Function<EshopRole, GroupRole> groupRoleConverter(
      final Map<EshopUserCreateAuthority, EshopGroup> eshopGroups) {
    return role -> {
      final GroupRole groupRole = new GroupRole();
      groupRole.setEshopGroup(eshopGroups.get(EshopUserCreateAuthority.valueOf(role.getName())));
      groupRole.setEshopRole(role);
      return groupRole;
    };
  }

  private static Function<EshopUserCreateAuthority, OrganisationGroup> organisationGroupConverter(
      final Map<EshopUserCreateAuthority, EshopGroup> eshopGroups, Organisation customer) {
    return authority -> {
      final OrganisationGroup orgGroup = new OrganisationGroup();
      orgGroup.setEshopGroup(eshopGroups.get(authority));
      orgGroup.setOrganisation(customer);
      return orgGroup;
    };
  }

  @Override
  @Transactional
  public List<OrganisationAddress> createCustomerAddresses(Organisation customer,
      List<Address> addresses) {
    Assert.notNull(customer, CUSTOMER_ORG_NOT_NULL);
    Assert.notEmpty(addresses, "The given addresses must not be empty");

    final List<EshopAddress> eshopAddresses =
        addresses.stream().map(eshopAddressConverter()).collect(Collectors.toList());
    addressRepo.saveAll(eshopAddresses);

    final List<OrganisationAddress> orgAddresses =
        eshopAddresses.stream().map(orgAddressConverter(customer)).collect(Collectors.toList());
    return orgAddressRepo.saveAll(orgAddresses);
  }

  private Function<Address, EshopAddress> eshopAddressConverter() {
    return address -> {
      Optional<AddressType> addressTypeOpt =
          addressTypeRepo.findAddressByType(address.getAddressTypeCode());
      final AddressType addressType = addressTypeOpt.get();

      final EshopAddress addressEntity = new EshopAddress();
      addressEntity.setAddressType(addressType);
      addressEntity.setCountryiso(StringUtils.EMPTY);
      addressEntity.setState(address.getStateDesc());
      addressEntity.setCity(address.getCity());
      addressEntity.setZipcode(address.getPostCode());

      final AddressBuilder addressBuilder = new AddressBuilder();
      addressBuilder.companyName(address.getCompanyName());
      addressBuilder.street(address.getStreet());
      addressBuilder.city(address.getCity());
      addressBuilder.country(address.getCountryDesc());

      addressEntity.setLine1(addressBuilder.build());
      return addressEntity;
    };
  }

  private Function<EshopAddress, OrganisationAddress> orgAddressConverter(Organisation customer) {
    return eshopAddr -> {
      final OrganisationAddress newOrgAddress = new OrganisationAddress();
      newOrgAddress.setOrganisation(customer);
      newOrgAddress.setAddress(eshopAddr);
      return newOrgAddress;
    };
  }

  @Override
  @Transactional
  public String findAffiliateByOrgId(final int orgId) {
    return orgRepo.findAffiliateByOrgId(orgId);
  }

  @Override
  public String findOrgCodeById(final int orgId) {
    return orgRepo.findOrgCodeById(orgId);
  }

  @Override
  public Map<String, String> findOrgSettingsByShortName(String shortName) {
    return orgCollectionService.findSettingsByCollectionShortname(shortName);
  }

  @Override
  public Optional<String> findOrgSettingByKey(String shortname, String key) {
    return orgCollectionService.findSettingValueByCollectionShortnameAndKey(shortname, key)
        .filter(StringUtils::isNotBlank);
  }

  @Override
  public Optional<Integer> findIdByShortName(String affiliateShortName) {
    return orgRepo.findIdByShortName(affiliateShortName);
  }

  @Override
  public boolean isWholesalerCustomer(Integer orgId) {
    Integer wholersalerPermission =
        eshopPermissionRepo.findPermissionIdByName(PermissionEnum.WHOLESALER.name())
            .orElseThrow(() -> new NoSuchElementException("Not found wholersaler permission"));
    return permissionService.hasPermission(orgId, wholersalerPermission);
  }

  @Override
  public boolean isDvseCustomer(Integer orgId) {
    Integer dvsePermission = eshopPermissionRepo.findPermissionIdByName(PermissionEnum.DVSE.name())
        .orElseThrow(() -> new NoSuchElementException("Not found dvse permission"));
    return permissionService.hasPermission(orgId, dvsePermission);
  }
}
