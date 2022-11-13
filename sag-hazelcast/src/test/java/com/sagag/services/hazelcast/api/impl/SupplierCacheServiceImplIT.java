package com.sagag.services.hazelcast.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.api.SupplierSearchService;
import com.sagag.services.elasticsearch.domain.SupplierTxt;
import com.sagag.services.hazelcast.api.SupplierCacheService;
import com.sagag.services.hazelcast.app.HazelcastApplication;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;

/**
 * Integration test class for suplier cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class SupplierCacheServiceImplIT {

  @Autowired
  private SupplierCacheService supplierService;

  @Autowired
  private SupplierSearchService supplierSearchService;

  /**
   * Initializes the suppliers data into cache.
   */
  @Before
  public void initCache() {
    if (!supplierService.exists()) {
      supplierService.refreshCacheSuppliers(supplierSearchService.getTop10Suppliers());
    }
  }

  @Test
  public void testSearchSupplierByIds() {
    Map<String, SupplierTxt> suppliers = supplierService.searchSupplierByIds(Arrays.asList("14"));
    Assert.assertThat(suppliers.values(),
        Matchers.hasItem(Matchers.hasProperty("suppname", Matchers.is("BOGE"))));
  }

}
