package com.sagag.services.service.dto.registration;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;


public class PotentialCustomerRegistrationBuiderTest {

  @Test
  public void testName() throws Exception {
    PotentialCustomerRegistrationField company = PotentialCustomerRegistrationField.builder()
        .key("COMPANY").value("Company A").title("Company").build();
    PotentialCustomerRegistrationField email = PotentialCustomerRegistrationField.builder()
        .key("EMAIL").value("kaka@bbv.ch").title("Email").build();
    PotentialCustomerRegistrationDto model =
        PotentialCustomerRegistrationDto.builder().collectionShortName("derendinger-at")
            .fields(Arrays.asList(company, email)).langCode("de").build();
    PotentialCustomerRegistrationBuider.buildHtml(model);

    Assert.assertThat(PotentialCustomerRegistrationBuider.buildHtml(model),
        Matchers.is("<div>Company: Company A</div><div>Email: kaka@bbv.ch</div>"));
  }
}
