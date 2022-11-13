package com.sagag.services.domain.eshop.dto.externalvendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalVendorDto implements Serializable{

  private static final long serialVersionUID = 110965901435957769L;

  private Integer id;
  private String country;
  private String sagArticleGroup;
  private Long brandId;
  private String vendorId;
  private String vendorName;
  private Integer vendorPriority;
  private Integer deliveryProfileId;
  private String availabilityTypeId;
}
