package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "V_ACTIVE_USER")
public class VActiveUser {

  @Id
  private Long id;

  private String username;

  private String email;

  private String fullName;

  private String phone;

  private String roleName;

  private String groupName;

  private Long orgId;

  private Long orgParentId;

  private String orgCode;

  private String affiliate;

  private String orgName;

  private Boolean isUserActive;

}
