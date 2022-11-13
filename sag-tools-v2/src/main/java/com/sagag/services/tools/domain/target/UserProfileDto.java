package com.sagag.services.tools.domain.target;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserProfileDto implements Serializable {

  public static final String ON_BEHALF_AGENT_PREFIX = "Agent-";
  public static final String ON_BEHALF_AGENT_SURNAME = "Agent";

  private static final long serialVersionUID = 1L;
  private long id;
  private String vinCall;
  private String license;
  private String userName;
  private String surName;
  private String firstName;
  private List<SalutationDto> salutations;
  private int salutationId;
  private String email;
  private String phoneNumber;
  private List<LanguageDto> languages;
  private int languageId;
  private List<EshopRoleDto> types;
  private int typeId;
  private String accessUrl;
  private boolean vatConfirm;
  private boolean emailConfirmation;
  private Double hourlyRate;
  private Boolean hasPartnerprogramView;

  private String userType;
}
