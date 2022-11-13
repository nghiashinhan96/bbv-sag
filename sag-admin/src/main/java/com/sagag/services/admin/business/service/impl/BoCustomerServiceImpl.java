package com.sagag.services.admin.business.service.impl;

import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.admin.business.service.BoCustomerService;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.bo.dto.PaymentSettingBODto;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.api.DvseBusinessService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BoCustomerServiceImpl implements BoCustomerService {

  private static final int DVSE_USER_POOL_QUANTITY = 20;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private DvseBusinessService dvseBusinessService;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private ExternalOrganisationRepository extOrgRepo;
  
  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Override
  @Transactional
  public PaymentSettingBODto getCustomerSettingByOrgCodeByAdmin(String orgCode) {
    return customerSettingsService.getCustomerSettingByOrgCodeByAdmin(orgCode);
  }

  @Override
  @Transactional
  public void updateCustomerSettingByOrgCode(CustomerSettingsBODto customerSettingsBODto)
      throws UserValidationException, MdmCustomerNotFoundException {
    final int orgId = customerSettingsBODto.getOrgId();
    final OrganisationCollection collection = orgCollectionService
        .getCollectionByShortName(customerSettingsBODto.getCollectionShortName())
        .orElseThrow(() -> new IllegalArgumentException("Collection short name not correct"));
    final OrganisationCollection existedCollection =
        orgCollectionService.getCollectionByOrgId(orgId)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find collection of " + orgId));
    if (!collection.getShortname().equals(existedCollection.getShortname())) {
      orgCollectionService.addCustomerToCollection(orgId, collection.getId());
    }
    customerSettingsService.updateCustomerSettingByOrgCode(customerSettingsBODto);

    int virtualPoolSize = getVirtualPoolSize(customerSettingsBODto.getPerms());
    if (virtualPoolSize == 0) {
      return;
    }

    SupportedAffiliate affiliate =
        SupportedAffiliate.fromDesc(organisationService.findAffiliateByOrgId(orgId));
    if (!organisationService.isDvseCustomer(orgId)) {
      return;
    }
    final long start = System.currentTimeMillis();
    String extCustId =
        extOrgRepo.findExternalCustomerIdByOrgIdAndExternalApp(orgId, ExternalApp.DVSE);
    if (StringUtils.isBlank(extCustId)) {
      extCustId = dvseBusinessService.createDvseCustomerInfo(orgId, affiliate);
    }
    List<VUserDetail> usersWithoutDVSEUser =
        vUserDetailRepo.findUsersByOrgIdNotHaveExternalUser(orgId, ExternalApp.DVSE);
    
    if (CollectionUtils.isNotEmpty(usersWithoutDVSEUser)) {
      for (VUserDetail vUserDetail : usersWithoutDVSEUser) {
        dvseBusinessService.createDvseUserInfo(vUserDetail.getUserId(), affiliate, orgId);
      }
    }
    
    dvseBusinessService.createVirtualDvseUserPool(orgId, extCustId,
        affiliate, virtualPoolSize);
    log.debug("BackOfficeServiceImpl-> updateCustomerSettingByOrgCode-> create oci pool {} ms",
        System.currentTimeMillis() - start);
  }

  private int getVirtualPoolSize(List<PermissionConfigurationDto> perms) {
    int poolSize = 0;
    if (CollectionUtils.isEmpty(perms)) {
      return poolSize;
    }

    if (perms.stream()
        .anyMatch(p -> PermissionEnum.OCI.name().equals(p.getPermission()) && p.isEnable())) {
      poolSize += DVSE_USER_POOL_QUANTITY;
    }

    if (perms.stream()
        .anyMatch(p -> PermissionEnum.DMS.name().equals(p.getPermission()) && p.isEnable())) {
      poolSize += DVSE_USER_POOL_QUANTITY;
    }

    return poolSize;
  }

}
