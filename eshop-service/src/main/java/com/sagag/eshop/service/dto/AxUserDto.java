package com.sagag.eshop.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Class of created AX User.
 */
@Data
public class AxUserDto implements Serializable {

  private static final long serialVersionUID = -7970504049501530969L;

  private long externalId;
  private long eshopUserId;
  private String username;

}
