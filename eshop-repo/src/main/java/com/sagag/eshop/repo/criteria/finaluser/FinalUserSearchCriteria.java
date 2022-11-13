package com.sagag.eshop.repo.criteria.finaluser;

import com.sagag.services.domain.eshop.criteria.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FinalUserSearchCriteria extends BaseRequest implements Serializable {

  private static final long serialVersionUID = 3579850653237210674L;

  private String userName;

  private String fullName;

  private String userEmail;

  private Integer type;

  private FinalUserSearchSortCriteria sort;

  private String firstName;

  private String lastName;

  private Integer salutation;

}
