package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.CollectiveDeliveryRepository;
import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.repo.api.OrganisationPropertyRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.api.PriceDisplayTypeRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.CollectiveDelivery;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.DeliveryType;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationProperty;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.PriceDisplayType;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.DeliveryTypeAdditionalService;
import com.sagag.eshop.service.api.InvoiceTypeService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.PriceDisplaySettingService;
import com.sagag.eshop.service.api.PriceDisplayTypeService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.converter.OrganisationCollectionConverters;
import com.sagag.eshop.service.permission.configuration.BackOfficeCustomerPermissionConfiguration;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.offer.OrganisationPropertyType;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.bo.dto.PaymentSettingBODto;
import com.sagag.services.domain.eshop.dto.CustomerSettingsDto;
import com.sagag.services.domain.eshop.dto.OrgPropertyOfferDto;
import com.sagag.services.domain.eshop.dto.OrganisationPropertyDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.PriceDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ValidationException;

@Service
@Transactional
@Slf4j
public class CustomerSettingsServiceImpl implements CustomerSettingsService {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Autowired
  private OrganisationPropertyRepository organisationPropertyRepo;

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private UserService userService;

  @Autowired
  private PaymentMethodRepository paymentMethodRepo;

  @Autowired
  private DeliveryTypesRepository deliveryTypesRepo;

  @Autowired
  private InvoiceTypeService invoiceTypeService;

  @Autowired
  private BackOfficeCustomerPermissionConfiguration customerPermissionConfiguration;

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private PriceDisplaySettingService priceDisplaySettingService;

  @Autowired
  private AddressFilterService addressFilterService;

  @Autowired
  private PriceDisplayTypeRepository priceDisplayTypeRepo;

  @Autowired
  private CollectiveDeliveryRepository collectiveRepo;

  @Autowired
  private CountryConfiguration countryConfig;

  @Autowired
  private PriceDisplayTypeService priceDisplayTypeService;

  @Autowired
  private DeliveryTypeAdditionalService deliveryTypeAdditionalHandler;

  @Override
  public void saveCustomerSetting(CustomerSettings customerSettings) {
    if (Objects.isNull(customerSettings)) {
      return;
    }
    customerSettingsRepo.save(customerSettings);
  }

  @Override
  public CustomerSettingsDto updateCustomerSetting(final Long userId,
      CustomerSettingsDto customerSettingsDto) {
    log.debug("Update customer setting by user id={}", userId);
    final Integer orgId =
        userService.getOrgIdByUserId(userId).orElseThrow(() -> new ValidationException(
            String.format("Organization of user id=%d is not found", userId)));
    final CustomerSettings cusSettings = customerSettingsRepo.findSettingsByOrgId(orgId);
    cusSettings.setNetPriceView(customerSettingsDto.isNetPriceView());
    cusSettings.setNetPriceConfirm(customerSettingsDto.isNetPriceConfirm());
    cusSettings.setViewBilling(customerSettingsDto.isViewBilling());
    if (!CollectionUtils.isEmpty(customerSettingsDto.getPriceDisplaySettings())) {
      final int priceDisplaySettingId = customerSettingsDto.getPriceDisplaySettings()
          .stream()
          .filter(PriceDisplaySettingDto::isEnable)
          .map(PriceDisplaySettingDto::getId)
          .findAny()
          .orElseThrow(() -> new ValidationException("Invalid price display setting"));
      cusSettings.setPriceDisplayId(priceDisplaySettingId);
    }

    // Save default customer settings
    final CustomerSettings updatedCustomerSettings = customerSettingsRepo.save(cusSettings);
    final CustomerSettingsDto updatedCustomerSettingsDto = new CustomerSettingsDto();
    SagBeanUtils.copyProperties(updatedCustomerSettings, updatedCustomerSettingsDto);

    updatedCustomerSettingsDto.setOrgPropertyOffer(new OrgPropertyOfferDto(
        updateOrgPropertyOffer(orgId, customerSettingsDto.getOrgPropertyOffer())));
    updateUsersViewBilling(orgId, customerSettingsDto.isViewBilling());

    return updatedCustomerSettingsDto;
  }

