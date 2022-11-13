package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.SupplierSearchService;
import com.sagag.services.elasticsearch.domain.SupplierDoc;
import java.util.List;
import org.hamcrest.Matchers;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test class for Elasticsearch Supplier service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class SupplierSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private SupplierSearchService supplierService;

  @Test
  public void shouldGetTop10Suppliers() {
    List<SupplierDoc> allSuppliers = supplierService.getTop10Suppliers();
    assertThat(allSuppliers.size(), Matchers.is(10));
  }
}
