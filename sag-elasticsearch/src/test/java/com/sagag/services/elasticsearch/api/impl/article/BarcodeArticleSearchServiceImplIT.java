package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.article.BarcodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.domain.article.ArticlePart;
import com.sagag.services.elasticsearch.enums.ArticlePartType;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
@Slf4j
public class BarcodeArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  private static final PageRequest PAGE_SIZE_2 = PageRequest.of(0, 2);

  @Autowired
  private BarcodeArticleSearchServiceImpl service;

  @Test
  public void testSearchBarcodeEanByCriteria() {

    final String code = "4009026037935";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PAGE_SIZE_2);

    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getNumberOfElements(), Matchers.is(1));

    final ArticleDoc articleDoc = response.getContent().get(0);
    final List<ArticlePart> parts = articleDoc.getParts().stream()
        .filter(part -> ArticlePartType.EAN.name().equals(part.getPtype()))
        .collect(Collectors.toList());
    Assert.assertThat(parts.size(), Matchers.greaterThan(0));

  }

  @Test
  public void testSearchBarcodeIamByCriteria() {
    final String code = "OG215HQ";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PAGE_SIZE_2);

    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getNumberOfElements(), Matchers.is(1));

    final ArticleDoc articleDoc = response.getContent().get(0);
    final List<ArticlePart> parts = articleDoc.getParts().stream()
        .filter(part -> ArticlePartType.IAM.name().equals(part.getPtype()))
        .collect(Collectors.toList());
    Assert.assertThat(parts.size(), Matchers.greaterThan(0));
  }

  @Test
  public void testSearchBarcodeOemByCriteria() {
    final String code = "8E0698451F";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PAGE_SIZE_2);

    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getNumberOfElements(), Matchers.greaterThan(0));

    final ArticleDoc articleDoc = response.getContent().get(0);
    final List<ArticlePart> parts = articleDoc.getParts().stream()
        .filter(part -> ArticlePartType.OEM.name().equals(part.getPtype()))
        .collect(Collectors.toList());
    Assert.assertThat(parts.size(), Matchers.greaterThan(0));
  }

  @Test
  public void testSearchBarcodePccByCriteria() {
    final String code = "LD2749";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PAGE_SIZE_2);

    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getNumberOfElements(), Matchers.is(1));

    final ArticleDoc articleDoc = response.getContent().get(0);
    final List<ArticlePart> parts = articleDoc.getParts().stream()
        .filter(part -> ArticlePartType.PCC.name().equals(part.getPtype()))
        .collect(Collectors.toList());
    Assert.assertThat(parts.size(), Matchers.greaterThan(0));
  }

  @Test
  public void testSearchBarcode_EmptyCode() {

    final String code = "";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    service.search(criteria, PAGE_SIZE_2);
  }

  @Test
  public void testSearchBarcode_EmptyResult() {
    final String code = "1234567890123";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PAGE_SIZE_2);

    Assert.assertThat(response.hasContent(), Matchers.is(false));
    Assert.assertThat(response.getNumberOfElements(), Matchers.is(0));
  }

  @Test
  public void testSearchBarcode_DashedCode() {
    final String code = "CC1T-10655-BB";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PageUtils.DEF_PAGE);

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response.getContent()));

    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getNumberOfElements(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void testSearchBarcode_SpaceCode() {
    final String code = "5Q0 129 620 B";
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> response = service.search(criteria, PageUtils.DEF_PAGE);

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response.getContent()));

    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getNumberOfElements(), Matchers.greaterThanOrEqualTo(1));
  }

}
