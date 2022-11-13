package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.WssTourTimes;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for {@link WssTourTimesRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class WssTourTimeRepositoryIT {

  @Autowired
  private WssTourTimesRepository repository;

  @Test
  public void testFindAll() {
    final Page<WssTourTimes> result = repository.findAll(PageUtils.DEF_PAGE);

    Assert.assertThat(result.getTotalElements(),
      Matchers.greaterThanOrEqualTo(NumberUtils.LONG_ZERO));
  }

}
