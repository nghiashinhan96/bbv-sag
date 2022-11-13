package com.sagag.services.stakis.erp.executor;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class StakisAttachedArticleSearchExternalExecutorTest {

  @InjectMocks
  private StakisAttachedArticleSearchExternalExecutor executor;

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllArgExceptionWithNullCriteria() {
    executor.execute(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllArgExceptionWithEmptyList() {
    final AttachedArticleSearchCriteria criteria = new AttachedArticleSearchCriteria();
    criteria.setAttachedArticleRequestList(Collections.emptyList());
    executor.execute(criteria);
  }

  @Test
  public void shouldReturnSuccessfulWithValidCriteria() {
    final AttachedArticleSearchCriteria criteria = DataProvider.buildCriteria();
    final Map<String, ArticleDocDto> result = executor.execute(criteria);
    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));
  }
}
