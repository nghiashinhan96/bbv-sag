package com.sagag.services.article.api;

import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.domain.sag.external.Employee;
import java.util.Optional;

public interface EmployeeExternalService {

  /**
   * Returns the Retrieves employee representation.
   *
   * @param companyName The company to which the webshop belongs, e.g. 'Derendinger-Switzerland’.
   * @param input       The email addressm, usernanme, employee number of the employee.
   * @return the {@link Employee} The list of employee is embedded in this resource representation.
   */
  Optional<Employee> findEmployee(String companyName, String input);

  /**
   * Returns payment type in connect for sales on behalf
   *
   * @param axPaymentType payment type in ax
   * @param isValidKSL    param to check customer has permission to show KSL or not
   * @return the payment type in connect
   */
  PaymentMethodType getConnectPaymentForSales(String axPaymentType, boolean isValidKSL);
}
