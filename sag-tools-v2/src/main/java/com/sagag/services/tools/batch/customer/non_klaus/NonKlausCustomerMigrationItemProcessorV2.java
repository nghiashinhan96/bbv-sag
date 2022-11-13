package com.sagag.services.tools.batch.customer.non_klaus;

import com.sagag.services.tools.batch.sales.on_behalf_user.RegisterOnbehalfProcessor;
import com.sagag.services.tools.domain.ax.AxCustomerInfo;
import com.sagag.services.tools.domain.customer.CsvCustomerInfoData;
import com.sagag.services.tools.domain.customer.ImportedCustomerData;
import com.sagag.services.tools.domain.customer.PermissionConfigurationDto;
import com.sagag.services.tools.domain.target.CollectionPermission;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.exception.AxExternalException;
import com.sagag.services.tools.repository.target.CollectionPermissionRepository;
import com.sagag.services.tools.repository.target.EshopPermissionRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.service.AxService;
import com.sagag.services.tools.support.PermissionEnum;
import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Component
@StepScope
@Slf4j
@Transactional
public class NonKlausCustomerMigrationItemProcessorV2
    implements ItemProcessor<CsvCustomerInfoData, ImportedCustomerData>, INonKlausCustomerMapper {

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private EshopPermissionRepository eshopPermissionRepo;

  @Autowired
  private AxService axService;

  @Autowired
  private CollectionPermissionRepository collPermissionRepo;

  @Autowired
  private RegisterOnbehalfProcessor registerOnbehalfUserProcessor;

  @Override
  public ImportedCustomerData process(CsvCustomerInfoData item) throws Exception {
    log.debug("Customer number = {}", item.getCustomerNr());
    if (!StringUtils.isNumeric(item.getCustomerNr())) {
      return null;
    }
    ImportedCustomerData data = new ImportedCustomerData();
    data.setCustomerNr(item.getCustomerNr());
    data.setAffiliate(item.getAffiliate());

    final Optional<Organisation> orgOpt = orgRepo.findOneByOrgCode(data.getCustomerNr());
    if (orgOpt.isPresent()) {
      log.warn("This customer {} is existing in ConnectDB", data.getCustomerNr());
      registerOnbehalfUserProcessor.process(orgOpt.get());
      data.setExisted(true);
      return data;
    }

    Optional<AxCustomerInfo> axCustomerInfo = Optional.empty();
    try {
      axCustomerInfo = axService.getActiveCustomerInfo(
          item.getAffiliate().getCompanyName(), data.getCustomerNr());
    } catch (RestClientException | IllegalArgumentException | AxExternalException ex) {
      log.error("Returns actived customer info has error: ", ex);
    }

    if (!axCustomerInfo.isPresent()) {
      log.warn("Not found any customer information of customer {} at AX SWS", data.getCustomerNr());
      return null;
    }

    if (axCustomerInfo.filter(filterKlausCustomer()).isPresent()) {
      log.warn("This customer nr = {} is belong to Klaus affiliate", data.getCustomerNr());
      return null;
    }

    axCustomerInfo
    .ifPresent(customerInfo -> {
      data.setAffiliate(
          SupportedAffiliate.fromDesc(customerInfo.getCustomer().getAffiliateShortName()).get());
      data.setCustomer(customerInfo.getCustomer());
      data.setCustomerAddresses(customerInfo.getAddresses());
      data.setCollecionPermissions(buildCollectionPermissions(
          SupportedAffiliate.fromDesc(customerInfo.getCustomer().getAffiliateShortName()).get()));
    });

    if (data.getCustomer() == null) {
      log.warn("No customer info from AX SWS");;
      return null;
    }
    if (CollectionUtils.isEmpty(data.getCustomerAddresses())) {
      log.warn("No customer addresses info from AX SWS");
      return null;
    }

    return data;
  }

  private static Predicate<AxCustomerInfo> filterKlausCustomer() {
    return customer -> SupportedAffiliate.KLAUS.getAffiliate()
        .equals(customer.getCustomer().getAffiliateShortName());
  }

  private List<PermissionEnum> buildCollectionPermissions(SupportedAffiliate affiliate) {
    final List<CollectionPermission> maxPermissionForCollection =
        collPermissionRepo.findByCollectionShortName(affiliate.getAffiliate());
    return findPermissions(affiliate.getAffiliate(), maxPermissionForCollection, true)
        .stream()
        .map(PermissionConfigurationDto::getPermission)
        .map(PermissionEnum::valueOf)
        .collect(Collectors.toList());
  }

  private List<PermissionConfigurationDto> findPermissions(String affShortName,
      List<CollectionPermission> collectionPermissions, boolean isCustomerUser) {
    Assert.hasText(affShortName, "AffiliateShortName cannot be empty");
    return eshopPermissionRepo.findAll().stream()
        .map(toPermissionDto(affShortName, collectionPermissions, isCustomerUser))
        .collect(Collectors.toList());
  }

}