  private void updateUsersViewBilling(int orgId, boolean viewBilling) {
    userSettingsRepo.findActiveUserSettingsByOrgId(orgId).ifPresent(settings -> {
      settings.forEach(item -> item.setViewBilling(viewBilling));
      userSettingsRepo.saveAll(settings);
    });
  }

  private List<OrganisationPropertyDto> updateOrgPropertyOffer(final int organisationId,
      final OrgPropertyOfferDto orgPropertyOffer) {

    final Long orgId = Long.valueOf(organisationId);
    final List<OrganisationProperty> orgProperties =
        organisationPropertyRepo.findByOrganisationId(orgId);

    final Map<String, OrganisationProperty> properties = new HashMap<>();
    if (!orgProperties.isEmpty()) {
      properties.putAll(orgProperties.stream()
          .collect(Collectors.toMap(OrganisationProperty::getType, Function.identity())));
    }

    // Update organisation properties
    Stream.of(OrganisationPropertyType.values()).forEach(type -> {
      switch (type) {
        case OFFER_FOOTER_TEXT:
          updateOrganisationProperty(orgId, properties,
              OrganisationPropertyType.OFFER_FOOTER_TEXT.getValue(),
              orgPropertyOffer.getFooterText());
          break;
        case OFFER_FORMAT_ALIGNMENT:
          updateOrganisationProperty(orgId, properties,
              OrganisationPropertyType.OFFER_FORMAT_ALIGNMENT.getValue(),
              orgPropertyOffer.getFormatAlign().toLowerCase());
          break;
        case OFFER_GENERAL_CROSSING:
          updateOrganisationProperty(orgId, properties,
              OrganisationPropertyType.OFFER_GENERAL_CROSSING.getValue(),
              orgPropertyOffer.getGeneralCrossing());
          break;
        case OFFER_PRINT_VENDOR_ADDR:
          updateOrganisationProperty(orgId, properties,
              OrganisationPropertyType.OFFER_PRINT_VENDOR_ADDR.getValue(),
              orgPropertyOffer.getPrintVendorAddr().toLowerCase());
          break;
        default:
          // Do nothing for other cases
          break;
      }
    });

    final List<OrganisationProperty> orgPropertiesEntity =
        organisationPropertyRepo.saveAll(properties.values());
    return orgPropertiesEntity.stream()
        .map(orgProp -> SagBeanUtils.map(orgProp, OrganisationPropertyDto.class))
        .collect(Collectors.toList());
  }

  private void updateOrganisationProperty(final Long orgId,
      final Map<String, OrganisationProperty> properties, final String type, final String value) {

    final String valueProp = StringUtils.isBlank(value) ? StringUtils.EMPTY : value;
    final OrganisationProperty property = properties.get(type);

    // Update to existing property
    if (Objects.nonNull(property)) {
      property.setValue(valueProp);
      properties.replace(type, property, property);
      return;
    }
    // Add new property
    properties.put(type, createOrganisationProperty(orgId, type, valueProp));
  }

  private OrganisationProperty createOrganisationProperty(final Long orgId, final String type,
      final String value) {
    // @formatter:off
    return OrganisationProperty.builder()
        .organisationId(orgId)
        .type(type)
        .value(value)
        .build();
    // @formatter:on
  }

