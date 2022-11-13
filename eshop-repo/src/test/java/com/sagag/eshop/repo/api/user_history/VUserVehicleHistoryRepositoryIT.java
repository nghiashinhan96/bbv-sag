package com.sagag.eshop.repo.api.user_history;

import com.sagag.eshop.repo.api.userhistory.VUserVehicleHistoryRepository;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.criteria.user_history.UserVehicleHistorySearchCriteria;
import com.sagag.eshop.repo.entity.user_history.VUserVehicleHistory;
import com.sagag.eshop.repo.specification.userhistory.VUserVehicleHistorySpecification;
import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@ChEshopIntegrationTest
@Transactional
@Slf4j
@Ignore("Do it later")
public class VUserVehicleHistoryRepositoryIT {

  @Autowired
  private VUserVehicleHistoryRepository repository;

  @Test
  public void testFindAllSpec() {
    final UserVehicleHistorySearchCriteria criteria = new UserVehicleHistorySearchCriteria();
    criteria.setOrgId(1);
    criteria.setUserId(367556l);
    criteria.setFullName("Verkäufer");
    criteria.setFilterMode("c4s");

    final VUserVehicleHistorySpecification spec = new VUserVehicleHistorySpecification(criteria);
    final Page<VUserVehicleHistory> result = repository.findAll(spec, PageUtils.DEF_PAGE);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(result.getContent()));
    Assert.assertThat(result.hasContent(), Matchers.is(true));
  }

}
