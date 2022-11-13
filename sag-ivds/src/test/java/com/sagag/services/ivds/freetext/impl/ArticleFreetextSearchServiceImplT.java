package com.sagag.services.ivds.freetext.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
public class ArticleFreetextSearchServiceImplT {

  @Autowired
  private ArticleFreetextSearchServiceImpl service;

  @Test
  public void testSearchArticlesFreetext() {
    final FreetextSearchRequest request = DataProvider.buildArticleSearchFreetext("audi");
    final FreetextResponseDto response = new FreetextResponseDto();

    boolean isSupported = service.support(request.getSearchOptions());
    Assert.assertThat(isSupported, Matchers.is(true));

    service.search(request, response);

    Assert.assertThat(response.getArticles().hasContent(), Matchers.is(true));
  }
}
