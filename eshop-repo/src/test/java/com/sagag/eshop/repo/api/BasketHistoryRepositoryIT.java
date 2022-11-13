package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link BasketHistoryRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class BasketHistoryRepositoryIT {

  @Autowired
  private BasketHistoryRepository basketHistoryRepo;

  @Test
  public void testFindByUpdatedDateBefore() {
    List<BasketHistory> histories = basketHistoryRepo.findByUpdatedDateBefore(DateTime.now().toDate());
    Assert.assertThat(histories.size(), Matchers.greaterThanOrEqualTo(0));
  }

}
