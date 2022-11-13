package com.sagag.services.ax.client;

import com.sagag.services.ax.domain.AxEmployee;
import com.sagag.services.ax.domain.AxEmployeesResourceSupport;
import com.sagag.services.common.profiles.AxProfile;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@AxProfile
public class AxEmployeeClient extends AxBaseClient {

  private static final String API_GET_EMPLOYEES_BY_EMAIL =
      "/webshop-service/employees/%s?emailAddress=%s";

  private static final String API_GET_EMPLOYEE_BY_PERSONAL_CODE =
      "/webshop-service/employees/%s/%s";

  /**
   * <p>
   * Retrieves employee representation.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param emailAddress the input employee email address
   * @return the response of {@link AxEmployeesResourceSupport}
   */
  public ResponseEntity<AxEmployeesResourceSupport> getEmployees(String accessToken,
      String companyName, String emailAddress) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(emailAddress, "The given email address must not be empty");
    return exchange(toUrl(API_GET_EMPLOYEES_BY_EMAIL, companyName, emailAddress),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxEmployeesResourceSupport.class);
  }

  public ResponseEntity<AxEmployee> getEmployeeByPersonalNr(String accessToken,
      String companyName, String personalNr) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(personalNr, "The given personal number must not be empty");
    return exchange(toUrl(API_GET_EMPLOYEE_BY_PERSONAL_CODE, companyName, personalNr),
        HttpMethod.GET, toHttpEntityNoBody(accessToken), AxEmployee.class);
  }
}
