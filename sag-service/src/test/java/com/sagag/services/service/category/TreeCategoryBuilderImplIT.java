package com.sagag.services.service.category;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.enums.LinkPartnerEnum;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.service.category.impl.TreeCategoryBuilderImpl;
import com.sagag.services.service.enums.CategoryType;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
public class TreeCategoryBuilderImplIT extends AbstractCategoryBuilderImplIT {

  @Autowired
  private TreeCategoryBuilderImpl builder;

  @Test
  public void testBuildAllCategories() {
    final Map<String, String> externalUrls = new HashMap<>();
    externalUrls.put(LinkPartnerEnum.THULE.name().toLowerCase(), "https://thule.com");
    final Map<CategoryType, List<CategoryItem>> categories =
        builder.buildCategories(categoriesOfVehicle, allCategories, criteria);
    log.debug("Response categories = {}", SagJSONUtil.convertObjectToPrettyJson(categories));
    Assert.assertThat(categories, Matchers.notNullValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildAllCategories_NullAffiliate() {
    criteria.setAff(null);
    final Map<CategoryType, List<CategoryItem>> categories =
        builder.buildCategories(categoriesOfVehicle, allCategories, criteria);
    log.debug("Response categories = {}", SagJSONUtil.convertObjectToPrettyJson(categories));
    Assert.assertThat(categories, Matchers.notNullValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildAllCategories_EmptyCategoriesOfVehicle() {
    final Map<CategoryType, List<CategoryItem>> categories =
        builder.buildCategories(Collections.emptyList(), allCategories, criteria);
    log.debug("Response categories = {}", SagJSONUtil.convertObjectToPrettyJson(categories));
    Assert.assertThat(categories, Matchers.notNullValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildAllCategories_EmptyAllCategories() {
    final Map<CategoryType, List<CategoryItem>> categories =
        builder.buildCategories(categoriesOfVehicle, Collections.emptyMap(), criteria);
    log.debug("Response categories = {}", SagJSONUtil.convertObjectToPrettyJson(categories));
    Assert.assertThat(categories, Matchers.notNullValue());
  }
}
