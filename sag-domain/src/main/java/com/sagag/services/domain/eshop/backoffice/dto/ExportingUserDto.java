package com.sagag.services.domain.eshop.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportingUserDto implements Serializable {

  private static final long serialVersionUID = -6851948344237278394L;

  private String customerNumber;

  private String dvseCustomerName;

  private String dvseUserName;

  private String affiliateShortname;

  private String roleName;

  private String salutation;

  private String language;

  private String userName;

  private String firstName;

  private String lastName;

  private String email;

  private String zip;

  private Date firstLoginDate;

  private Date lastLoginDate;
}
