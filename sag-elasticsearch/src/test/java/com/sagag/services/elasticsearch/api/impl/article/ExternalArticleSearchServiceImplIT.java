package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EshopIntegrationTest
@SpringBootTest(classes = { ElasticsearchApplication.class })
public class ExternalArticleSearchServiceImplIT extends AbstractExternalArticleSearchServiceImplIT {

  @Test
  public void testSearchExternalArticles() {
    testAndAssertSearchExtArticles("8426345502672");
  }
}
