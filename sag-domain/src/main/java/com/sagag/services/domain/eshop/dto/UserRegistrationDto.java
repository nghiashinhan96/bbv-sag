package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto implements Serializable {

  private static final long serialVersionUID = -1898396360889763970L;

  private String customerNumber;

  private String affiliate;

  private String firstName;

  private String surName;

  private String userName;

  private String address;

  private String optionalAddress;

  private String postCode;

  private String city;

  private String phoneNumber;

  private String faxNumber;

  private String email;

  private String homePage;

  private String description;

  private String langIso;

  private String accessUrl;

  private String userType;

  private String collectionShortName;

  private boolean hasPartnerprogramView;

  private boolean createOnBehalfOnly;

  private String passwordHash;

  private String passwordSalt;
}
