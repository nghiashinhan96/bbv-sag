package com.sagag.eshop.repo.api;

import static org.junit.Assert.*;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.services.common.annotation.EshopIntegrationTest;

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
public class DeliveryProfileRepositoryIT {

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepo;

  @Test
  public void findAll_shouldReturnDeliveryProfiles() throws Exception {
    List<DeliveryProfile> profiles = deliveryProfileRepo.findAll();
    assertNotNull(profiles);
  }
}
