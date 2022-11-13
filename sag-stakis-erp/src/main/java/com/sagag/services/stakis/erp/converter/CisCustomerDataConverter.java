package com.sagag.services.stakis.erp.converter;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.stakis.erp.enums.StakisCustomerDataType;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

public interface CisCustomerDataConverter<R>
  extends Function<OutCustomer, R>, BiConsumer<CustomerInfo, OutCustomer> {

  /**
   * Returns the customer data type.
   *
   * @return the customer data type
   */
  StakisCustomerDataType type();

}
