package com.sagag.services.incentive.points.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.incentive.IncentiveApplication;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IncentiveApplication.class })
@EshopIntegrationTest
public class HappyPointsGetterIT {

  @Autowired
  private HappyPointsGetter getter;

  @Test
  public void testBuildHappyPoints() {
    String customerNr = "23531";
    Long points = getter.get(customerNr);
    Assert.assertThat(points, Matchers.greaterThanOrEqualTo(0L));
  }

  @Test(expected = HttpClientErrorException.class)
  public void testBuildHappyPointsNotFound() {
    String customerNr = "235310";
    Long points = getter.get(customerNr);
    Assert.assertThat(points, Matchers.greaterThanOrEqualTo(0L));
  }
}
