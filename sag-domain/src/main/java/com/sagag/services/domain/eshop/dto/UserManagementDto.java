package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class UserManagementDto implements Serializable{
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  String garageName;
  String salutation;
  String role;
  String firstName;
  String lastName;
  boolean isUserActive;
  long id;
  boolean canDelete;
  String userName;
  boolean isUserAdmin;
}
