package com.sagag.services.hazelcast.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * Session cache customer info for the current logging user<br/>
 * to avoid the request at server side to get customer info again to SWS AX and thus, to reduce performance.
 */
@Data
@JsonInclude(Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedCustomerInfo implements Serializable {

  private static final long serialVersionUID = 6104002983995084264L;

  private String custNr;

  private Customer customer;

  private List<Address> customerAddresses;

  public static class CachedCustomerInfoBuilder {

    public CachedCustomerInfoBuilder customer(Customer customer) {
      Assert.notNull(customer, "customer must not be null");
      Assert.notNull(customer.getNr(), "customer nr must not be null");
      this.customer = customer;
      this.custNr = String.valueOf(customer.getNr());
      return this;
    }

  }
}
