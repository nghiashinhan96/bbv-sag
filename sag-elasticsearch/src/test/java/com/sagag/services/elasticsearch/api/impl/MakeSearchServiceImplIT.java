package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for Make ES service implementation.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class MakeSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private MakeSearchServiceImpl makeSearchService;

  @Test
  public void shouldGetTop10Makes() {
    assertThat(10, is(makeSearchService.getTop10Makes().size()));
  }
  
}
