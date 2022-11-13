package com.sagag.eshop.repo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "V_USER_EXPORT")
@NoArgsConstructor
public class VUserExport {

  @Id
  private Long id;

  private Long userId;

  private String orgCode;

  private String externalCustomerName;

  private String externalUserName;

  private String userName;

  private String firstName;

  private String lastName;

  private String userEmail;

  private String zipcode;

  private String orgParentShortName;

  private Date firstLoginDate;

  private Date lastLoginDate;

  private String roleName;

  private String salutation;

  private String langiso;

  private Boolean isUserActive;
}
