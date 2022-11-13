package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

@RunWith(SpringRunner.class)
@AutonetEshopIntegrationTest
@SpringBootTest(classes = { ElasticsearchApplication.class })
@Ignore("Need Simon index the data or change the endpoint in the ES configuration")
public class AutonetExternalArticleSearchServiceImplIT
  extends AbstractExternalArticleSearchServiceImplIT {

  @Test
  public void testSearchAutonetExternalArticles() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    testAndAssertSearchExtArticles("U4626644");
  }

  @Test
  public void testFilterAutonetExternalArticles() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    testAndAssertFilterExtArticles("U4626644");
  }

  @Test
  public void testFilterAutonetExternalArticles_OC90() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    testAndAssertFilterExtArticles("oc90");
  }

}
