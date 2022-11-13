package com.sagag.eshop.repo.criteria;

import com.sagag.services.domain.eshop.criteria.UserExportRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VUserExportSearchCriteria implements Serializable {

  private static final long serialVersionUID = -4413851867585370347L;

  private String affiliate;

  private String customerNumber;

  private String userName;

  private String email;

  private Boolean isUserActive;

  public static VUserExportSearchCriteria fromUserExportRequest(UserExportRequest request) {

    return VUserExportSearchCriteria.builder()
        .affiliate(request.getAffiliate())
        .customerNumber(request.getCustomerNumber())
        .userName(request.getUserName())
        .email(request.getEmail())
        .isUserActive(request.getIsUserActive())
        .build();
  }

}
