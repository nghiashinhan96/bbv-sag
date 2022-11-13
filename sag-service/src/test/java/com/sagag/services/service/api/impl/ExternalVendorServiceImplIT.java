package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.ExternalVendorRepository;
import com.sagag.eshop.repo.entity.ExternalVendor;
import com.sagag.eshop.repo.entity.VExternalVendor;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.CsvExternalVendorDto;
import com.sagag.eshop.service.exception.ExternalVendorValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.criteria.ExternalVendorSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.service.api.ExternalVendorService;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * Integration tests for external vendor service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class ExternalVendorServiceImplIT {

  @Autowired
  private ExternalVendorService externalVendorService;

  @Autowired
  private ExternalVendorRepository externalVendorRepo;

  private Long userId = 1l;

  @Test
  public void importExternalVendor_shouldImportData_givenExternalVenDorDto()
      throws ExternalVendorValidationException {
    CsvExternalVendorDto csvExternalVendor =
        CsvExternalVendorDto.builder().country("AT").vendorId(1L).vendorName("Test")
            .vendorPriority(1).deliveryProfileId(1).availabilityTypeId("VEN").build();
    externalVendorService.importExternalVendor(Arrays.asList(csvExternalVendor));
    List<ExternalVendor> externalVendors = externalVendorRepo.findAll();
    Assert.assertThat(externalVendors.size(), Is.is(1));
  }

  @Test
  public void searchExternalVendor_shouldReturnExternalVendor_givenCountry() {
    ExternalVendorSearchCriteria search =
        ExternalVendorSearchCriteria.builder().country("AT").build();
    Page<VExternalVendor> result = externalVendorService.searchExternalVendor(search);
    Assert.assertThat(result.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchExternalVendor_shouldReturnExternalVendor_givenDeliveryProfileName() {
    ExternalVendorSearchCriteria search =
        ExternalVendorSearchCriteria.builder().deliveryProfileName("test").build();
    Page<VExternalVendor> result = externalVendorService.searchExternalVendor(search);
    Assert.assertThat(result.hasContent(), Matchers.is(true));
  }

  @Test
  public void createExternalVendor_shoulCreateExternalVendor_givenExternalVendorRequest()
      throws ExternalVendorValidationException {
    String vendorName = "Vendor Name Test";
    ExternalVendorDto request = new ExternalVendorDto();
    request.setCountry("AT");
    request.setBrandId(1L);
    request.setVendorId("1");
    request.setVendorName(vendorName);
    request.setVendorPriority(3);
    request.setDeliveryProfileId(1);
    request.setAvailabilityTypeId("CON");
    externalVendorService.createExternalVendor(request, userId);
    boolean checkExistedExternalVendor = externalVendorRepo
        .exists(Example.of(ExternalVendor.builder().vendorName(vendorName).build()));
    Assert.assertThat(checkExistedExternalVendor, Matchers.is(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createExternalVendor_shoulThrowsException_givenNullCountry()
      throws ExternalVendorValidationException {
    ExternalVendorDto request = new ExternalVendorDto();
    request.setCountry(null);
    externalVendorService.createExternalVendor(request, userId);
  }

  @Test
  public void createExternalVendor_shouldAllowDuplicateVendorName()
      throws ExternalVendorValidationException {
    // #1946
    ExternalVendorDto request = new ExternalVendorDto();
    request.setCountry("AT");
    request.setBrandId(1L);
    request.setVendorId("1");
    request.setVendorName("AUTONET-HU");
    request.setVendorPriority(3);
    request.setDeliveryProfileId(1);
    request.setAvailabilityTypeId("CON");
    externalVendorService.createExternalVendor(request, userId);
  }

  @Test
  public void createExternalVendor_shouldAllowDuplicateVendorID()
      throws ExternalVendorValidationException {
    // #1946
    ExternalVendorDto request = new ExternalVendorDto();
    request.setCountry("AT");
    request.setBrandId(1L);
    request.setVendorId("1");
    request.setVendorName("AUTONET-TTTT");
    request.setVendorPriority(3);
    request.setDeliveryProfileId(1);
    request.setAvailabilityTypeId("CON");
    externalVendorService.createExternalVendor(request, userId);
  }

  @Test
  public void updateExternalVendor_shouldAllowMissingId()
      throws ExternalVendorValidationException {
    // #1946
    String vendorName = "Vendor Name Test";
    ExternalVendorDto request = new ExternalVendorDto();
    request.setCountry("AT");
    request.setBrandId(1L);
    request.setVendorId(null);
    request.setVendorName(vendorName);
    request.setVendorPriority(3);
    request.setDeliveryProfileId(1);
    request.setAvailabilityTypeId("CON");
    request.setId(213456);
    externalVendorService.updateExternalVendor(request, userId);
  }

  @Test
  public void delete_shouldDeleteExternalVendor_giveExternalVendorId() {
    Integer deleteExternalVendorId = 1;
    externalVendorService.delete(deleteExternalVendorId);
    Optional<ExternalVendor> externalVendor = externalVendorRepo.findById(deleteExternalVendorId);
    Assert.assertThat(externalVendor.isPresent(), Is.is(false));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void delete_throwsException_giveNonExistedExternalVendorId()
      throws EmptyResultDataAccessException {
    Integer deleteExternalVendorId = 123456;
    externalVendorService.delete(deleteExternalVendorId);
  }
}
