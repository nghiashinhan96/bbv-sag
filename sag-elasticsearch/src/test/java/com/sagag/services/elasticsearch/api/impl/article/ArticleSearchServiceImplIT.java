package com.sagag.services.elasticsearch.api.impl.article;

import static org.hamcrest.core.Is.is;

import com.google.common.collect.Lists;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchTerm;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.domain.article.ArticlePart;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test class for ES Article Search Service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class ArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  private static final int BRAND_5010 = 5010;
  private static final int GEN_ART_1191 = 1191;
  private static final PageRequest PAGE_SIZE_100 = PageRequest.of(0, 100);
  private static final PageRequest PAGE_SIZE_2 = PageRequest.of(0, 2);

  @Autowired
  private ArticleSearchService articleSearchService;

  @Test
  public void testSearchArticlesByNumberWithEmpty() {
    final String articleNr = StringUtils.EMPTY;
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertNotNull(articles);
    Assert.assertThat(0, is(articles.getNumberOfElements()));
  }

  @Test
  public void testSearchArticlesByNumber() {
    final String articleNr = "5001100420";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .allMatch(p -> p.equals(articleNr));
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByRefNumber() {
    final String articleNr = "1935393R";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);

    Assert.assertThat(articles.getTotalElements(), Matchers.greaterThanOrEqualTo(0l));
  }

  @Test
  public void testSearchArticlesByNumberPnrn() {
    final String articleNr = "7700720978";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    boolean anyMatch = articles.getContent().parallelStream().flatMap(f -> f.getParts().stream())
        .map(m -> m.getPnrn()).anyMatch(p -> p.equals(articleNr));
    Assert.assertTrue(anyMatch);
  }

  @Test
  public void testSearchArticlesByNumberPnrn2() {
    final String articleNr = "5952779";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    boolean anyMatch = articles.getContent().parallelStream().flatMap(f -> f.getParts().stream())
        .map(m -> m.getPnrn()).anyMatch(p -> p.equals(articleNr));
    Assert.assertTrue(anyMatch);
  }

  @Test
  public void testSearchArticlesByNumberNotFound() {
    final String articleNr = "5112NF";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), is(false));
  }

  @Test
  public void testSearchArticlesByNumberNotFound2() {
    final String articleNr = "NF2";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), Matchers.anyOf(is(true), is(false)));
  }

  @Test
  public void testSearchArticlesByNumberNotFound4() {
    final String articleNr = "NF4";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), is(false));
  }

  @Test
  public void testSearchArticlesByNumberHavingDot() {
    final String articleNr = "10.6026";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), Matchers.anyOf(is(true), is(false)));
  }

  @Test
  public void testSearchArticlesByNumberHavingParts() {
    final String articleNr = "6401576";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_100);

    Assert.assertThat(articles.hasContent(), Matchers.equalTo(true));

    final ArticleDoc articleFound = articles.getContent().get(0);
    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .anyMatch(p -> p.equals(articleNr));

    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertThat(articleFound.getParts().size(), Matchers.greaterThanOrEqualTo(2));
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByNumberWithNull() {
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(StringUtils.EMPTY, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertNotNull(articles);
    Assert.assertThat(0, is(articles.getNumberOfElements()));
  }

  @Test
  public void testSearchArticlesByHavingCharacter() {
    final String articleNr = "P61007";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void testSearchArticlesByHavingCharacter2() {
    final String articleNr = "562229JC";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void testSearchArticlesByHavingCharacter3() {
    final String articleNr = "1LL010820861";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(1, is(articles.getNumberOfElements()));
  }

  @Test
  public void testSearchArticlesByIdSagSysWithEmpty() {
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(StringUtils.EMPTY, PAGE_SIZE_2, isSaleOnBehalf);
    Assert.assertNotNull(articles);
    Assert.assertThat(0, is(articles.getNumberOfElements()));
  }

  @Test
  public void testSearchArticlesByIdSagSys() {
    final String idSagsys = "1000646933";
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(idSagsys, PAGE_SIZE_2, isSaleOnBehalf);

    boolean allMatch =
        articles.getContent().parallelStream().allMatch(p -> p.getIdSagsys().equals(idSagsys));
    if (!articles.hasContent()) {
      return;
    }

    final ArticleDoc articleFound = articles.getContent().get(0);
    Assert.assertThat(1, is(articles.getNumberOfElements()));
    Assert.assertEquals(idSagsys, articleFound.getIdSagsys());
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSsearchArticlesByIdSagSyses() {
    List<String> idSagsyses = Arrays.asList("1001037670", "1001250836", "1000542844", "1000740841",
        "1001261483", "1001260990");
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSyses(idSagsyses,
          PageUtils.defaultPageable(idSagsyses.size()), isSaleOnBehalf);

    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThan(0));
    Assert.assertTrue(articles.getContent().parallelStream().map(m -> m.getIdSagsys())
        .collect(Collectors.toSet()).stream().anyMatch(idSagsyses::contains));
  }

  @Test
  public void testSearchArticlesByFreeTextWithCndech() {
    final String freeText = "Stückzahl";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_2);

    Set<String> cndechSet =
        articles.getContent().parallelStream().flatMap(m -> m.getCriteria().parallelStream())
            .filter(criteria -> Objects.nonNull(criteria.getCndech())
                && criteria.getCndech().contains(freeText))
            .map(m -> m.getCndech()).collect(Collectors.toSet());

    Assert.assertThat(cndechSet.size(), Matchers.greaterThan(0));
    Assert.assertTrue(cndechSet.parallelStream().allMatch(p -> p.contains(freeText)));

  }

  @Test
  public void testSearchArticlesByFreeTextWithIdSagsys() {
    final String freeText = "1000646933";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_2);

    Set<String> collect = articles.getContent().parallelStream()
        .filter(p -> Objects.nonNull(p.getIdSagsys()) && p.getIdSagsys().contains(freeText))
        .map(m -> m.getIdSagsys()).collect(Collectors.toSet());

    Assert.assertThat(freeText, collect.size(), Matchers.greaterThanOrEqualTo(0));
    Assert.assertTrue(collect.parallelStream().allMatch(p -> p.contains(freeText)));
  }

  @Test
  public void testSearchArticlesByFreeTextWithSupplier() {
    final String freeText = "ATE";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_100);
    Set<String> collect = articles.getContent().parallelStream()
        .filter(p -> Objects.nonNull(p.getSupplier()) && p.getSupplier().contains(freeText))
        .map(m -> m.getSupplier()).collect(Collectors.toSet());

    Assert.assertThat(freeText, collect.size(), Matchers.greaterThan(0));
    Assert.assertThat(collect.size(), Matchers.is(1));
    Assert.assertTrue(collect.parallelStream().allMatch(p -> p.contains(freeText)));
  }

  @Test
  public void testSearchArticlesByFreeTextWithCvp() {
    final String freeText = "39";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_100);
    Set<String> collect =
        articles.getContent().parallelStream().flatMap(m -> m.getCriteria().parallelStream())
            .filter(p -> Objects.nonNull(p.getCvp()) && p.getCvp().contains(freeText))
            .map(m -> m.getCvp()).collect(Collectors.toSet());

    Assert.assertThat(collect.size(), Matchers.greaterThan(0));
    Assert.assertThat(collect.size(), Matchers.greaterThanOrEqualTo(1));
    Assert.assertTrue(collect.parallelStream().allMatch(p -> p.contains(freeText)));
  }

  @Test
  public void testSearchArticlesByFreeTextWithInfoTxtDe() {
    final String freeText = "DE-H3-Nebelscheinwerfer";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_2);

    Set<String> collect =
        articles.getContent().parallelStream().flatMap(m -> m.getInfos().parallelStream())
            .filter(p -> Objects.nonNull(p.getInfoTxtDe()) && p.getInfoTxtDe().contains(freeText))
            .map(m -> m.getInfoTxtDe()).collect(Collectors.toSet());

    Assert.assertThat(freeText, collect.size(), Matchers.greaterThan(0));
    Assert.assertTrue(collect.parallelStream().allMatch(p -> p.contains(freeText)));
  }

  @Test
  @Ignore("Test data is not existing in new Elasticsearch version")
  public void testSearchArticlesByFreeTextWithMultiFieldMatchCndech() {
    final String freeText = "Einbauseite DE-H3-Nebelscheinwerfer ATE 69,5";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_2);
    Set<String> cndechSet =
        articles.getContent().parallelStream().flatMap(m -> m.getCriteria().parallelStream())
            .filter(criteria -> Objects.nonNull(criteria.getCndech())
                && criteria.getCndech().contains("Einbauseite"))
            .map(m -> m.getCndech()).collect(Collectors.toSet());

    Assert.assertThat(freeText, cndechSet.size(), Matchers.greaterThan(0));
  }

  @Test
  @Ignore("Test data is not existing in new Elasticsearch version")
  public void testSearchArticlesByFreeTextWithMultiFieldMatchInfoTxtDe() {
    final String freeText = "DE-H3-Nebelscheinwerfer Einbauseite ATE 69,5";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_2);

    Set<String> collect =
        articles.getContent().parallelStream().flatMap(m -> m.getInfos().parallelStream())
            .filter(p -> Objects.nonNull(p.getInfoTxtDe())
                && StringUtils.indexOf(p.getInfoTxtDe(), "DE-H3") >= 0)
            .map(m -> m.getInfoTxtDe()).collect(Collectors.toSet());

    Assert.assertThat(freeText, collect.size(), Matchers.greaterThan(0));
  }

  @Test
  @Ignore("Test data is not existing in new Elasticsearch version")
  public void testSearchArticlesByFreeTextWithMultiFieldMatchSupplier() {
    final String freeText = "ATE DE-H3-Nebelscheinwerfer Einbauseite 69,5";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_100);

    Set<String> suppliers = articles.getContent().parallelStream()
        .filter(p -> Objects.nonNull(p.getSupplier())
          && StringUtils.equalsIgnoreCase("HELLA", p.getSupplier()))
        .map(m -> m.getSupplier()).collect(Collectors.toSet());

    Assert.assertThat(freeText, suppliers.size(), Matchers.greaterThan(0));
    Assert.assertThat(suppliers.size(), Matchers.is(1));
  }

  @Test
  public void testSearchArticlesByIdSagSysNotFound() throws IOException {
    final String idSagsys = "10000001";
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(idSagsys, PAGE_SIZE_2, isSaleOnBehalf);
    Assert.assertThat(articles.getNumberOfElements(), is(0));
  }

  @Test
  @Ignore("Data is not ready")
  public void testfindAll() {
    List<String> ids = Arrays.asList("1000646933", "1000303119");
    final boolean isSaleOnBehalf = false;
    List<ArticleDoc> articleDoc = articleSearchService.searchArticlesByIdSagSyses(ids, isSaleOnBehalf);
    List<String> actualIds = articleDoc.stream().map(m -> m.getId()).collect(Collectors.toList());
    List<String> collect =
        articleDoc.parallelStream().map(m -> m.getArtid()).collect(Collectors.toList());

    Assert.assertNotNull(articleDoc);
    Assert.assertTrue(CollectionUtils.isEqualCollection(actualIds, ids));
    Assert.assertTrue(CollectionUtils.isEqualCollection(collect, ids));
  }

  @Test
  public void testfindAllWithEmpty() {
    final boolean isSaleOnBehalf = false;
    List<ArticleDoc> articleDoc = articleSearchService.searchArticlesByIdSagSyses(Collections.emptyList(), isSaleOnBehalf);

    Assert.assertNotNull(articleDoc);
    Assert.assertEquals(0, articleDoc.size());
  }

  @Test
  public void testfindAllWithNull() {
    final boolean isSaleOnBehalf = false;
    List<ArticleDoc> articleDoc = articleSearchService.searchArticlesByIdSagSyses(null, isSaleOnBehalf);

    Assert.assertNotNull(articleDoc);
    Assert.assertEquals(0, articleDoc.size());
  }

  @Test
  public void testSearchArticlesByPartRefs() {
    final List<String> prnrs = Arrays.asList("5Q0698151C", "2078", "90512909", "2002112");
    final boolean isSaleOnBehalf = false;
    final List<ArticleDoc> articles = articleSearchService.searchArticlesByPartRefs(prnrs, isSaleOnBehalf);
    Set<String> prnrsActual = articles.parallelStream().flatMap(m -> m.getParts().parallelStream())
        .filter(p -> prnrs.contains(p.getPnrn())).map(m -> m.getPnrn()).collect(Collectors.toSet());
    Set<String> artIds =
        articles.parallelStream().map(m -> m.getArtid()).collect(Collectors.toSet());

    Assert.assertTrue(prnrs.containsAll(prnrsActual));
    Assert.assertNotEquals(artIds, CollectionUtils.emptyCollection());
  }

  @Test
  public void shouldHaveArticleForPartRefsFromGtmotive() {
    final String partRefNum = "4F0615301J";
    assertPartRefsArticleSearching(partRefNum);
  }

  @Test
  public void shouldHaveArticleForLowercasePartRefsFromGtmotive() {
    final String partRefNum = "4f0615301j";
    assertPartRefsArticleSearching(partRefNum);
  }

  private void assertPartRefsArticleSearching(final String partRefNum) {
    final List<String> prnrs = Arrays.asList(partRefNum);
    final boolean isSaleOnBehalf = false;
    final List<ArticleDoc> articles = articleSearchService.searchArticlesByPartRefs(prnrs, isSaleOnBehalf);
    final List<ArticlePart> parts = articles.parallelStream()
        .flatMap(m -> m.getParts().parallelStream()).collect(Collectors.toList());
    Assert.assertThat(parts,
        Matchers.hasItem(Matchers.hasProperty("pnrn", is(StringUtils.upperCase(partRefNum)))));
  }

  @Test
  public void testSearchArticlesByPartRefsWithEmpty() {
    final boolean isSaleOnBehalf = false;
    final List<ArticleDoc> articles =
        articleSearchService.searchArticlesByPartRefs(Collections.emptyList(), isSaleOnBehalf);
    Assert.assertThat(0, Is.is(articles.size()));
  }

  @Test
  public void testSearchArticlesByPartRefsWithNull() {
    final boolean isSaleOnBehalf = false;
    final List<ArticleDoc> articles = articleSearchService.searchArticlesByPartRefs(null, isSaleOnBehalf);
    Assert.assertThat(0, Is.is(articles.size()));
  }

  @Test
  @Ignore("ES data is not available")
  public void testSearchArticlesByUmarIdSetWithGerman() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final Set<String> umarIds =
        Stream.of("1000000026835759414", "1000000000193071853").collect(Collectors.toSet());
    final Set<String> deNameTexts = Stream.of("Gasfeder, Koffer-/Laderaum", "Wischgummi")
        .collect(Collectors.toSet());

    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByUmarIds(umarIds, PAGE_SIZE_2);

    final Set<String> actualUmarIds =
        articles.getContent().stream().map(ArticleDoc::getIdUmsart).collect(Collectors.toSet());
    final Set<String> actualNameTexts =
        articles.getContent().stream().map(ArticleDoc::getName).collect(Collectors.toSet());

    Assert.assertNotNull(articles);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(0));
    if (articles.hasContent()) {
      Assert.assertTrue(CollectionUtils.isEqualCollection(actualUmarIds, umarIds));
      Assert.assertTrue(CollectionUtils.isEqualCollection(actualNameTexts, deNameTexts));
      Assert.assertTrue(articles.getContent().stream()
        .allMatch(article -> umarIds.contains(article.getIdUmsart())));
    }

  }

  @Test
  @Ignore("The AX have no IT index for ax_articles")
  public void testSearchArticlesByUmarIdSetWithItalian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    final Set<String> umarIds =
        Stream.of("1000000000193071802", "1000000000193071853").collect(Collectors.toSet());
    final Set<String> itNameTexts = Stream
        .of("Motorino d'avviamento", "Refil spazzola tergicristallo").collect(Collectors.toSet());
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByUmarIds(umarIds, PAGE_SIZE_2);

    final Set<String> actualUmarIds =
        articles.getContent().stream().map(ArticleDoc::getIdUmsart).collect(Collectors.toSet());
    final Set<String> actualNameTexts =
        articles.getContent().stream().map(ArticleDoc::getName).collect(Collectors.toSet());

    Assert.assertNotNull(articles);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThan(0));
    Assert.assertTrue(CollectionUtils.isEqualCollection(actualUmarIds, umarIds));
    Assert.assertTrue(CollectionUtils.isEqualCollection(actualNameTexts, itNameTexts));
    Assert.assertTrue(articles.getContent().stream()
        .allMatch(article -> umarIds.contains(article.getIdUmsart())));
  }

  @Test
  @Ignore("The AX have no FR index for ax_articles")
  public void testSearchArticlesByUmarIdSetWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    final Set<String> umarIds =
        Stream.of("1000000000193071802", "1000000000193071853").collect(Collectors.toSet());
    final Set<String> frNameTexts =
        Stream.of("Démarreur", "Lame d'essuie-glace").collect(Collectors.toSet());
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByUmarIds(umarIds, PAGE_SIZE_2);

    final Set<String> actualUmarIds =
        articles.getContent().stream().map(ArticleDoc::getIdUmsart).collect(Collectors.toSet());
    final Set<String> actualNameTexts =
        articles.getContent().stream().map(ArticleDoc::getName).collect(Collectors.toSet());

    Assert.assertNotNull(articles);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThan(0));
    Assert.assertTrue(CollectionUtils.isEqualCollection(actualUmarIds, umarIds));
    Assert.assertTrue(CollectionUtils.isEqualCollection(actualNameTexts, frNameTexts));
    Assert.assertTrue(articles.getContent().stream()
        .allMatch(article -> umarIds.contains(article.getIdUmsart())));
  }

  @Test
  public void testSearchArticlesByUmarIdsWithEmptyUmarIdSet() {
    final Set<String> umarIds = Collections.emptySet();
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByUmarIds(umarIds, PAGE_SIZE_2);

    Assert.assertNotNull(articles);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.equalTo(0));
  }

  @Test
  public void testSearchArticlesByUmarIdsWithNullUmarIdSet() {
    final Set<String> umarIds = null;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByUmarIds(umarIds, PAGE_SIZE_2);

    Assert.assertNotNull(articles);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.is(0));
  }

  @Test
  public void testSearchArticlesByUmarIdsWithInvalidUmarIdSet() {
    final Set<String> umarIds = Stream.of("AAAAA", "23561817326").collect(Collectors.toSet());
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByUmarIds(umarIds, PAGE_SIZE_2);

    Assert.assertNotNull(articles);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.is(0));
  }

  @Test
  public void testSearchArticlesByFreeTextWithMultiFieldMatchSupplierOc90() {
    final String freeText = "oc90";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_100);

    Set<String> suppliers = articles.getContent().parallelStream()
        .filter(p -> Objects.nonNull(p.getSupplier()) && p.getSupplier().contains("MAHLE"))
        .map(m -> m.getSupplier()).collect(Collectors.toSet());

    Assert.assertThat(suppliers.size(), Matchers.is(1));
  }

  @Test
  public void testSearchArticlesByNumberDeepLink() {
    final String articleNr = "5001100420";

    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(articleNr, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .allMatch(p -> p.equals(articleNr));
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByNumberDeepLinkWithNull() {
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(null, PAGE_SIZE_2);

    Assert.assertTrue(articles.getContent().isEmpty());
  }

  @Test
  public void testSearchArticlesByNumberDeepLinkWithEmpty() {
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(StringUtils.EMPTY, PAGE_SIZE_2);

    Assert.assertTrue(articles.getContent().isEmpty());
  }

  @Test
  @Ignore("Simon need index for english")
  public void testSearchArticlesByNumberDeepLinkWithEnglish() {
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    final String articleNr = "5001100420";

    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(articleNr, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .allMatch(p -> p.equals(articleNr));
    Assert.assertTrue(allMatch);
    Assert.assertEquals("Frontscheibe",
        articles.getContent().parallelStream().findFirst().get().getName());
  }

  @Test
  public void testSearchArticlesByNumberDeepLinkWithGerman() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final String articleNr = "5001100420";

    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(articleNr, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .allMatch(p -> p.equals(articleNr));
    Assert.assertTrue(allMatch);
    Assert.assertEquals("Frontscheibe",
        articles.getContent().parallelStream().findFirst().get().getName());
  }

  @Test
  @Ignore("The AX have no FR index for ax_articles")
  public void testSearchArticlesByNumberDeepLinkWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    final String articleNr = "5001100420";

    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(articleNr, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .allMatch(p -> p.equals(articleNr));
    Assert.assertTrue(allMatch);
    Assert.assertEquals("Pare-brise",
        articles.getContent().parallelStream().findFirst().get().getName());
  }

  @Test
  @Ignore("The AX have no IT index for ax_articles")
  public void testSearchArticlesByNumberDeepLinkWithItalian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    final String articleNr = "5001100420";

    final Page<ArticleDoc> articles =
        articleSearchService.searchArticleByNumberDeepLink(articleNr, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getArtnr())
        .allMatch(p -> p.equals(articleNr));
    Assert.assertTrue(allMatch);
    Assert.assertEquals("Parabrezza",
        articles.getContent().parallelStream().findFirst().get().getName());
  }

  @Test
  public void shouldGetArticleByPimId() {
    final int pimId = 1000070294;
    final boolean isSaleOnBehalf = false;
    // to rename the method also, for now just avoid a little change
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(String.valueOf(pimId), PAGE_SIZE_2, isSaleOnBehalf);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(0));
  }

  /**
   * <p>
   * Validates search article by PIM_ID with lock article with DAT.
   * </p>
   *
   * <pre>
   * Test case: 1000542844 1000000011396252565 tm,tmoci,ehdch,ifr,dat,ehaub,rwb,dch
   * </pre>
   *
   */
  @Test
  public void testSearchArticlesByPimIdWithLocksWithDat() {
    final int pimId = 1000542844;
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(String.valueOf(pimId), PAGE_SIZE_2, isSaleOnBehalf, LOCKS_DAT);
    Assert.assertThat(articles.getNumberOfElements(), is(NumberUtils.INTEGER_ZERO));
    Assert.assertFalse(assertLocksAffiliate(articles.getContent(), LOCKS_DAT));
  }

  @Test
  public void testSearchArticlesByPimIdWithLocksWithTnm() {
    final int pimId = 1000542844;
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(String.valueOf(pimId), PAGE_SIZE_2, isSaleOnBehalf, LOCKS_TECHNO);
    Assert.assertThat(articles.getNumberOfElements(), is(NumberUtils.INTEGER_ZERO));
    Assert.assertFalse(assertLocksAffiliate(articles.getContent(), LOCKS_TECHNO));
  }

  @Test
  public void testSearchArticlesByPimIdWithLocksWithDch() {
    final int pimId = 1000542844;
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(String.valueOf(pimId), PAGE_SIZE_2, isSaleOnBehalf, LOCKS_DCH);
    Assert.assertThat(articles.getNumberOfElements(), is(NumberUtils.INTEGER_ZERO));
    Assert.assertFalse(assertLocksAffiliate(articles.getContent(), LOCKS_DCH));
  }

  @Test
  public void testSearchArticlesByPimIdWithLocksWithMat() {
    final int pimId = 1000646933;
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(String.valueOf(pimId), PAGE_SIZE_2, isSaleOnBehalf, LOCKS_MAT);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  public void givenFreetext_SearchArticleFreetextLockedByDatAffiliate() {
    final String freetext = "ox188d";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freetext, LOCKS_AT), PAGE_SIZE_2);
    Assert.assertThat(articles.getNumberOfElements(), Matchers.greaterThanOrEqualTo(2));
    Assert.assertTrue(assertLocksAffiliate(articles.getContent(), LOCKS_AT));
  }

  private static boolean assertLocksAffiliate(List<ArticleDoc> articles, String... esAffilates) {
    if (CollectionUtils.isEmpty(articles)) {
      return false;
    }
    String joinedAff = StringUtils.join(esAffilates, ',');
    return !articles.stream()
        .anyMatch(article -> StringUtils.containsIgnoreCase(article.getLocks(), joinedAff));
  }

  private void searchFreetextAndAssert(final String freeText, int maxAttemptNum) {
    final PageRequest pageRequest = PageUtils.DEF_PAGE;
    final List<String> firstAttempt =
        articleSearchService
            .searchArticlesByFreeText(new KeywordArticleSearchCriteria(freeText, new String[0]),
                pageRequest)
            .getContent().stream().map(a -> a.getArtnr()).collect(Collectors.toList());
    if (maxAttemptNum > 1) {
      List<List<String>> nextAttempts = new ArrayList<>(maxAttemptNum);
      int i = 0;
      do {
        nextAttempts.add(articleSearchService
            .searchArticlesByFreeText(new KeywordArticleSearchCriteria(freeText, new String[0]),
                pageRequest)
            .getContent().stream().map(a -> a.getArtnr()).collect(Collectors.toList()));
      } while (i++ < maxAttemptNum);

      // assert the list in order
      i = 0;
      do {
        Assert.assertThat(firstAttempt,
            IsIterableContainingInOrder.contains(nextAttempts.get(i).toArray()));
      } while (i++ < maxAttemptNum);
      return;
    }
    Assert.assertThat(firstAttempt.size(), Matchers.greaterThan(0));
  }

  @Test
  public void givenWildcardCustomerNumber_ShouldGetArticleResults() {
    final String articleNr = "oc*90";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenWildcardCustomerNumberSuffix_ShouldGetArticleResults() {
    final String articleNr = "oc90*";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenWildcardCustomerNumberPrefix_ShouldGetArticleResults() {
    final String articleNr = "*oc90";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  @Ignore
  public void givenWildcardCustomerNumberWithSpace_ShouldGetArticleResults() {
    final String articleNr = "OC 9*";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  public void givenWildcardSuffixText_shouldGetArticleResults() {
    searchFreetextAndAssert("oc90*", 1);
  }

  @Test
  public void givenWildcardPrefixText_shouldGetArticleResults() {
    searchFreetextAndAssert("*c90", 1);
  }

  @Test
  public void givenWildcardBetweenText_shouldGetArticleResults() {
    searchFreetextAndAssert("o*90", 1);
  }

  @Test
  public void givenWildcardTextWithSpace_shouldGetArticleResults() {
    searchFreetextAndAssert("LS 7*", 1);
  }

  @Test
  public void testSearchArticlesByPimIdWithLocksNotWithinAustria() {
    final int pimId = 1000542844;
    final boolean isSaleOnBehalf = false;
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByIdSagSys(String.valueOf(pimId), PAGE_SIZE_2, isSaleOnBehalf, LOCKS_DAT);
    Assert.assertThat(articles.getTotalElements(), is(NumberUtils.LONG_ZERO));
  }

  @Test
  public void testSearchArticlesByNrWithLocksNotWithinAustria() {
    final String articleNr = "57264021";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_100);
    Assert.assertThat(articles.getTotalElements(), Matchers.greaterThanOrEqualTo(NumberUtils.LONG_ZERO));
  }

  @Test
  public void testSearchArticlesByNrWithLocksWithinAustria() {
    final String articleNr = "nup219ecp";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, LOCKS_AT);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_100);
    Assert.assertThat(articles.getTotalElements(),
        Matchers.greaterThanOrEqualTo(NumberUtils.LONG_ONE));
  }

  @Test
  public void testSearchArticlesByPimId() {
    final String pimId = "1000301977";
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(pimId, ArrayUtils.EMPTY_STRING_ARRAY);
    final Page<ArticleDoc> articles =
        articleSearchService.searchArticlesByNumber(criteria, PAGE_SIZE_2);

    boolean allMatch = articles.getContent().parallelStream().map(m -> m.getIdSagsys())
        .allMatch(p -> p.equals(pimId));
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByFreeTextWithPimId() {
    final String freeText = "1000055887";
    final Page<ArticleDoc> articles = articleSearchService.searchArticlesByFreeText(
        new KeywordArticleSearchCriteria(freeText, new String[0]), PAGE_SIZE_2);

    Set<String> collect = articles.getContent().parallelStream()
        .filter(p -> Objects.nonNull(p.getIdSagsys()) && p.getIdSagsys().contains(freeText))
        .map(m -> m.getIdSagsys()).collect(Collectors.toSet());

    Assert.assertThat(freeText, collect.size(), Matchers.greaterThan(0));
    Assert.assertThat(collect.size(), Matchers.is(1));
    Assert.assertTrue(collect.parallelStream().allMatch(p -> p.contains(freeText)));
  }

  @Test
  public void testSearchArticleByPartNumbersAndSupplier() {
    final String[] partNrs = new String[] { "721400", "721500", "565THU", "565" };
    final String supplier = "THULE";
    final boolean isSaleOnBehalf = false;

    List<ArticleDoc> articles = articleSearchService.searchArticleByPartNumbersAndSupplier(
        partNrs, supplier, isSaleOnBehalf, ArrayUtils.EMPTY_STRING_ARRAY);

    Assert.assertThat(articles.isEmpty(), Matchers.is(false));
    for (ArticleDoc article : articles) {
      Assert.assertThat(article.getSupplier(), Matchers.is(supplier));
      final String[] foundPartNrs = article.getParts().stream().map(ArticlePart::getPnrn)
          .filter(prnr -> Arrays.asList(partNrs).contains(prnr))
          .toArray(String[]::new);
      Assert.assertThat(foundPartNrs.length, Matchers.greaterThanOrEqualTo(1));
    }
  }

  @Test
  public void testSearchArticlesByUniversalPartIncludeArticleId() {
    final String articleId = "1000301977";
    UniversalPartArticleSearchTerm inclSearchTerm =
        UniversalPartArticleSearchTerm.builder().articleIds(Lists.newArrayList(articleId)).build();
    UniversalPartArticleSearchCriteria criteria = UniversalPartArticleSearchCriteria.builder().includeTerm(inclSearchTerm).build();
    final List<ArticleDoc> articles =
        articleSearchService.searchArticlesByUniversalPart(criteria);

    boolean allMatch = articles.parallelStream().map(m -> m.getIdSagsys())
        .allMatch(p -> p.equals(articleId));
    Assert.assertTrue(allMatch);
  }

  @Test
  public void testSearchArticlesByUniversalPartInclGenArtExlArticleId() {
    final String articleId = "1000301977";
    UniversalPartArticleSearchTerm inclSearchTerm =
        UniversalPartArticleSearchTerm.builder().genArts(Lists.newArrayList(GEN_ART_1191)).build();
    UniversalPartArticleSearchTerm exclSearchTerm =
        UniversalPartArticleSearchTerm.builder().articleIds(Lists.newArrayList(articleId)).build();

    UniversalPartArticleSearchCriteria criteria = UniversalPartArticleSearchCriteria.builder()
        .includeTerm(inclSearchTerm).excludeTerm(exclSearchTerm).build();
    final List<ArticleDoc> articles = articleSearchService.searchArticlesByUniversalPart(criteria);

    boolean noneMatch =
        articles.parallelStream().map(m -> m.getIdSagsys()).noneMatch(p -> p.equals(articleId));
    boolean allGenArtMatch = articles.parallelStream().map(m -> m.getGaId())
        .allMatch(p -> p.equals(String.valueOf(GEN_ART_1191)));
    Assert.assertTrue(allGenArtMatch);
    Assert.assertTrue(noneMatch);
  }

  @Test
  public void testSearchArticlesByUniversalPartInclBrandExlArticleId() {
    final String articleId = "1000301977";
    UniversalPartArticleSearchTerm inclSearchTerm =
        UniversalPartArticleSearchTerm.builder().brandIds(Lists.newArrayList(BRAND_5010)).build();
    UniversalPartArticleSearchTerm exclSearchTerm =
        UniversalPartArticleSearchTerm.builder().articleIds(Lists.newArrayList(articleId)).build();

    UniversalPartArticleSearchCriteria criteria = UniversalPartArticleSearchCriteria.builder()
        .includeTerm(inclSearchTerm).excludeTerm(exclSearchTerm).build();
    final List<ArticleDoc> articles = articleSearchService.searchArticlesByUniversalPart(criteria);

    boolean noneMatch =
        articles.parallelStream().map(m -> m.getIdSagsys()).noneMatch(p -> p.equals(articleId));
    boolean allGenArtMatch = articles.parallelStream().map(m -> m.getIdProductBrand())
        .allMatch(p -> p.equals(String.valueOf(BRAND_5010)));
    Assert.assertTrue(allGenArtMatch);
    Assert.assertTrue(noneMatch);
  }
}

