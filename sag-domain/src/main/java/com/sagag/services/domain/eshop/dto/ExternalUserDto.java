package com.sagag.services.domain.eshop.dto;

import com.sagag.services.common.enums.ExternalApp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalUserDto implements Serializable {

  private static final long serialVersionUID = -5621850604784079639L;

  private Long id;

  private Long eshopUserId;

  private String username;

  private String password;

  private boolean active;

  private Date createdDate;

  private ExternalApp externalApp;

  private String lockVirtualUser;

}
