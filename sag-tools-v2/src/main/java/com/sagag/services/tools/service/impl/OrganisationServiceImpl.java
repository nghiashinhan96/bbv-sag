package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.target.AddressType;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.EshopAddress;
import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.EshopRole;
import com.sagag.services.tools.domain.target.GroupRole;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.domain.target.OrganisationAddress;
import com.sagag.services.tools.domain.target.OrganisationGroup;
import com.sagag.services.tools.domain.target.SagAddressBuilder;
import com.sagag.services.tools.repository.target.AddressTypeRepository;
import com.sagag.services.tools.repository.target.EshopAddressRepository;
import com.sagag.services.tools.repository.target.EshopGroupRepository;
import com.sagag.services.tools.repository.target.EshopRoleRepository;
import com.sagag.services.tools.repository.target.GroupRoleRepository;
import com.sagag.services.tools.repository.target.OrganisationAddressRepository;
import com.sagag.services.tools.repository.target.OrganisationGroupRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.repository.target.OrganisationTypeRepository;
import com.sagag.services.tools.service.OrganisationService;
import com.sagag.services.tools.service.PermissionService;
import com.sagag.services.tools.support.EshopUserCreateAuthority;
import com.sagag.services.tools.support.PermissionEnum;
import com.sagag.services.tools.support.SupportedAffiliate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

@Service
@Transactional
@OracleProfile
public class OrganisationServiceImpl implements OrganisationService {

  @Autowired
  private OrganisationRepository organisationRepository;

  @Autowired
  private OrganisationTypeRepository orgTypeRepo;

  @Autowired
  private EshopAddressRepository addressRepo;

  @Autowired
  private OrganisationAddressRepository orgAddressRepo;

  @Autowired
  private AddressTypeRepository addressTypeRepo;

  @Autowired
  private OrganisationGroupRepository orgGroupRepo;

  @Autowired
  private GroupRoleRepository groupRoleRepo;

  @Autowired
  private EshopRoleRepository roleRepo;

  @Autowired
  private EshopGroupRepository groupRepo;

  @Autowired
  private PermissionService permissionService;

  /* Common error message */
  private static final String CUSTOMER_ORG_NOT_NULL = "The given customer must not be null";

  private static final PermissionEnum[] DF_PERMISSIONS =
    { PermissionEnum.BATTERY, PermissionEnum.BULB, PermissionEnum.OIL };

  @Override
  public boolean isBelongsToDatMatikAtMatikChAffiliate(final Integer orgId) {
    final Optional<Organisation> org = organisationRepository.findOneById(orgId);
    if (!org.isPresent()) {
      return false;
    }
    final Optional<Organisation> parentOrg = organisationRepository.findOneById(org.get().getParentId());

    if (!parentOrg.isPresent()) {
      return false;
    }
    final Optional<SupportedAffiliate> affiliate = SupportedAffiliate.fromDesc(parentOrg.get().getShortname());
    if (!affiliate.isPresent()) {
      return false;
    }
    final SupportedAffiliate supportedAffiliate = affiliate.get();
    return supportedAffiliate.isDat() || supportedAffiliate.isMatikAt() || supportedAffiliate.isMatikCh();
  }

  @Override
  public Optional<Organisation> findOrganisationById(int id) {
    return organisationRepository.findOneById(id);
  }


  @Override
  public Organisation createCustomer(SupportedAffiliate affiliate, String customerNr, String companyName, CustomerSettings custSettings) {
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.hasText(customerNr, "The given custNr must not be empty");
    Assert.notNull(custSettings, "The given cust settings must not be null");

    final String shortname = "customer-" + customerNr;

    final Integer affiliateId = organisationRepository.findIdByShortName(affiliate.getAffiliate()).orElseThrow(() -> new IllegalArgumentException(""));

    final Organisation customer = new Organisation();
    customer.setName(companyName);
    customer.setOrgCode(customerNr);
    customer.setOrgTypeId(orgTypeRepo.getCustomerOrgType().getId());
    customer.setParentId(affiliateId);
    customer.setDescription(companyName);
    customer.setShortname(shortname);
    customer.setCustomerSettings(custSettings);
    return organisationRepository.save(customer);
  }

  @Override
  public List<OrganisationAddress> createCustomerAddresses(Organisation customer, List<Address> addresses) {
    Assert.notNull(customer, CUSTOMER_ORG_NOT_NULL);
    Assert.notEmpty(addresses, "The given addresses must not be empty");

    final List<EshopAddress> eshopAddresses =
        addresses.stream().filter(add -> add.getAddressTypeCode() == "DEFAULT").map(eshopAddressConverter()).collect(Collectors.toList());
    addressRepo.saveAll(eshopAddresses);

    final List<OrganisationAddress> orgAddresses = eshopAddresses.stream().map(orgAddressConverter(customer)).collect(Collectors.toList());
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

      final SagAddressBuilder addressBuilder = new SagAddressBuilder();
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
  public void assignCustomerGroupAndDefaultPermission(Organisation customer) {
    Assert.notNull(customer, CUSTOMER_ORG_NOT_NULL);
    assignCustomerGroupAndDefaultPermission(customer, Arrays.asList(DF_PERMISSIONS));
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
  public void assignCustomerGroupAndDefaultPermission(Organisation customer,
      List<PermissionEnum> defaultPermission) {

    final Map<EshopUserCreateAuthority, EshopGroup> eshopGroups =
        Stream.of(EshopUserCreateAuthority.values()).collect(Collectors.toMap(
            authority -> authority, authority -> EshopGroup.buildGroupBy(customer, authority)));
    final List<EshopGroup> savedUserGroups = groupRepo.saveAll(eshopGroups.values());
    permissionService.setPermissions(savedUserGroups, defaultPermission, true);

    final List<String> roles = Arrays.asList(EshopUserCreateAuthority.values()).stream()
        .map(EshopUserCreateAuthority::name).collect(Collectors.toList());
    final List<EshopRole> eshopRoles = roleRepo.findEshopRolesByName(roles);
    if (CollectionUtils.isEmpty(eshopRoles)) {
      return;
    }

    final List<GroupRole> eshopGroupRoles =
        eshopRoles.stream().map(groupRoleConverter(eshopGroups)).collect(Collectors.toList());
    groupRoleRepo.saveAll(eshopGroupRoles);

    final List<OrganisationGroup> orgGroups = Stream.of(EshopUserCreateAuthority.values())
        .map(organisationGroupConverter(eshopGroups, customer)).collect(Collectors.toList());
    orgGroupRepo.saveAll(orgGroups);
  }

}
