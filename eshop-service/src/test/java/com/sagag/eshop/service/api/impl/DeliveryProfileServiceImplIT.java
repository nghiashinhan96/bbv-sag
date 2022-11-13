package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.eshop.repo.entity.VDeliveryProfile;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.criteria.DeliveryProfileSearchCriteria;
import com.sagag.services.domain.eshop.criteria.DeliveryProfileSearchSortableColumn;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileSavingDto;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import javax.transaction.Transactional;

/**
 * Integration tests for branch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class DeliveryProfileServiceImplIT {

  @Autowired
  private DeliveryProfileServiceImpl deliveryProfileService;

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepo;

  @Test
  public void searchDeliveryProfile_shouldReturnDeliveryProfile_givenCountryCode() {
    DeliveryProfileSearchCriteria search =
        DeliveryProfileSearchCriteria.builder().country("AT").build();
    Page<VDeliveryProfile> result = deliveryProfileService.searchDeliveryProfile(search);
    Assert.assertThat(result.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchDeliveryProfile_shouldReturnDeliveryProfile_givenVendorCutOffTime() {
    DeliveryProfileSearchCriteria search =
        DeliveryProfileSearchCriteria.builder().vendorCutOffTime("17:00").build();
    Page<VDeliveryProfile> result = deliveryProfileService.searchDeliveryProfile(search);
    Assert.assertThat(result.hasContent(), Matchers.is(false));
  }

  @Test
  public void searchDeliveryProfile_shouldReturnDeliveryProfile_givenDeliveryBranch() {
    DeliveryProfileSearchCriteria search =
        DeliveryProfileSearchCriteria.builder().deliveryBranchCode("KIR").build();
    Page<VDeliveryProfile> result = deliveryProfileService.searchDeliveryProfile(search);
    Assert.assertThat(result.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchDeliveryProfile_shouldOrderDeliveryProfile_givenOrderCountryCode() {
    DeliveryProfileSearchCriteria search = DeliveryProfileSearchCriteria.builder()
        .sort(DeliveryProfileSearchSortableColumn.builder().orderByCountryDesc(true).build())
        .build();
    Page<VDeliveryProfile> result = deliveryProfileService.searchDeliveryProfile(search);
    Assert.assertThat(result.getContent().get(0).getCountry(), Matchers.is("Austria"));
  }

  @Test
  public void searchDeliveryProfile_shouldReturnDeliveryProfile_givenDurationTime() {
    final Integer durationTimeMinutes = 7200;
    DeliveryProfileSearchCriteria search =
        DeliveryProfileSearchCriteria.builder().deliveryDuration(durationTimeMinutes).build();
    Page<VDeliveryProfile> result = deliveryProfileService.searchDeliveryProfile(search);
    Assert.assertThat(result.hasContent(), Matchers.is(true));
  }

  @Test
  public void createDeliveryProfile_shouldCreateDeliveryProfile()
      throws DeliveryProfileValidationException {
    final DeliveryProfileSavingDto profile = initialData();
    profile.setDeliveryBranchId(1234);
    deliveryProfileService.createDeliveryProfile(profile, 1l);
    final boolean existed = deliveryProfileRepo.exists(Example
        .of(DeliveryProfile.builder().deliveryBranchId(1234).distributionBranchId(1001).build()));
    Assert.assertThat(existed, Matchers.is(true));

  }

  @Test(expected = DeliveryProfileValidationException.class)
  public void createDeliveryProfile_shouldThrowsException_duplicateData()
      throws DeliveryProfileValidationException {
    final DeliveryProfileSavingDto profile = initialData();
    profile.setDistributionBranchId(1001);
    profile.setDeliveryBranchId(1005);
    deliveryProfileService.createDeliveryProfile(profile, 1l);
  }

  @Test(expected = DeliveryProfileValidationException.class)
  public void createDeliveryProfile_shouldThrowsException_missingDeliveryBranchId()
      throws DeliveryProfileValidationException {
    final DeliveryProfileSavingDto profile = initialData();
    profile.setDeliveryBranchId(null);
    deliveryProfileService.createDeliveryProfile(profile, 1l);
  }

  @Test
  public void shouldUpdateDeliveryProfile() throws DeliveryProfileValidationException {
    final DeliveryProfileSavingDto profile = initialData();
    final String deliveryProfileName = "Delivery Profile Test";
    profile.setId(1);
    profile.setDeliveryProfileName(deliveryProfileName);
    deliveryProfileService.updateDeliveryProfile(profile, 1l);
    Optional<DeliveryProfile> deliveryOpt = deliveryProfileRepo.findById(1);
    deliveryOpt.ifPresent(de -> {
      Assert.assertThat(de.getDeliveryProfileName(), Matchers.is(deliveryProfileName));
    });
  }

  @Test(expected = DeliveryProfileValidationException.class)
  public void updateDeliveryProfile_shouldThrowException_duplicateData()
      throws DeliveryProfileValidationException {
    final DeliveryProfileSavingDto profile = initialData();
    profile.setId(1);
    profile.setDistributionBranchId(1001);
    profile.setDeliveryBranchId(1008);
    deliveryProfileService.updateDeliveryProfile(profile, 1l);
  }

  @Test
  public void shouldRemoveDeliveryProfile() throws DeliveryProfileValidationException {
    deliveryProfileService.removeDeliveryProfile(6);
  }

  @Test(expected = DeliveryProfileValidationException.class)
  public void shouldNotRemoveDeliveryProfile() throws DeliveryProfileValidationException {
    deliveryProfileService.removeDeliveryProfile(1);
  }

  @Test
  public void findDeliveryProfileNameByDeliveryProfileId_shouldReturnProfileName_InputDeliveryProfileId() {
    final Integer deliveryProfileId = 1;
    DeliveryProfileDto deliveryDto =
        deliveryProfileService.findProfileNameByDeliveryProfileId(deliveryProfileId);
    Optional.of(deliveryDto).ifPresent(de -> {
      Assert.assertThat(de.getDeliveryProfileName(), Matchers.not(StringUtils.EMPTY));
    });
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
