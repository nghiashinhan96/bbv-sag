package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.ProdAllDataService;
import com.sagag.services.elasticsearch.domain.alldata.ProdAllDataDoc;

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
public class ProdAllDataServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private ProdAllDataService service;

  @Test
  public void test() {
    final String text = "8426345502672";
    final List<ProdAllDataDoc> result = service.searchAllDataByText(text);
    Assert.assertThat(result.isEmpty(), Matchers.is(false));
  }

}
