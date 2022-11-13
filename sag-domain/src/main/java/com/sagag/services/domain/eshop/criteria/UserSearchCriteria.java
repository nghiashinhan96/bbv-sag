package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchCriteria  {

  private String affiliate;

  private String customerNumber;

  private String userName;

  private String email;

  private String telephone;

  private String name;

  private Boolean isUserActive;

}
