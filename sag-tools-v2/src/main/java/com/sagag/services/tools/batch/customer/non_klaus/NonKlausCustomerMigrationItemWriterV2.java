package com.sagag.services.tools.batch.customer.non_klaus;

import com.sagag.services.tools.batch.sales.on_behalf_user.RegisterOnbehalfProcessor;
import com.sagag.services.tools.domain.customer.ImportedCustomerData;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.domain.target.OrganisationCollection;
import com.sagag.services.tools.repository.target.CollectionRelationRepository;
import com.sagag.services.tools.repository.target.OrganisationCollectionRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.service.CustomerSettingsService;
import com.sagag.services.tools.service.OrganisationService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.transaction.Transactional;

@Component
@StepScope
@Slf4j
@Transactional
public class NonKlausCustomerMigrationItemWriterV2 implements ItemWriter<ImportedCustomerData>, INonKlausCustomerMapper {

  @Autowired
  private CustomerSettingsService custSettingsService;

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private OrganisationCollectionRepository orgCollectionRepo;

  @Autowired
  private CollectionRelationRepository collectionRelationRepo;

  @Autowired
  private RegisterOnbehalfProcessor registerOnbehalfUserProcessor;

  @Autowired
  private OrganisationRepository orgRepo;

  @Override
  public void write(List<? extends ImportedCustomerData> items) throws Exception {
    if (CollectionUtils.isEmpty(items)) {
      return;
    }

    Organisation customer;
    for (ImportedCustomerData item : items) {
      if (item.isExisted()) {
        registerOnbehalfUserProcessor.process(
            orgRepo.findOneByOrgCode(item.getCustomerNr()).orElse(new Organisation()));
        continue;
      }
      OrganisationCollection orgCollection = orgCollectionRepo.findByShortname(item.getAffiliate().getAffiliate())
          .orElseThrow(() -> new IllegalArgumentException("Not found affiliate"));
      log.debug("{}", item);
      final CustomerSettings custSettings = custSettingsService.createCustomerSettings(
          item.getCustomer(), item.getCustomerAddresses());
     customer = orgService.createCustomer(item.getAffiliate(),
          item.getCustomerNr(), item.getCustomer().getCompanyName(), custSettings);

      orgService.createCustomerAddresses(customer, item.getCustomerAddresses());

      orgService.assignCustomerGroupAndDefaultPermission(customer, item.getCollecionPermissions());

      collectionRelationRepo.save(buildOrgCollectionRelation(orgCollection.getId(),
          customer.getId()));

      registerOnbehalfUserProcessor.process(customer);
    }
  }

}
