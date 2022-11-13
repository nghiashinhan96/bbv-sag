package com.sagag.services.rest.scheduled;

import com.sagag.eshop.repo.api.BasketHistoryRepository;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.services.common.profiles.DisableForProfile;
import com.sagag.services.common.schedule.ScheduledTask;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Class for execute some schedule task for basket histories.
 *
 */
@Component
@DisableForProfile({"country-cz", "country-sb", "country-autonet",
    "country-cz-ax", "country-ax-cz"})
@Slf4j
public class CleanBasketHistoryScheduledTask implements ScheduledTask {

  @Autowired
  private Environment env;

  @Autowired
  private BasketHistoryRepository basketHistoryRepo;

  @Override
  @Transactional
  @Scheduled(cron = "${cron.basket_histories.remove_old_items}")
  public void executeTask() {
    log.debug("START -> Clear old basket histories");

    Integer daysOfBeforeCurrentDate =
      env.getProperty("days.basket_histories.remove_old_items", Integer.class);
    Assert.notNull(daysOfBeforeCurrentDate, "The days of before current date is not valid");

    Integer maxRecordExecuteEachTime =
      env.getProperty("max_result.basket_histories.remove_old_items", Integer.class);
    Assert.notNull(maxRecordExecuteEachTime, "The max result to execute is not valid");

    // Get all basket histories which older than xx days
    log.debug("Get all basket histories which older than {} days", daysOfBeforeCurrentDate);
    DateTime beforeXDays = DateTime.now().minusDays(daysOfBeforeCurrentDate);
    List<BasketHistory> basketHistories =
      basketHistoryRepo.findByUpdatedDateBefore(beforeXDays.toDate());

    // If the deleted basket histories is deleted is empty, exit now
    if (CollectionUtils.isEmpty(basketHistories)) {
      log.debug("END -> The list of old basket histories is empty, exit now");
      return;
    }

    log.debug("The size of old basket histories = {}", basketHistories.size());

    // Delete all basket histories with number of records each bundles is set in configuration file
    final List<List<BasketHistory>> basketHistoriesPartitions =
      ListUtils.partition(basketHistories, maxRecordExecuteEachTime);
    basketHistoriesPartitions.stream()
      .peek(entities -> log.debug("Delete the size of basket histories = {}", entities.size()))
      .forEach(basketHistoryRepo::deleteInBatch);

    log.debug("END -> Clear old basket histories");
  }

}
