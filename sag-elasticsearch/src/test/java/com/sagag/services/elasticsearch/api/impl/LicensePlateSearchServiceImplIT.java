package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.LicensePlateSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.licenseplate.LicensePlateDoc;

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
public class LicensePlateSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private LicensePlateSearchService service;

  @Test
  public void test() {
    final String text = "TG20824";
    final List<LicensePlateDoc> result = service.searchLicensePlateByText(text);

    Assert.assertThat(result.size(), Matchers.greaterThanOrEqualTo(1));
  }

}
