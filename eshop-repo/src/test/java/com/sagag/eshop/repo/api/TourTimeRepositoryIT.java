package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.TourTime;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link TourTimeRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class TourTimeRepositoryIT {

  @Autowired
  private TourTimeRepository tourTimeRepo;

  @Test
  public void testFindByCustomerNumber() {
    final List<TourTime> tourTime = tourTimeRepo.findByCustomerNumber("1100043");
    Assert.assertFalse(tourTime.isEmpty());
  }

}
