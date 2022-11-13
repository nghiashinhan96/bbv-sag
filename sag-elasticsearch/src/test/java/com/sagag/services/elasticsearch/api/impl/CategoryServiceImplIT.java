package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.CategoryService;
import com.sagag.services.elasticsearch.domain.CategoryDoc;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Integration tests for category elasticsearch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class CategoryServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private CategoryService categoryService;

  @Test
  public void testGetAllCategories() {
    final List<CategoryDoc> categoryDocs = categoryService.getAllCategories(PageUtils.DEF_PAGE)
        .getContent();
    Assert.assertThat(10, Matchers.is(categoryDocs.size()));
  }

  @Test
  public void testGetCategoriesByLeaftxtWithPartialMatchingHandlerWithOneWrongCharacter() {
    final List<CategoryDoc> categoryDocs =
        categoryService.getCategoriesByLeaftxt("Kri");
    Assert.assertEquals(0, categoryDocs.size());
  }

  @Test
  public void testGetCategoriesByLeaftxtWhenMisspellingThreeCharacter() {
    final List<CategoryDoc> categoryDocs =
        categoryService.getCategoriesByLeaftxt("Kraftstaffversargang");
    Assert.assertEquals(0, categoryDocs.size());
  }

  @Test
  public void testGetCategoriesByLeaftxtWithSearchTextHasMoreThanOneWord() {
    final List<CategoryDoc> categoryDocs =
        categoryService.getCategoriesByLeaftxt("sika kleber set");
    Assert.assertEquals(0, categoryDocs.size());
  }

}
