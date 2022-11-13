package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.UnitreeSearchService;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Integration test class for Elasticsearh service for Unitree.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class UnitreeSearchServiceImplIT {

  @Autowired
  private UnitreeSearchService unitreeSearchService;

  @Test
  public void testGetAllUnitreeCompact() {
    final List<UnitreeDoc> actual = unitreeSearchService.getAllUnitreeCompact();
    Assert.assertNotNull(actual);
    List<UnitreeDoc> unitreeDocs = actual.stream()
        .filter(x -> Objects.equals(x.getId(), "1") || Objects.equals(x.getId(), "2"))
        .collect(Collectors.toList());
    Assert.assertThat(unitreeDocs.size(), Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void tesGetUnitreeByUnitreeId_1() {
    String unitreeId = "1";
    final UnitreeDoc actual = unitreeSearchService.getUnitreeByUnitreeId(unitreeId).orElse(null);
    Assert.assertNotNull(actual);
    Assert.assertThat(actual.getTreeName(), Matchers.notNullValue());
  }

}
