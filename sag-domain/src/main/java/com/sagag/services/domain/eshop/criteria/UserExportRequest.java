package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExportRequest implements Serializable {

  private static final long serialVersionUID = -2651709991321977271L;

  private String affiliate;

  private String customerNumber;

  private String userName;

  private String email;

  private Boolean isUserActive;

}
