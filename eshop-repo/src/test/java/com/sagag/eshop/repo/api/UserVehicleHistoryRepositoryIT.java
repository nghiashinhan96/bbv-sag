package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.UserVehicleHistory;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class UserVehicleHistoryRepositoryIT {

  @Autowired
  private UserVehicleHistoryRepository repository;

  @Test
  public void test() {
    final long userid = 5l;
    final int vehHistoryId = 10001;
    final String searchTerm = null;
    final UserHistoryFromSource fromSource = UserHistoryFromSource.C4C;
    final List<UserVehicleHistory> entities =
        repository.findExistingUserVehicleHistory(userid, vehHistoryId, searchTerm, fromSource);

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(entities));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(entities.stream().findFirst().get()));
  }

}
