package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPriorityDoc;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class BrandPriorityServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private BrandPriorityServiceImpl service;

  @Test
  public void testGetAll() {
    final List<BrandPriorityDoc> docs = service.getAll();
    log.debug(SagJSONUtil.convertObjectToJson(docs));
    Assert.assertThat(docs.isEmpty(), Matchers.is(false));
  }
}
