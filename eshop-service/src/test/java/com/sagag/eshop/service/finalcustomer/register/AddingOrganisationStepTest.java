package com.sagag.eshop.service.finalcustomer.register;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.OrganisationTypeRepository;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationType;
import com.sagag.eshop.service.finalcustomer.register.result.AddingCustomerSettingStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.enums.OrganisationTypeEnum;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AddingOrganisationStepTest extends AbstractNewFinalCustomerTest {

  @InjectMocks
  private AddingOrganisationStep addingOrganisationStep;

  @Mock
  private OrganisationTypeRepository organisationTypeRepo;

  @Mock
  private OrganisationRepository organisationRepo;

  @Test
  public void processItem_shouldCreateOrganisation_givenNewFinalCustomerDto() throws Exception {

    OrganisationType orgType = OrganisationType.builder().id(4).name("FINAL_CUSTOMER")
        .description("This is Wholesaler organisation").level(4).build();
    when(organisationTypeRepo.findOneByName(OrganisationTypeEnum.FINAL_CUSTOMER.name()))
        .thenReturn(Optional.of(orgType));

    NewFinalCustomerStepResult stepResult = new NewFinalCustomerStepResult();
    stepResult.setAddingCustomerSettingStepResult(
        new AddingCustomerSettingStepResult(buildCustomerSetting()));

    when(organisationRepo.save(any(Organisation.class))).thenReturn(buildOrganisation());

    NewFinalCustomerStepResult result =
        addingOrganisationStep.processItem(buildNewFinalCustomerDto(), stepResult);
    Organisation createdOrganisation = result.getAddingOrganisationStepResult().getOrganisation();

    assertNotNull(createdOrganisation);
    assertThat(createdOrganisation.getParentId(), Matchers.is(137));
    assertThat(createdOrganisation.getDescription(), Matchers.is("final customer 1"));
    assertThat(createdOrganisation.getShortname(), Matchers.is("customer - 100"));
  }

}
