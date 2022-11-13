package com.sagag.eshop.service.finalcustomer;

import com.sagag.eshop.repo.api.GroupPermissionRepository;
import com.sagag.eshop.repo.api.VFinalCustomerRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.GroupPermission;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.eshop.service.converter.WssDeliveryProfileConverters;
import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.services.common.enums.FinalCustomerStatus;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SelectedFinalCustomerProcessor extends AbstractFinalCustomerProcessor {

  @Autowired
  private VFinalCustomerRepository vFinalCustomerRepo;

  @Autowired
  private GroupPermissionRepository groupPermissionRepo;

  public SavingFinalCustomerDto process(Integer finalCustomerOrgId, Long wholesalerUserId) {
    VFinalCustomer vFinalCustomer = vFinalCustomerRepo.findByOrgId(finalCustomerOrgId)
        .orElseThrow(() -> new NoSuchElementException(
            "Not found final customer with id = " + finalCustomerOrgId));

    CustomerSettings settings = customerSettingsRepo.findSettingsByOrgId(finalCustomerOrgId);
    SavingFinalCustomerDto finalCustomerDto = new SavingFinalCustomerDto();
    finalCustomerDto.setCustomerName(vFinalCustomer.getName());
    finalCustomerDto.setCustomerType(vFinalCustomer.getFinalCustomerType());
    finalCustomerDto.setShowNetPrice(settings.isNetPriceView()); // #3166
    finalCustomerDto.setWssMarginGroup(settings.getWssMarginGroup());
    finalCustomerDto.setPerms(getPermissions(finalCustomerOrgId, wholesalerUserId));
    if (settings.getWssDeliveryProfile() != null) {
      finalCustomerDto.setWssDeliveryProfileDto(WssDeliveryProfileConverters
          .convertToDeliveryProfileDto(settings.getWssDeliveryProfile()));
    }
    finalCustomerDto.setDeliveryId(settings.getDeliveryId());
    final List<FinalCustomerProperty> properties =
        finalCustomerPropertyRepo.findByOrgId(finalCustomerOrgId.longValue());

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.TYPE)
        .ifPresent(p -> finalCustomerDto.setCustomerType(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.STATUS)
        .ifPresent(p -> finalCustomerDto.setIsActive(FinalCustomerStatus.ACTIVE.name().equals(p.getValue())));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.CUSTOMER_NUMBER)
        .ifPresent(p -> finalCustomerDto.setCustomerNr((p.getValue())));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.SALUTATION)
        .ifPresent(p -> finalCustomerDto.setSalutation(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.SURNAME)
        .ifPresent(p -> finalCustomerDto.setSurName(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.FIRSTNAME)
        .ifPresent(p -> finalCustomerDto.setFirstName(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.STREET)
        .ifPresent(p -> finalCustomerDto.setStreet(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.ADDRESS_1)
        .ifPresent(p -> finalCustomerDto.setAddressOne(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.ADDRESS_2)
        .ifPresent(p -> finalCustomerDto.setAddressTwo(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.PO_BOX)
        .ifPresent(p -> finalCustomerDto.setPoBox(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.POSTCODE)
        .ifPresent(p -> finalCustomerDto.setPostcode(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.PLACE)
        .ifPresent(p -> finalCustomerDto.setPlace(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.PHONE)
        .ifPresent(p -> finalCustomerDto.setPhone(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.FAX)
        .ifPresent(p -> finalCustomerDto.setFax(p.getValue()));

    filterProperty(properties, SettingsKeys.FinalCustomer.Settings.EMAIL)
        .ifPresent(p -> finalCustomerDto.setEmail(p.getValue()));

    return finalCustomerDto;
  }

  public List<PermissionConfigurationDto> getPermissions(Integer finalCustomerOrgId, Long userId) {
    EshopGroup adminGroup = eshopGroupRepo
        .findByOrgIdAndRoleName(finalCustomerOrgId,
            EshopUserCreateAuthority.FINAL_USER_ADMIN.name())
        .orElseThrow(() -> new NoSuchElementException(
            "Not found admin group for final customer with id = " + finalCustomerOrgId));
    List<GroupPermission> savedGroupPerms = groupPermissionRepo.findByGroupId(adminGroup.getId());

    List<PermissionConfigurationDto> maxPerms =
        permissionService.getFinalCustomerMaxPermissions(userId);

    if (CollectionUtils.isEmpty(maxPerms)) {
      return Collections.emptyList();
    }

    if (CollectionUtils.isEmpty(savedGroupPerms)) {
      return maxPerms;
    }

    return maxPerms.stream().map(p -> {
      p.setEnable(isEnablePerm(savedGroupPerms, p.getPermissionId()));
      return p;
    }).collect(Collectors.toList());
  }

  private boolean isEnablePerm(List<GroupPermission> perms, int permId) {
    return perms.stream().anyMatch(gp -> Optional.ofNullable(gp.getEshopPermission())
        .map(EshopPermission::getId).orElse(null) == permId && gp.isAllowed());
  }
}
