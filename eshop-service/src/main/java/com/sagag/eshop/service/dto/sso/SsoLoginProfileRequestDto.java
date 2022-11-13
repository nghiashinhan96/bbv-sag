package com.sagag.eshop.service.dto.sso;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class SsoLoginProfileRequestDto implements Serializable {

  private static final long serialVersionUID = 7143428014766761605L;

  private String uuid;

  private String userName;

  private String surName;

  private String firstName;

  private String email;

  private String phoneNumber;

  private int salutationId;

  private int languageId;

  private Double hourlyRate;

  private List<String> groupUuids;
}
