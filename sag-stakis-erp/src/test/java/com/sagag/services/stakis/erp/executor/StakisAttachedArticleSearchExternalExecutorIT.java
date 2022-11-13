package com.sagag.services.stakis.erp.executor;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.StakisErpApplication;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StakisErpApplication.class)
@EshopIntegrationTest
@CzProfile
@Slf4j
public class StakisAttachedArticleSearchExternalExecutorIT {

  @Autowired
  private StakisAttachedArticleSearchExternalExecutor executor;

  @Test
  public void testExecutor() {
    final AttachedArticleSearchCriteria criteria = DataProvider.buildCriteria();

    final Map<String, ArticleDocDto> result = executor.execute(criteria);

    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));
  }

}
