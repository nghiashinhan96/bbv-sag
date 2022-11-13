package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserSearchCriteriaRequest extends BaseRequest {

  /**
   *
   */
  private static final long serialVersionUID = -8218186625546043830L;

  private String affiliate;

  private String customerNumber;

  private String userName;

  private String email;

  private String telephone;

  private String name;

  private Boolean isUserActive;

  private UserSearchSortableColumn sorting;

}
