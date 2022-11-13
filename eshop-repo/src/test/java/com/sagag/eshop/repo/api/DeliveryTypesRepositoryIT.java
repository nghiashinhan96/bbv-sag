package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.DeliveryType;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for {@link DeliveryTypesRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class DeliveryTypesRepositoryIT {

  @Autowired
  private DeliveryTypesRepository deliveryTypeTypesRepo;

  @Test
  public void testfindOneByid() {
    final Optional<DeliveryType> deliveryTypes = deliveryTypeTypesRepo.findOneById(2);
    Assert.assertThat(true, Is.is(deliveryTypes.isPresent()));
    Assert.assertThat(2, Is.is(deliveryTypes.get().getId()));
  }

  @Test
  public void testfindOneDesCode() {
    final Optional<DeliveryType> deliveryTypes = deliveryTypeTypesRepo.findOneByDescCode("TOUR");
    Assert.assertThat(true, Is.is(deliveryTypes.isPresent()));
    Assert.assertThat(2, Is.is(deliveryTypes.get().getId()));
  }

}
