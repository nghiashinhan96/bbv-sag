package com.sagag.services.rest.scheduled;

import com.sagag.eshop.repo.api.BasketHistoryRepository;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.BasketHistoryItemDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import java.util.Date;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Class integration test for {@link CleanBasketHistoryScheduledTask}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class CleanBasketHistoryScheduledTaskIT {

  @Autowired
  private CleanBasketHistoryScheduledTask tasks;

  @Autowired
  private BasketHistoryRepository basketHistoryRepo;

  @Autowired
  private Environment env;

  private Date validDateToDelete;

  @Before
  public void init() {
    validDateToDelete = DateTime.now()
    .minusDays(env.getProperty("days.basket_histories.remove_old_items", Integer.class)).toDate();
  }

  @Test
  public void testClearOldBasketHistories() {
    addDataTest();
    tasks.executeTask();
  }

  @Test
  public void testClearOldBasketHistoriesWithEmptyList() {
    tasks.executeTask();
  }

  private void addDataTest() {
    BasketHistory basketHistory = new BasketHistory();
    basketHistory.setBasketName(StringUtils.EMPTY);
    basketHistory.setOrganisationId(1);
    basketHistory.setCreatedUserId(1L);
    basketHistory.setBasketJson(SagJSONUtil.convertObjectToJson(new BasketHistoryItemDto()));
    basketHistory.setUpdatedDate(validDateToDelete);
    basketHistory.setGrandTotalExcludeVat(NumberUtils.DOUBLE_ONE);
    basketHistoryRepo.save(basketHistory);
  }

}
