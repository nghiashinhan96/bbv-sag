package com.sagag.services.ax.client;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxBaseIT;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.domain.AxEmployeesResourceSupport;
import com.sagag.services.common.annotation.EshopIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
public class AxEmployeeClientIT extends AxBaseIT {

  @Autowired
  private AxEmployeeClient axClient;

  private static String cachedToken = StringUtils.EMPTY;

  @Test
  @Ignore("AX Pre-Prod is cleaned all emails for sales")
  public void testGetEmployees() {
    String emailAddress = "andreas.kaltenbrunner@derendinger.at";
    ResponseEntity<AxEmployeesResourceSupport> jsonRes =
        axClient.getEmployees(cachedToken, AxDataTestUtils.companyNameOfDDAT(), emailAddress);
    assertOkHttpStatus(jsonRes);
  }

}
