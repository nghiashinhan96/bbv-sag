package com.sagag.eshop.service.finalcustomer.register.result;

import com.sagag.eshop.repo.entity.EshopGroup;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddingEshopGroupStepResult {

  private List<EshopGroup> eshopGroups;
}
