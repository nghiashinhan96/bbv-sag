package com.sagag.services.dvse.scheduled;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.schedule.ScheduledTask;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Actives the DVSE(MDM) users is generated from Connect.
 * </p>
 *
 *
 *
 */
@Component
@Slf4j
@PropertySource(value = "classpath:schedule.properties")
public class ActiveDvseUserScheduledTask implements ScheduledTask {

  private static final Long MDM_USER_SYNCHRONIZED_MILISECONDS =
      SagConstants.DELAY_MINUTE_TO_SEND_ACTIVE_USER_MAIL * 60 * 1000L;

  private static final String DEBUG_MSG =
    "External User: user_id = {} - username = {} - password = {}";

  @Autowired
  private Environment env;

  @Autowired
  private ExternalUserService externalUserService;

  @Override
  @Scheduled(cron = "${cron.mdm.sync_user}")
  @Transactional
  public void executeTask() {
    log.debug("Activating DVSE Users");
    int maxResult = env.getProperty("max_result.mdm.sync_user", Integer.class);
    final Page<ExternalUserDto> users = externalUserService.searchInactiveExternalUser(
      ExternalApp.DVSE, PageUtils.defaultPageable(maxResult));
    if (!users.hasContent()) {
      log.debug("Not found any users to active");
      return;
    }

    final List<ExternalUserDto> updatedUsers = users.stream()
      .peek(peekConsumer())
      .filter(isActiveExtUser())
      .map(activeExtUserAdapter())
      .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(updatedUsers)) {
      log.debug("Not found any users are valid to active");
      return;
    }
    externalUserService.updateExternalUsers(updatedUsers);
  }

  private static Consumer<ExternalUserDto> peekConsumer() {
    return user -> log.debug(DEBUG_MSG, user.getEshopUserId(), user.getUsername(),
      user.getPassword());
  }

  private static Predicate<ExternalUserDto> isActiveExtUser() {
    return user -> {
      DateTime createdDate = new DateTime(user.getCreatedDate().getTime());
      long distinct = DateTime.now().getMillis() - createdDate.getMillis();
      return NumberUtils.compare(distinct, MDM_USER_SYNCHRONIZED_MILISECONDS) >= 0;
    };
  }

  private static Function<ExternalUserDto, ExternalUserDto> activeExtUserAdapter() {
    return user -> {
      user.setActive(true);
      return user;
    };
  }

}
