package com.sagag.services.domain.eshop.dto.externalvendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportExternalVendorDto implements Serializable {

  private static final long serialVersionUID = 4221295362110728025L;

  private List<CountryDto> countries;
  private List<String> availabilityType;
  private List<SupportBrandDto> brands;
  private List<DeliveryProfileDto> deliveryProfile;
}
