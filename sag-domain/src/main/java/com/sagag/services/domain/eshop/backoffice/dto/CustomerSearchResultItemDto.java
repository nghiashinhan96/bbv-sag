package com.sagag.services.domain.eshop.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchResultItemDto implements Serializable {

  private static final long serialVersionUID = -7550926489890016956L;

  private String customerNr;

  private String affiliate;

  private String companyName;

}
