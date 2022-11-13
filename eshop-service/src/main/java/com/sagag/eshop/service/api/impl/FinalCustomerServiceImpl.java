package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.api.VFinalCustomerRepository;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchCriteria;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.eshop.repo.specification.VFinalCustomerSearchSpecification;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.DeliveryTypesService;
import com.sagag.eshop.service.api.FinalCustomerService;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerSettingDto;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.TemplateNewFinalCustomerProfileDto;
import com.sagag.eshop.service.dto.finalcustomer.TemplateNewFinalCustomerProfileDto.TemplateNewFinalCustomerProfileDtoBuilder;
import com.sagag.eshop.service.dto.finalcustomer.UpdatingFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.DeletingFinalCustomerProcessor;
import com.sagag.eshop.service.finalcustomer.SelectedFinalCustomerProcessor;
import com.sagag.eshop.service.finalcustomer.UpdatingFinalCustomerProcessor;
import com.sagag.eshop.service.finalcustomer.register.NewFinalCustomerProcess;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.eshop.service.validator.finalcustomer.DeleteFinalCustomerValidator;
import com.sagag.eshop.service.validator.finalcustomer.NewFinalCustomerValidator;
import com.sagag.eshop.service.validator.finalcustomer.UpdatingFinalCustomerValidator;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.FinalCustomerType;
import com.sagag.services.common.enums.SalutationEnum;
import com.sagag.services.common.enums.WssMarginGroupEnum;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FinalCustomerServiceImpl implements FinalCustomerService {

  private static final String[] SALUTATIONS_FOR_FINAL_CUSTOMER =
      { SalutationEnum.GENERAL_SALUTATION_COMPANY.name(),
          SalutationEnum.GENERAL_SALUTATION_FEMALE.name(),
          SalutationEnum.GENERAL_SALUTATION_MALE.name() };

  @Autowired
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Autowired
  private VFinalCustomerRepository vFinalCustomerRepo;

  @Autowired
  private SalutationService salutationService;

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private NewFinalCustomerProcess newFinalCustomerProcess;

  @Autowired
  private NewFinalCustomerValidator newFinalCustomerValidator;

  @Autowired
  private UpdatingFinalCustomerProcessor updatingFinalCustomerProcessor;

  @Autowired
  private UpdatingFinalCustomerValidator updatingFinalCustomerValidator;

  @Autowired
  private DeleteFinalCustomerValidator deleteFinalCustomerValidator;

  @Autowired
  private SelectedFinalCustomerProcessor selectedFinalCustomerProcessor;

  @Autowired
  private DeletingFinalCustomerProcessor deletingFinalCustomerProcessor;

  @Autowired
  private DeliveryTypesService deliveryTypesService;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Override
  @LogExecutionTime
  public FinalCustomerSettingDto getFinalCustomerSettings(final Long orgId) {
    Assert.notNull(orgId, "orgId is required!");
    final List<FinalCustomerProperty> properties = finalCustomerPropertyRepo.findByOrgId(orgId);
    Map<String, String> settings = properties.stream().collect(
        Collectors.toMap(FinalCustomerProperty::getSettingKey, FinalCustomerProperty::getValue));
    return new FinalCustomerSettingDto(settings);
  }

  @Override
  @LogExecutionTime
  public Page<FinalCustomerDto> searchFinalCustomersBelongToCustomer(Integer customerOrgId,
      FinalCustomerSearchCriteria criteria, Pageable pageable) {
    if (customerOrgId == null || criteria == null || criteria.getTerm() == null
        || criteria.getSort() == null || pageable == null) {
      return Page.empty();
    }
    final Specification<VFinalCustomer> spec =
        VFinalCustomerSearchSpecification.getInstance(customerOrgId, criteria);
    return vFinalCustomerRepo.findAll(spec, pageable).map(FinalCustomerDto::new);
  }

  @Override
  @LogExecutionTime
  public Optional<FinalCustomerDto> getFinalCustomerInfo(Integer finalCustomerOrgId,
      boolean fullMode) {
    if (finalCustomerOrgId == null) {
      return Optional.empty();
    }
    final Optional<FinalCustomerDto> finalCustomerOpt =
        vFinalCustomerRepo.findByOrgId(finalCustomerOrgId).map(FinalCustomerDto::new);
    if (!fullMode) {
      return finalCustomerOpt;
    }
    finalCustomerOpt.ifPresent(customer -> customer
        .setCustomerProperties(getFinalCustomerSettings(Long.valueOf(finalCustomerOrgId))));
    return finalCustomerOpt;
  }

  @Override
  @LogExecutionTime
  public TemplateNewFinalCustomerProfileDto getTemplateNewFinalCustomerProfile(
      Integer wholesalerOrgId, Long userId, boolean showNetPriceEnabled) {
    final TemplateNewFinalCustomerProfileDtoBuilder profileBuilder =
        TemplateNewFinalCustomerProfileDto.builder();

    profileBuilder.customerTypes(Stream.of(FinalCustomerType.values()).map(FinalCustomerType::name)
        .collect(Collectors.toList()));
    profileBuilder
        .salutations(salutationService.getSalutationByCodes(SALUTATIONS_FOR_FINAL_CUSTOMER));

    profileBuilder.showNetPrice(false); // #4857
    List<DeliveryTypeDto> deliveryTypes = deliveryTypesService.findAllDeliveryTypes();
    profileBuilder.deliveryTypes(deliveryTypes);

    profileBuilder.defaultDeliveryType(getDefaultDeliveryTypeId(wholesalerOrgId, deliveryTypes));

    if (userId == null) {
      return profileBuilder.build();
    }

    profileBuilder.permissions(permissionService.getFinalCustomerMaxPermissions(userId));
    profileBuilder.showNetPriceEnabled(showNetPriceEnabled);
    profileBuilder.marginGroups(WssMarginGroupEnum.getAll());
    return profileBuilder.build();
  }

  private Integer getDefaultDeliveryTypeId(Integer wholesalerOrgId,
      List<DeliveryTypeDto> deliveryTypes) {
    // #3963 FC default delivery type is PICKUP
    Integer defaultDeliveryTypeId = deliveryTypes.stream()
        .filter(deliveryType -> StringUtils.equals(deliveryType.getDescCode(), ErpSendMethodEnum.PICKUP.name()))
        .findFirst().map(DeliveryTypeDto::getId).orElse(null);

    CustomerSettings wssSettings = customerSettingsService.findSettingsByOrgId(wholesalerOrgId);
    if (wssSettings.getWssDeliveryId() != null) {
      defaultDeliveryTypeId = wssSettings.getWssDeliveryId();
    }
    return defaultDeliveryTypeId;
  }

  @Override
  @Transactional
  @LogExecutionTime
  public void createFinalCustomer(NewFinalCustomerDto finalCustomerDto) throws ServiceException {
    newFinalCustomerValidator.validate(finalCustomerDto);
    newFinalCustomerProcess.setUpSteps().processRequest(finalCustomerDto,
        new NewFinalCustomerStepResult());
  }

  @Override
  @Transactional
  @LogExecutionTime
  public void updateFinalCustomer(Integer finalCustomerId, SavingFinalCustomerDto finalCustomer)
      throws ServiceException {
    assertFinalCustomerId(finalCustomerId);
    updatingFinalCustomerValidator.validate(finalCustomer);
    updatingFinalCustomerProcessor.process(finalCustomerId, finalCustomer);
  }

  @Override
  @LogExecutionTime
  public UpdatingFinalCustomerDto getSelectedFinalCustomer(Integer finalCustomerOrgId,
      Long wholesalerUserId, boolean showNetPriceEnabled) {
    assertFinalCustomerId(finalCustomerOrgId);
    UpdatingFinalCustomerDto result = new UpdatingFinalCustomerDto();
    result.setCustomerTypes(Stream.of(FinalCustomerType.values()).map(FinalCustomerType::name)
        .collect(Collectors.toList()));
    result.setSalutations(salutationService.getSalutationByCodes(SALUTATIONS_FOR_FINAL_CUSTOMER));
    List<DeliveryTypeDto> deliveryTypes = deliveryTypesService.findAllDeliveryTypes();
    result.setDeliveryTypes(deliveryTypes);

    SavingFinalCustomerDto finalCustomer =
        selectedFinalCustomerProcessor.process(finalCustomerOrgId, wholesalerUserId);
    result.setSelectedFinalCustomer(finalCustomer);
    result.setShowNetPriceEnabled(showNetPriceEnabled);
    result.setMarginGroups(WssMarginGroupEnum.getAll());
    return result;
  }

  @Override
  @Transactional
  @LogExecutionTime
  public void deleteFinalCustomer(Integer finalCustomerId) throws ServiceException {
    assertFinalCustomerId(finalCustomerId);
    deleteFinalCustomerValidator.validate(finalCustomerId);
    deletingFinalCustomerProcessor.process(finalCustomerId);
  }

  private void assertFinalCustomerId(Integer finalCustomerId) {
    Assert.notNull(finalCustomerId, "Final customer org id must not be null");
  }
}
