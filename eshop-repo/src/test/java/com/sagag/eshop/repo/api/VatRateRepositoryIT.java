package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.VatRate;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link VatRateRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VatRateRepositoryIT {

  @Autowired
  private VatRateRepository vatRateRepository;

  @Test
  public void testFindAll() {
    final List<VatRate> vatRates = vatRateRepository.findAll();
    Assert.assertThat(vatRates.size(),Matchers.greaterThan(2)); 
  }
}
