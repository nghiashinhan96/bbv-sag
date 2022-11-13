package com.sagag.eshop.service.finalcustomer.register.result;

import com.sagag.eshop.repo.entity.Organisation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddingOrganisationStepResult {

  private Organisation organisation;
}
