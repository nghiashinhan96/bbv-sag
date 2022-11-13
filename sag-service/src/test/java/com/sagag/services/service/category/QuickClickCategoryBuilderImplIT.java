package com.sagag.services.service.category;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.service.category.impl.QuickClickCategoryBuilderImpl;

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
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
public class QuickClickCategoryBuilderImplIT extends AbstractCategoryBuilderImplIT {

  @Autowired
  private QuickClickCategoryBuilderImpl builder;

  @Test
  public void testBuildQuickClickCategories() {
    final List<List<CategoryItem>> categories =
        builder.buildCategories(categoriesOfVehicle, allCategories, criteria);
    log.debug("Response categories = {}", SagJSONUtil.convertObjectToPrettyJson(categories));
    Assert.assertThat(categories, Matchers.notNullValue());
  }

}
