package com.sagag.eshop.service.finalcustomer.register.result;

import com.sagag.services.common.step.StepResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NewFinalCustomerStepResult extends StepResult {

  private AddingCustomerSettingStepResult addingCustomerSettingStepResult;
  private AddingOrganisationStepResult addingOrganisationStepResult;
  private AddingFinalCustomerPropertyStepResult addingFinalCustomerPropertyStepResult;
  private AssigningToCollectionStepResult assigningToCollectionStepResult;
  private AddingEshopGroupStepResult addingEshopGroupStepResult;
}
