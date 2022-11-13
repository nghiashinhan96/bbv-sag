package com.sagag.services.ax.api.impl;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.ax.client.AxEmployeeClient;
import com.sagag.services.ax.domain.AxEmployee;
import com.sagag.services.ax.domain.AxEmployeesResourceSupport;
import com.sagag.services.ax.translator.AxPaymentTypeForSalesTranslator;
import com.sagag.services.ax.translator.AxPaymentTypeForSalesWithKSLTranslator;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.domain.sag.external.Employee;

@Service
@DynamicAxProfile
public class AxEmployeeExternalServiceImpl extends AxProcessor implements EmployeeExternalService {

  @Autowired
  protected AxEmployeeClient axEmpClient;

  @Autowired
  private AxPaymentTypeForSalesTranslator axPaymentTypeForSalesTranslator;

  @Autowired
  private AxPaymentTypeForSalesWithKSLTranslator axPaymentTypeForSalesWithKSLTranslator;

  @Override
  public Optional<Employee> findEmployee(String companyName, String emailAddress) {
    Function<String, ResponseEntity<AxEmployeesResourceSupport>> function =
      token -> getOrDefaultThrow(() -> axEmpClient.getEmployees(
        token, companyName, emailAddress));
    ResponseEntity<AxEmployeesResourceSupport> employeeRes = retryIfExpiredToken(function);
    if (!employeeRes.hasBody() || !employeeRes.getBody().hasEmployees()) {
      return Optional.empty();
    }
    return employeeRes.getBody().getEmployees().stream().map(AxEmployee::toEmployeeDto)
      .findFirst();
  }

  @Override
  public PaymentMethodType getConnectPaymentForSales(final String axPaymentType,
    final boolean isValidKSL) {
    return isValidKSL ? axPaymentTypeForSalesWithKSLTranslator.translateToConnect(axPaymentType)
                      : axPaymentTypeForSalesTranslator.translateToConnect(axPaymentType);
  }

}
