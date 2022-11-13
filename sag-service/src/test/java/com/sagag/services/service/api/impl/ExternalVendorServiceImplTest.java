package com.sagag.services.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.ExternalVendorRepository;
import com.sagag.eshop.service.dto.CsvExternalVendorDto;
import com.sagag.eshop.service.exception.ExternalVendorValidationException;
import com.sagag.services.hazelcast.api.SupplierCacheService;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class ExternalVendorServiceImplTest {

  @InjectMocks
  private ExternalVendorServiceImpl externalVendorService;

  @Mock
  private ExternalVendorRepository externalVendorRepo;
  
  @Mock
  private CacheDataProcessor processor;

  @Mock
  private SupplierCacheService supplierCacheService;

  @Test(expected = ExternalVendorValidationException.class)
  public void importExternalVendor_shouldExternalVendorValidationException_givenEmptyList()
      throws Exception {
    externalVendorService.importExternalVendor(Collections.emptyList());
  }

  @Test()
  public void importExternalVendor_shouldImportExternalVendor_givenExternalVendor()
      throws Exception {
    CsvExternalVendorDto csvExternalVendor =
        CsvExternalVendorDto.builder().country("AT").vendorId(1L).vendorName("Test")
            .vendorPriority(1).deliveryProfileId(1).availabilityTypeId("VEN").build();
    externalVendorService.importExternalVendor(Arrays.asList(csvExternalVendor));
    Mockito.verify(externalVendorRepo, times(1)).saveAll(Mockito.anyList());

  }
}
