package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for {@link VinLoggingRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VinLoggingRepositoryIT {

  @Autowired
  private VinLoggingRepository repository;

  @Test
  public void shouldFoundByEstimateUsed() {
    final String vin = "WVWZZZ1KZ5P039566";
    final long custNr = 1100005l;
    Optional<String> estimateId = repository.findByEstimateUsed(vin, custNr);
    Assert.assertThat(estimateId.isPresent(), Matchers.is(true));
    Assert.assertThat(estimateId.get(), Matchers.is("11000051507881674894"));
  }

  @Test
  public void shouldNotFoundByEstimateUsed() {
    final String vin = "WVWZZZ1KZ5P039566";
    final long custNr = 1100002l;
    Optional<String> estimateId = repository.findByEstimateUsed(vin, custNr);
    Assert.assertThat(estimateId.isPresent(), Matchers.is(false));
  }

}
