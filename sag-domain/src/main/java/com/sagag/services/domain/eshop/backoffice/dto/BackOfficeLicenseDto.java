package com.sagag.services.domain.eshop.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BackOfficeLicenseDto implements Serializable {

  private static final long serialVersionUID = 6481864110416606186L;

  private long id;

  private String typeOfLicense;

  private String packName;

  private Long customerNr;

  private String beginDate;

  private String endDate;

  private int quantity;

  private int quantityUsed;
}
