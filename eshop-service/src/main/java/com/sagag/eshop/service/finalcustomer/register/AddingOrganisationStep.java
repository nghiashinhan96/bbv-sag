package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.OrganisationTypeRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationType;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.register.result.AddingOrganisationStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.enums.OrganisationTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddingOrganisationStep extends AbstractNewFinalCustomerStep {

  @Autowired
  private OrganisationTypeRepository organisationTypeRepo;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Override
  public String getStepName() {
    return "Adding organisation step";
  }

  @Override
  public NewFinalCustomerStepResult processItem(NewFinalCustomerDto newFinalCustomer,
      NewFinalCustomerStepResult stepResult) {

    OrganisationType orgType =
        organisationTypeRepo.findOneByName(OrganisationTypeEnum.FINAL_CUSTOMER.name())
            .orElseThrow(() -> new IllegalArgumentException("Not found final customer type"));

    Integer parentId = newFinalCustomer.getCustomerOrgId();
    String customerName = newFinalCustomer.getCustomerName();
    String shortName = "customer - " + newFinalCustomer.getCustomerNr();

    CustomerSettings customerSettings =
        stepResult.getAddingCustomerSettingStepResult().getCustomerSettings();

    Organisation newOrganisation = Organisation.builder().name(customerName)
        .organisationType(orgType).parentId(parentId).description(customerName).shortname(shortName)
        .customerSettings(customerSettings).build();

    Organisation createdOrganisation = organisationRepo.save(newOrganisation);
    stepResult
        .setAddingOrganisationStepResult(new AddingOrganisationStepResult(createdOrganisation));
    return stepResult;
  }
}
