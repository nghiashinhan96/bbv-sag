package com.sagag.eshop.service.dto.finalcustomer;

import com.sagag.services.common.domain.wss.WssMarginGroupDto;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.SalutationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateNewFinalCustomerProfileDto implements Serializable {

  private static final long serialVersionUID = 553272857266294696L;

  private List<String> customerTypes;

  private List<SalutationDto> salutations;

  private boolean showNetPrice;

  private List<PermissionConfigurationDto> permissions;

  private List<DeliveryTypeDto> deliveryTypes;

  private int defaultDeliveryType;

  private boolean showNetPriceEnabled;

  private List<WssMarginGroupDto> marginGroups;

}
