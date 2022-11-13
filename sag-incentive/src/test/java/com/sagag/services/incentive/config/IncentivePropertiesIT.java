package com.sagag.services.incentive.config;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.incentive.IncentiveApplication;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * IT for {@link HappyPointsService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IncentiveApplication.class })
@EshopIntegrationTest
@Slf4j
public class IncentivePropertiesIT {

  @Autowired
  private IncentiveProperties props;

  @Test
  public void verifyIncentiveProperties() {
    log.debug("{}", props.toString());
  }
}
