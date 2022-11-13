package com.sagag.services.domain.eshop.dto;

import com.sagag.services.common.enums.ExternalApp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalOrganisationDto implements Serializable {

  private static final long serialVersionUID = 7531944217295958667L;

  private Long id;

  private Integer orgId;

  private String externalCustomerId;

  private String externalCustomerName;

  private ExternalApp externalApp;

}
