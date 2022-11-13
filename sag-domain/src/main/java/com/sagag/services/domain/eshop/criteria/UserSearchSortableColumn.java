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
public class UserSearchSortableColumn implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 558991665093142659L;

  private Boolean orderByOrganisationNameDesc;

  private Boolean orderByCustomerNumberDesc;

  private Boolean orderByUserNameDesc;

  private Boolean orderByEmailDesc;

  private Boolean orderByStatusDesc;

  private Boolean orderByRoleDesc;

}