  @Override
  public CustomerSettingsDto getCustomerSetting(Long userId) {
    log.debug("Get customer setting by user id={}", userId);
    int orgId = userService.getOrgIdByUserId(userId).orElseThrow(() -> new ValidationException(
        String.format("Organization of user id=%d is not found", userId)));
    final CustomerSettings customerSettings = customerSettingsRepo.findSettingsByOrgId(orgId);
    final CustomerSettingsDto customerSettingsDto = SagBeanUtils.map(customerSettings, CustomerSettingsDto.class);

    final List<OrganisationProperty> orgProperties =
        organisationPropertyRepo.findByOrganisationId(Long.valueOf(orgId));

    // Get organisation properties
    final List<OrganisationPropertyDto> orgPropDto = orgProperties.stream()
        .map(orgProp -> SagBeanUtils.map(orgProp, OrganisationPropertyDto.class))
        .collect(Collectors.toList());
    customerSettingsDto.setOrgPropertyOffer(new OrgPropertyOfferDto(orgPropDto));
    customerSettingsDto.setPriceDisplaySettings(priceDisplaySettingService.getPriceDisplaySetting(customerSettings));

    return customerSettingsDto;
  }

  @Override
  public CustomerSettings findSettingsByOrgId(int orgId) {
    return customerSettingsRepo.findSettingsByOrgId(orgId);
  }

  @Override
  public PaymentSettingBODto getCustomerSettingByOrgCodeByAdmin(String orgCode) {
    log.debug("Get customer setting by orgCode={}", orgCode);
    final Organisation org = orgRepo.findOneByOrgCode(orgCode).orElseThrow(
        () -> new ValidationException(String.format("Organisation=%s is not found", orgCode)));

    final CustomerSettings customerSettings = findSettingsByOrgId(org.getId());

    CustomerSettingsBODto customerSettingsDto =
        SagBeanUtils.map(customerSettings, CustomerSettingsBODto.class);
    customerSettingsDto.setPaymentId(customerSettings.getPaymentMethod().getId());
    customerSettingsDto.setOrgId(org.getId());
    setWssDeliveryId(customerSettingsDto);
    // handle permission
    customerSettingsDto.setPerms(customerPermissionConfiguration.getPermissions(customerSettingsDto));

    List<OrganisationCollectionDto> collections = orgCollectionService.findByAffiliateId(org.getParentId())
        .stream()
        .map(OrganisationCollectionConverters.toCustomerSettingBO())
        .collect(Collectors.toList());
    String collectionShortName = orgCollectionService.getCollectionByOrgId(org.getId())
        .map(OrganisationCollection::getShortname)
        .orElseThrow(() -> new ValidationException(String.format("Collection for org=%s is not found", orgCode)));
    customerSettingsDto.setCollections(collections);
    customerSettingsDto.setCollectionShortName(collectionShortName);

    PaymentSettingDto paymentSettingDto = userService.getPaymentSetting(false, true);
    deliveryTypeAdditionalHandler.additionalHandleForDeliveryType(orgCode, paymentSettingDto);
    PaymentSettingBODto paymentSettingBODto =
        SagBeanUtils.map(paymentSettingDto, PaymentSettingBODto.class);
    paymentSettingBODto.setCustomerSettingsBODto(customerSettingsDto);
    return paymentSettingBODto;
  }

  private void setWssDeliveryId(CustomerSettingsBODto customerSettingsDto) {
    if (customerSettingsDto.getWssDeliveryId() == null) {
      Integer pickupDeliveryTypeId = deliveryTypesRepo
          .findOneByDescCode(ErpSendMethodEnum.PICKUP.name()).map(DeliveryType::getId).orElse(null);
      customerSettingsDto.setWssDeliveryId(pickupDeliveryTypeId);
    }
  }

  @Override
  public void updateCustomerSettingByOrgCode(CustomerSettingsBODto customerSettingsBODto) {
    CustomerSettings customerSettings =
        customerSettingsRepo.findOneById(customerSettingsBODto.getId())
            .orElseThrow(() -> new ValidationException(String.format(
                "Organization of organisation=%s is not found", customerSettingsBODto.getId())));
    if (customerSettingsBODto.getWssDeliveryId() == null) {
      customerSettingsBODto.setWssDeliveryId(customerSettingsBODto.getDeliveryId());
    }
    SagBeanUtils.copyProperties(customerSettingsBODto, customerSettings);
    PaymentMethod paymentMethod =
        paymentMethodRepo.findOneById(customerSettingsBODto.getPaymentId()).orElseThrow(
            () -> new ValidationException(String.format("Payment Method code =%s is not found",
                customerSettingsBODto.getPaymentId())));

    customerPermissionConfiguration.updatePermisions(customerSettingsBODto);

    customerSettings.setPaymentMethod(paymentMethod);
    updateNetPriceConfirmIfNecessary(customerSettings);
    customerSettingsRepo.save(customerSettings);
  }

