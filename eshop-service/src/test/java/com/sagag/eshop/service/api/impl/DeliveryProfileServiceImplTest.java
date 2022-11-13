package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileSavingDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryProfileServiceImplTest {

  @InjectMocks
  private DeliveryProfileServiceImpl deliveryProfileService;

  @Mock
  private DeliveryProfileRepository deliveryProfileRepo;

  @Test(expected = DeliveryProfileValidationException.class)
  public void createDeliveryProfile_shouldthrowException_givenDeliveryProfile() throws Exception {
    DeliveryProfileSavingDto profile = DeliveryProfileSavingDto.builder().build();
    deliveryProfileService.createDeliveryProfile(profile, 1L);
    Mockito.verify(deliveryProfileRepo, times(1)).save(Mockito.any(DeliveryProfile.class));
  }

  @Test
  public void createDeliveryProfile_shouldCreateDeliveryProfile_givenDeliveryProfile()
      throws Exception {
    DeliveryProfileSavingDto profile = initialData();
    deliveryProfileService.createDeliveryProfile(profile, 1L);
    Mockito.verify(deliveryProfileRepo, times(1)).save(Mockito.any(DeliveryProfile.class));
  }

  private DeliveryProfileSavingDto initialData() {
    final DeliveryProfileSavingDto profile = new DeliveryProfileSavingDto();
    profile.setCountry("AT");
    profile.setDeliveryProfileId(1);
    profile.setDistributionBranchId(1001);
    profile.setDeliveryBranchId(1005);
    profile.setNextDay(1);
    profile.setLastDelivery("08:00");
    profile.setDeliveryDuration(1);
    return profile;
  }
}
