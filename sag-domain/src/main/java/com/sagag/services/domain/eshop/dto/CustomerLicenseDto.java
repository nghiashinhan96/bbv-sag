package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLicenseDto implements Serializable {

  private static final long serialVersionUID = 507724704250704574L;

  private String typeOfLicense;

  private String packName;

  private long customerNr;

  private String beginDate;

  private String endDate;

  private int quantity;

}
