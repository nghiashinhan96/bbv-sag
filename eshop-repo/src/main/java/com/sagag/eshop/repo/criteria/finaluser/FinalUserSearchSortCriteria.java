package com.sagag.eshop.repo.criteria.finaluser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalUserSearchSortCriteria implements Serializable {

  private static final long serialVersionUID = -8520593114989033967L;

  private Boolean orderByUserNameDesc;

  private Boolean orderByFullNameDesc;

  private Boolean orderByEmailDesc;

  private Boolean orderByFirstNameDesc;

  private Boolean orderByLastNameDesc;

}
