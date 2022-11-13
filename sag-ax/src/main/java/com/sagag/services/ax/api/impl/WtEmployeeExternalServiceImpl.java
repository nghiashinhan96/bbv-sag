package com.sagag.services.ax.api.impl;

import com.sagag.services.ax.domain.AxEmployee;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.sag.external.Employee;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@SbProfile
public class WtEmployeeExternalServiceImpl extends AxEmployeeExternalServiceImpl {

  @Autowired
  private AxPaymentTypeTranslator axPaymentTypeTranslator;

  @Override
  public PaymentMethodType getConnectPaymentForSales(String axPaymentType, boolean isValidKSL) {
    return axPaymentTypeTranslator.translateToConnect(axPaymentType);
  }

  @Override
  public Optional<Employee> findEmployee(String companyName, String personalNr) {
    Function<String, ResponseEntity<AxEmployee>> function =
      token -> getOrDefaultThrow(() -> axEmpClient.getEmployeeByPersonalNr(
        token, companyName, personalNr));
    ResponseEntity<AxEmployee> employeeRes = retryIfExpiredToken(function);
    if (!employeeRes.hasBody()) {
      return Optional.empty();
    }
    return Optional.of(employeeRes.getBody().toEmployeeDto());
  }
}
