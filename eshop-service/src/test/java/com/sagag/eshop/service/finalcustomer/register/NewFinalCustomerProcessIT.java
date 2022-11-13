package com.sagag.eshop.service.finalcustomer.register;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class NewFinalCustomerProcessIT extends AbstractNewFinalCustomerTest {

  @Autowired
  private NewFinalCustomerProcess newFinalCustomerProcess;

  @Test
  public void processRequest_shouldProcessRequest_givenNewFinalCustomerDto() throws Exception {
    NewFinalCustomerStepResult stepResult = new NewFinalCustomerStepResult();
    NewFinalCustomerStepResult result =
        newFinalCustomerProcess.setUpSteps().processRequest(buildNewFinalCustomerDto(), stepResult);
    assertNotNull(result);
  }
}
