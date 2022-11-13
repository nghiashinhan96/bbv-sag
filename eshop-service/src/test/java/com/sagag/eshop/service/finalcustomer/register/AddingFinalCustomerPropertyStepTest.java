package com.sagag.eshop.service.finalcustomer.register;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.finalcustomer.register.result.AddingOrganisationStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AddingFinalCustomerPropertyStepTest extends AbstractNewFinalCustomerTest {

  @InjectMocks
  private AddingFinalCustomerPropertyStep addingFinalCustomerPropertyStep;

  @Mock
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Test
  public void processItem_shouldAddingProperties_givenNewFinalCustomerDto() throws Exception {
    List<FinalCustomerProperty> properties = new ArrayList<>();

    FinalCustomerProperty customerTypeProperty = FinalCustomerProperty.builder().orgId(159l)
        .settingKey(SettingsKeys.FinalCustomer.Settings.TYPE.name()).value("ONLINE").build();
    properties.add(customerTypeProperty);

    FinalCustomerProperty statusProperty = FinalCustomerProperty.builder().orgId(159l)
        .settingKey(SettingsKeys.FinalCustomer.Settings.STATUS.name()).value("ACTIVE").build();
    properties.add(statusProperty);

    when(finalCustomerPropertyRepo.saveAll(any())).thenReturn(properties);

    Organisation organisation = buildOrganisation();
    organisation.setId(1);

    NewFinalCustomerStepResult stepResult = new NewFinalCustomerStepResult();
    stepResult.setAddingOrganisationStepResult(new AddingOrganisationStepResult(organisation));

    NewFinalCustomerStepResult result =
        addingFinalCustomerPropertyStep.processItem(buildNewFinalCustomerDto(), stepResult);

    List<FinalCustomerProperty> savedProperties =
        result.getAddingFinalCustomerPropertyStepResult().getFinalCustomerProperties();
    assertNotNull(savedProperties);
    assertThat(savedProperties.get(0).getValue(), Matchers.is("ONLINE"));
    assertThat(savedProperties.get(1).getValue(), Matchers.is("ACTIVE"));
  }
}
