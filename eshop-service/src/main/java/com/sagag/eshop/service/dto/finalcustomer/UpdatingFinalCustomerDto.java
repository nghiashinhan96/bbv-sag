package com.sagag.eshop.service.dto.finalcustomer;

import com.sagag.services.common.domain.wss.WssMarginGroupDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.SalutationDto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpdatingFinalCustomerDto implements Serializable {

  private static final long serialVersionUID = -4994284634138821394L;

  private List<String> customerTypes;

  private List<SalutationDto> salutations;

  private List<DeliveryTypeDto> deliveryTypes;

  private SavingFinalCustomerDto selectedFinalCustomer;

  private boolean showNetPriceEnabled;

  private List<WssMarginGroupDto> marginGroups;
}
