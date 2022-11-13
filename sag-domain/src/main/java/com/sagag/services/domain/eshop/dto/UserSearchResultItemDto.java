package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResultItemDto implements Serializable {

  private static final long serialVersionUID = 6937058962984767381L;

  private long id;

  private String affiliate;

  private String organisationName;

  private String organisationCode;

  private String userName;

  private String email;

  private String telephone;

  private String roleName;

  private Boolean isUserActive;

}
