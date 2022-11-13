package com.sagag.services.service.category.converter.impl;

import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.ivds.api.IvdsOilSearchService;
import com.sagag.services.service.category.CategoryItemCriteria;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
public class DefaultCategoryConverterTest {

  @InjectMocks
  private DefaultCategoryConverterV2 categoryConverter;

  @Mock
  private IvdsOilSearchService ivdsOilSerchService;

  @Test
  public void shouldConvertEmptyCategoryDocItem() {
    final CategoryDoc doc = new CategoryDoc();
    doc.setGenarts(Collections.emptyList());
    categoryConverter.convert(doc, CategoryItemCriteria.builder().build());
  }

}
