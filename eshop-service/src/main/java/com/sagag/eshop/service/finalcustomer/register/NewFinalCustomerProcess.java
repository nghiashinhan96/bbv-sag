package com.sagag.eshop.service.finalcustomer.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewFinalCustomerProcess {

  @Autowired
  private AddingCustomerSettingStep addingCustomerSettingStep;

  @Autowired
  private AddingOrganisationStep addingOrganisationStep;

  @Autowired
  private AddingFinalCustomerPropertyStep addingFinalCustomerPropertyStep;

  @Autowired
  private AssigningToCollectionStep assignToCollectionStep;

  @Autowired
  private AddingEshopGroupStep addingEshopGroupStep;

  @Autowired
  private SettingPermissionStep settingPermissionStep;

  public AbstractNewFinalCustomerStep setUpSteps() {
    addingCustomerSettingStep.nextStep(addingOrganisationStep)
        .nextStep(addingFinalCustomerPropertyStep).nextStep(assignToCollectionStep)
        .nextStep(addingEshopGroupStep).nextStep(settingPermissionStep);

    return addingCustomerSettingStep;
  }

}
