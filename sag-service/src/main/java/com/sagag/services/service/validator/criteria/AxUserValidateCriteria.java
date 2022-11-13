package com.sagag.services.service.validator.criteria;

import com.sagag.eshop.repo.entity.EshopUser;

import lombok.Data;

/**
 * Criteria for validate Ax User belong to Ax services.
 */
@Data
public class AxUserValidateCriteria {

  private final EshopUser eshopUser;

  private final String affiliate;

}
