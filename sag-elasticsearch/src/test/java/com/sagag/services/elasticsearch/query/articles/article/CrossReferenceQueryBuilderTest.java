package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.criteria.article.CrossReferenceArticleSearchCriteria;
import com.sagag.services.elasticsearch.parser.FreetextStringParser;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilderTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

@RunWith(MockitoJUnitRunner.class)
public class CrossReferenceQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private CrossReferenceQueryBuilder queryBuilder;
  @Spy
  private FreetextStringParser freetextStringParser;

  @Test
  public void shouldBuildQuerySuccessfully() {
    // Given
    final CrossReferenceArticleSearchCriteria criteria = new CrossReferenceArticleSearchCriteria();
    criteria.setArtNr("oc90");
    criteria.setBrandId("62");
    final Pageable pageable = PageUtils.DEF_PAGE;
    // When
    SearchQuery query = queryBuilder.buildQuery(criteria, pageable, INDEX);
    // Then
    Assert.assertNotNull(query);
  }
}