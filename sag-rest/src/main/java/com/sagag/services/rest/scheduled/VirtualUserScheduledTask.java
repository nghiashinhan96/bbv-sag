package com.sagag.services.rest.scheduled;

import com.sagag.eshop.repo.api.VVirtualUserRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;
import com.sagag.services.common.schedule.ScheduledTask;
import com.sagag.services.common.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.IntStream;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
@Slf4j
public class VirtualUserScheduledTask implements ScheduledTask {

  private static final int MAX_ESHOP_USER_PAG_SIZE = 2000;

  @Autowired
  private VirtualUserHandler virtualUserHandler;

  @Autowired
  private VVirtualUserRepository vVirtualUserRepo;

  @Override
  @Scheduled(cron = "${cron.user.virtual.remove.schedule}")
  @SchedulerLock(name = "virtualUserScheduledTask")
  @Transactional
  public void executeTask() {
    log.info("Removing virtual users");
    LockAssert.assertLocked();
    final long total = vVirtualUserRepo.count();
    final int size = MAX_ESHOP_USER_PAG_SIZE;
    final int totalPages = new PageImpl<>(Collections.emptyList(),
      PageUtils.defaultPageable(size), total).getTotalPages();

    IntStream.range(0, totalPages).boxed()
      .forEach(pageNr -> {
        final Page<VVirtualUser> virtualUsersPage = vVirtualUserRepo.findVirtualUsers(
          PageUtils.defaultPageable(pageNr, size));
        if (virtualUsersPage.hasContent()) {
          virtualUserHandler.accept(virtualUsersPage.getContent());
        }
      });
    log.info("Page of eshop user info:\n - totalElements = {}\n - totalPages = {}",
      total, totalPages);

    log.info("End remove virtual users");
  }
}
