package com.sagag.services.tools.domain.ax;

import java.util.List;

import com.sagag.services.tools.domain.external.Customer;

import lombok.Data;

@Data
public class AxCustomerInfo {

  private Customer customer;

  private List<Address> addresses;

}
