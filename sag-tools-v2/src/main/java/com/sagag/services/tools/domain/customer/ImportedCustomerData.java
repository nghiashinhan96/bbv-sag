package com.sagag.services.tools.domain.customer;

import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.external.Customer;
import com.sagag.services.tools.support.PermissionEnum;
import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.Data;

import java.util.List;

@Data
public class ImportedCustomerData {

  private boolean existed;

  private String customerNr;

  private SupportedAffiliate affiliate;

  private Customer customer;

  private List<Address> customerAddresses;

  private boolean allowCreateDvseAccount;

  private List<PermissionEnum> collecionPermissions;

}
