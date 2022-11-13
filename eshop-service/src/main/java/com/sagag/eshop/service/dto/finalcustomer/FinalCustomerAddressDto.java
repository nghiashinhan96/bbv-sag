package com.sagag.eshop.service.dto.finalcustomer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalCustomerAddressDto implements Serializable {


  private static final long serialVersionUID = 8082307298192017601L;

  private String customerName;

  private String street;

  private String address1;

  private String address2;

  private String postcode;

  private String place;
}
