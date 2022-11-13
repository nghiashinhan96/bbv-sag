package com.sagag.services.service.customer;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.TelephoneFormatException;

import org.springframework.data.domain.Pageable;

public interface ICustomerSearch {

  /**
   * Executes search customers by input info
   *
   * @param affiliate the current affiliate
   * @param input the input search term
   * @param pageable the pagination
   * @return the object of {@link CustomerDataResultDto}
   * @throws TelephoneFormatException, UserValidationException, ErpCustomerNotFoundException
   *
   */
  CustomerDataResultDto search(SupportedAffiliate affiliate, String input, Pageable pageable)
      throws TelephoneFormatException, UserValidationException, ErpCustomerNotFoundException;
}
