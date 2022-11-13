package com.sagag.eshop.repo.api;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.ExternalVendor;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class ExternalVendorRepositoryIT {

  @Autowired
  private ExternalVendorRepository externalVendorRepo;

  @Test
  public void findAll_shouldReturnVendors() throws Exception {
    List<ExternalVendor> vendors = externalVendorRepo.findAll();
    assertNotNull(vendors);
  }

  @Test
  public void deleteAll_shouldReturnVendors() throws Exception {
    externalVendorRepo.deleteAll();
    List<ExternalVendor> vendors = externalVendorRepo.findAll();
    Assert.assertThat(vendors, is(empty()));
  }
}
