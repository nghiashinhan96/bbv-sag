package com.sagag.services.incentive.domain;

import com.sagag.services.common.enums.HashType;

import lombok.Data;

import java.io.Serializable;

/**
 * Incentive Login info to request happy bonus link.
 */
@Data
public class IncentiveLoginDto implements Serializable {

  private static final long serialVersionUID = -3053090231904077215L;

  private String userName;

  private String password;

  private HashType type;

  private String custNr;
}
