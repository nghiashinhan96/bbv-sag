package com.sagag.eshop.service.finalcustomer.register;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.PriceDisplayTypeRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.service.api.PriceDisplayTypeService;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddingCustomerSettingStepTest extends AbstractNewFinalCustomerTest {

  @InjectMocks
  private AddingCustomerSettingStep addingCustomerSettingStep;

  @Mock
  private CustomerSettingsRepository customerSettingsRepository;

  @Mock
  private PriceDisplayTypeRepository priceDisplayTypeRepo;

  @Mock
  private PriceDisplayTypeService priceDisplayTypeService;


  @Test
  public void processItem_shouldAddingCustomerSetting_givenNewFinalCustomerdto() throws Exception {
    when(customerSettingsRepository.findSettingsByOrgId(anyInt()))
        .thenReturn(buildCustomerSetting());

    when(priceDisplayTypeService.getDefaultPriceDisplayType())
    .thenReturn(PriceDisplayTypeEnum.UVPE_OEP_GROSS);

    when(priceDisplayTypeRepo.findByType(PriceDisplayTypeEnum.UVPE_OEP_GROSS.name()))
        .thenReturn(buildPriceType());

    when(customerSettingsRepository.save(any(CustomerSettings.class)))
    .thenReturn(buildCustomerSetting());


    NewFinalCustomerStepResult result = addingCustomerSettingStep
        .processItem(buildNewFinalCustomerDto(), new NewFinalCustomerStepResult());
    CustomerSettings cutomerSetting =
        result.getAddingCustomerSettingStepResult().getCustomerSettings();
    assertNotNull(cutomerSetting);
    assertTrue(cutomerSetting.isNetPriceView());
  }
}
