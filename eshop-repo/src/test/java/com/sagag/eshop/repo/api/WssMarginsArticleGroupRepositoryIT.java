package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for {@link CustomerSettingsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class WssMarginsArticleGroupRepositoryIT {

  @Autowired
  private WssMarginsArticleGroupRepository repository;

  @Test
  public void testFindAllByOrgId() {
    log.debug("Starting wssMarginsArticleGroupRepository");
    final List<WssMarginsArticleGroup> marginsArticleGroups =
      repository.findAllByOrgId(RepoDataTests.CUSTOMER_5132364_ORG_ID);
    assertNotNull(marginsArticleGroups);
  }
  
  @Test
  public void testByOrgIdAndArticleGroup() {
    log.debug("Starting wssMarginsArticleGroupRepository");
    String sagArtGroup = "1-1";
    final Optional<WssMarginsArticleGroup> marginsArticleGroupOpt =
      repository.findWssMarginArticleGroupByArtGroup(RepoDataTests.CUSTOMER_5132364_ORG_ID, sagArtGroup);
    assertTrue(marginsArticleGroupOpt.isPresent());
    WssMarginsArticleGroup wssMarginsArticleGroup = marginsArticleGroupOpt.get();
    assertEquals(wssMarginsArticleGroup.getSagArtGroup(), sagArtGroup);
  }
}
