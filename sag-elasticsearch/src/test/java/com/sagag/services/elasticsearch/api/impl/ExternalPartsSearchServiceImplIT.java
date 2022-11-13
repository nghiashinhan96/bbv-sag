package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.criteria.ExternalPartsSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import com.sagag.services.elasticsearch.dto.ExternalPartsResponse;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@AutonetEshopIntegrationTest
public class ExternalPartsSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private ExternalPartsSearchServiceImpl service;

  @Override
  @Before
  public void setup() {
    LocaleContextHolder.setLocale(new Locale("ro"));
  }

  @Test
  public void givenCriteriaShouldGetValidResult() {
    String freeText = "A0004208704";
    final ExternalPartsSearchCriteria criteria = new ExternalPartsSearchCriteria(freeText);
    final ExternalPartsResponse response = service.searchByCriteria(criteria);
    Assert.assertNotNull(response.getExternalParts());
  }

  @Test
  public void givenArtIdShouldGetValidResult() {
    String artId = "RE7700274177";
    List<ExternalPartDoc> externalPartDocs = service.searchByArtIds(Arrays.asList(artId));
    Assert.assertThat(externalPartDocs, Matchers.not(Matchers.empty()));
  }
}
