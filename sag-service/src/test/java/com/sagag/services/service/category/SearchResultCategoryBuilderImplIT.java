package com.sagag.services.service.category;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.service.category.impl.SearchResultCategoryBuilderImpl;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
public class SearchResultCategoryBuilderImplIT extends AbstractCategoryBuilderImplIT {

  @Autowired
  private SearchResultCategoryBuilderImpl builder;

  @Test
  public void testBuildSearchedCategories() {
    final Page<CategoryDoc> searchedCates =
        cateCacheService.searchCategoriesByLeafText("Elektronik");
    final Map<String, String> filteredCates =
        builder.buildCategories(categoriesOfVehicle, allCategories, searchedCates, criteria);
    log.debug("Response categories = {}", SagJSONUtil.convertObjectToPrettyJson(filteredCates));
    Assert.assertThat(filteredCates, Matchers.notNullValue());
  }

}
