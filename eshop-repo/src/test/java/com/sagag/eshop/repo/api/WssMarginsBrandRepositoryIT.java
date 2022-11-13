package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.WssMarginsBrand;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link CustomerSettingsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class WssMarginsBrandRepositoryIT {

  @Autowired
  private WssMarginsBrandRepository wssMarginsBrandRepository;

  @Test
  public void testFindAllByOrgId() {
    log.debug("starting wssMarginsArticleGroupRepository");
    final List<WssMarginsBrand> marginsBrands =
        wssMarginsBrandRepository.findAllByOrgId(RepoDataTests.CUSTOMER_5132364_ORG_ID);
    assertNotNull(marginsBrands);
  }
}
