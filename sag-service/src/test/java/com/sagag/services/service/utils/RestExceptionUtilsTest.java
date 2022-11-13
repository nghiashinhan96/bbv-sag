package com.sagag.services.service.utils;

import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.article.ArticleDocDto;

import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UT for {@link RestExceptionUtils}.
 */
public class RestExceptionUtilsTest {

  @Test(expected = ResultNotFoundException.class)
  public void testDoSafelyReturnNotEmptyRecords_NullPage() throws ResultNotFoundException {
    final Page<ArticleDocDto> articles = null;
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
  }

  @Test(expected = ResultNotFoundException.class)
  public void testDoSafelyReturnNotEmptyRecords_EmptyPage() throws ResultNotFoundException {
    final Page<ArticleDocDto> articles = Page.empty();
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
  }

  @Test
  public void testDoSafelyReturnNotEmptyRecords_OneItemInPage() throws ResultNotFoundException {
    final Page<ArticleDocDto> articles = new PageImpl<>(Lists.newArrayList(new ArticleDocDto()));
    Page<ArticleDocDto> actual = RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
    Assert.assertThat(actual.getNumberOfElements(), Matchers.is(1));
  }

  @Test(expected = ResultNotFoundException.class)
  public void testDoSafelyReturnNotEmptyRecords_NullList() throws ResultNotFoundException {
    final List<ArticleDocDto> articles = null;
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
  }

  @Test(expected = ResultNotFoundException.class)
  public void testDoSafelyReturnNotEmptyRecords_EmptyList() throws ResultNotFoundException {
    final List<ArticleDocDto> articles = Collections.emptyList();
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
  }

  @Test
  public void testDoSafelyReturnNotEmptyRecords_OneItemInList() throws ResultNotFoundException {
    final List<ArticleDocDto> articles = Lists.newArrayList(new ArticleDocDto());
    List<ArticleDocDto> actual = RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
    Assert.assertThat(actual.size(), Matchers.is(1));
  }

  @Test(expected = ResultNotFoundException.class)
  public void testDoSafelyReturnNotEmptyRecords_NullMap() throws ResultNotFoundException {
    final Map<String, ArticleDocDto> articles = null;
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles, StringUtils.EMPTY);
  }

  @Test(expected = ResultNotFoundException.class)
  public void testDoSafelyReturnNotEmptyRecords_EmptyMap() throws ResultNotFoundException {
    final Map<String, ArticleDocDto> articles = Collections.emptyMap();
    RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles, StringUtils.EMPTY);
  }

  @Test
  public void testDoSafelyReturnNotEmptyRecords_OneItemInMap() throws ResultNotFoundException {
    final String idSagSys = "1000001";

    final ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys(idSagSys);
    final List<ArticleDocDto> articles = Lists.newArrayList(article);
    final Map<String, ArticleDocDto> articleMap = articles.stream()
        .collect(Collectors.toMap(ArticleDocDto::getIdSagsys, item -> article));

    Map<String, ArticleDocDto> actual = RestExceptionUtils.doSafelyReturnNotEmptyRecords(articleMap, "");
    Assert.assertThat(actual.size(), Matchers.is(1));
    Assert.assertThat(actual.get(idSagSys), Matchers.notNullValue());
  }
}
