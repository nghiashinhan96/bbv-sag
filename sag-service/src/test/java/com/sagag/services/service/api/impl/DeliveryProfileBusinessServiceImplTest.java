package com.sagag.services.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.service.api.DeliveryProfileService;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryProfileBusinessServiceImplTest {

  @InjectMocks
  private DeliveryProfileBusinessServiceImpl deliveryProfileBusService;

  @Mock
  private DeliveryProfileService deliveryProfileService;

  @Mock
  private CacheDataProcessor processor;

  @Test(expected = DeliveryProfileValidationException.class)
  public void importExternalVendor_shouldThrowsDeliveryProfileValidationException_givenEmptyList()
      throws Exception {
    Mockito.doThrow(DeliveryProfileValidationException.class)
    .when(deliveryProfileService)
    .replaceDeliveryProfilesByCsv(Mockito.eq(Collections.emptyList()));

    deliveryProfileBusService.importAndRefreshCacheDeliveryProfile(Collections.emptyList());
  }

  @Test
  public void importDeliveryProfile_shouldImportDeliveryProfile_givenDeliveryProfileDTO()
      throws Exception {

    Calendar lastDelivery = Calendar.getInstance();
    List<CsvDeliveryProfileDto> csvDeliveryProfiles = Arrays.asList(CsvDeliveryProfileDto.builder()
        .country("AT").deliveryProfileId(1).distributionBranchId(2).deliveryBranchId(3).nextDay(2)
        .lastDelivery(lastDelivery.getTime()).deliveryDuration("01:30").build());

    deliveryProfileBusService.importAndRefreshCacheDeliveryProfile(csvDeliveryProfiles);

    Mockito.verify(deliveryProfileService, times(1)).replaceDeliveryProfilesByCsv(Mockito.anyList());
  }
}
