package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.eshop.service.exception.ExternalVendorValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;



/**
 * Integration tests for external vendor service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("This test case should be adapted by Quang, because he changes some business at this services")
public class DeliveryProfileBusinessServiceImplIT {

  @Autowired
  private DeliveryProfileBusinessServiceImpl deliveryProfileBusinessService;

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepository;

  @Autowired
  @Qualifier("deliveryProfileCacheServiceImpl")
  private CacheDataProcessor processor;

  @Test
  public void importDeliveryProfile_shouldImportData_givenDeliveryProfileDto()
      throws ExternalVendorValidationException, DeliveryProfileValidationException {
    Calendar lastDelivery = Calendar.getInstance();
    List<CsvDeliveryProfileDto> csvDeliveryProfiles = Arrays.asList(CsvDeliveryProfileDto.builder()
        .country("AT").deliveryProfileId(1).distributionBranchId(2).deliveryBranchId(3).nextDay(2)
        .lastDelivery(lastDelivery.getTime()).deliveryDuration("01:30").build());
    deliveryProfileBusinessService.importAndRefreshCacheDeliveryProfile(csvDeliveryProfiles);
    List<DeliveryProfile> deliveryProfiles = deliveryProfileRepository.findAll();
    Assert.assertThat(deliveryProfiles.size(), Is.is(1));
  }

}
