package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "V_USER_DETAIL")
public class VEshopUserDetail implements Serializable {

  private static final long serialVersionUID = -2374711264532759853L;

  @Id
  private Long id;

  private Long userId;

  private String userName;

  private String userEmail;

  private String fullName;

  private String telephone;

  private Long userSettingId;

  private Boolean saleOnBehalfOf;

  private Integer roleId;

  private String roleName;

  private Integer groupId;

  private String groupName;

  private Integer orgId;

  private Integer orgParentId;

  private String orgCode;

  private String orgShortName;

  private String orgName;

  private Integer orgSettingsId;
}
