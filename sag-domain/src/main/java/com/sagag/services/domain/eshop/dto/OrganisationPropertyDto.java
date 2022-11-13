package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrganisationPropertyDto implements Serializable {

  private static final long serialVersionUID = 2886894883263297758L;

  private Long id;

  private Long organisationId;

  private String type;

  private String value;
}