  private void updateNetPriceConfirmIfNecessary(CustomerSettings customerSettings) {
    customerSettings.setNetPriceConfirm(Boolean.logicalAnd(customerSettings.isNetPriceView(), customerSettings.isNetPriceConfirm()));
  }

  @Override
  public CustomerSettings createCustomerSettings(Customer customer, List<Address> addresses) {
    Assert.notNull(customer, "The given customer must not be null");
    Assert.notEmpty(addresses, "The given addresses must not be empty");
    final PriceDisplayTypeEnum defaultPriceDisplayType = priceDisplayTypeService.getDefaultPriceDisplayType();
    PriceDisplayType priceDisplayType = priceDisplayTypeRepo.findByType(defaultPriceDisplayType.name())
        .orElseThrow(() -> new ValidationException("Invalid price display setting"));
    final String sendMethodCode = customer.getSendMethodCode();
    int deliveryId = 1;
    final Optional<DeliveryType> deliveryType = deliveryTypesRepo.findOneByDescCode(sendMethodCode);
    if (deliveryType.isPresent()) {
      deliveryId = deliveryType.get().getId();
    }
    int wssDeliveryId = 1;
    final Optional<DeliveryType> wssDeliveryType =
        deliveryTypesRepo.findOneByDescCode(ErpSendMethodEnum.PICKUP.name());
    if (wssDeliveryType.isPresent()) {
      wssDeliveryId = wssDeliveryType.get().getId();
    }
    final CollectiveDelivery collectiveDelivery =
        collectiveRepo.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().findFirst()
            .orElseThrow(() -> new ValidationException("Could not found collective delivery"));
    final CustomerSettings customerSettings = new CustomerSettings();
    customerSettings.setDeliveryId(deliveryId);
    customerSettings.setWssDeliveryId(wssDeliveryId);
    customerSettings.setAllocationId(1);
    customerSettings.setCollectiveDelivery(collectiveDelivery.getId());

    final Optional<InvoiceType> optInvoiceType =
        invoiceTypeService.getInvoiceTypeByCode(customer.getInvoiceTypeCode());
    optInvoiceType.ifPresent(customerSettings::setInvoiceType);

    customerSettings.setSessionTimeoutSeconds(countryConfig.getCustomerSessionTimeoutSeconds());
    customerSettings.setNetPriceView(countryConfig.getCustomerNetPriceViewSetting());
    customerSettings.setNetPriceConfirm(countryConfig.getCustomerNetPriceConfirmSetting());
    customerSettings.setAllowNetPriceChanged(true);
    final Optional<PaymentMethod> paymentMethod =
        paymentMethodRepo.findOneByDescCode(customer.getCashOrCreditTypeCode());
    paymentMethod.ifPresent(customerSettings::setPaymentMethod);

    addressFilterService.filterAddress(addresses, StringUtils.EMPTY, ErpAddressType.INVOICE)
        .map(Address::getId)
        .map(Long::valueOf)
        .ifPresent(customerSettings::setBillingAddressId);
    addressFilterService.filterAddress(addresses, StringUtils.EMPTY, ErpAddressType.DELIVERY)
        .map(Address::getId)
        .map(Long::valueOf)
        .ifPresent(customerSettings::setDeliveryAddressId);
		customerSettings.setPriceDisplayId(priceDisplayType.getId());
    return customerSettings;

  }
}
