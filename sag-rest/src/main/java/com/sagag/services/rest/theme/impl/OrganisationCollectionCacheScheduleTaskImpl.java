package com.sagag.services.rest.theme.impl;

import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.services.common.cache.CacheConstants;
import com.sagag.services.common.schedule.ScheduledTask;
import com.sagag.services.common.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class OrganisationCollectionCacheScheduleTaskImpl implements ScheduledTask {

  private static final long FIXED_RATE = CacheConstants.TTL_4_HOURS * 1000l;

  @Autowired
  private OrganisationCollectionRepository orgCollectionRepo;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Override
  @Scheduled(fixedRate = FIXED_RATE)
  public void executeTask() {
    log.info("Processing caching collection cache settings map");
    Pageable pageable = PageUtils.DEF_PAGE;
    final List<String> orgCollectionShortNames = new ArrayList<>();
    Page<OrganisationCollection> orgCollectionPage = orgCollectionRepo.findAll(pageable);
    orgCollectionShortNames.addAll(orgCollectionPage.map(OrganisationCollection::getShortname)
        .getContent());
    while (orgCollectionPage.hasNext()) {
      pageable = pageable.next();
      orgCollectionPage = orgCollectionRepo.findAll(pageable);
      orgCollectionShortNames.addAll(orgCollectionPage.map(OrganisationCollection::getShortname)
          .getContent());
    }
    log.info("The all organistion collection in system = {}", orgCollectionShortNames);
    if (CollectionUtils.isEmpty(orgCollectionShortNames)) {
      return;
    }

    for (String collectionShortname : orgCollectionShortNames) {
      log.info("Caching organisation collection setting of short name = {}", collectionShortname);
      orgCollectionService.findSettingsByCollectionShortname(collectionShortname);
    }
  }

}
