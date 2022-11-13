package com.sagag.eshop.service.dto.finalcustomer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FinalCustomerUserDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userName;

  private String fullName;

  private String userEmail;

  private String type;

  private Long userId;

  private String salutation;

  private String firstName;

  private String lastName;

}
