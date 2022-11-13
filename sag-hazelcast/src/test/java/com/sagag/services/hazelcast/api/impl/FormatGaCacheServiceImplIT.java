package com.sagag.services.hazelcast.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.FormatGaSearchService;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaElement;
import com.sagag.services.hazelcast.api.FormatGaCacheService;
import com.sagag.services.hazelcast.app.HazelcastApplication;

import org.apache.commons.collections4.ListUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Integration test class for Format Generic Article cache service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HazelcastApplication.class })
@EshopIntegrationTest
public class FormatGaCacheServiceImplIT {

  private static final String GAID_51 = "51";

  @Autowired
  private FormatGaCacheService formatGaCacheService;

  @Autowired
  private FormatGaSearchService formatGaSearchService;

  @Test
  public void testSearchFormatGaByGaIdsWithGermany() {
    if (!formatGaCacheService.exists()) {
      LocaleContextHolder.setLocale(Locale.GERMANY);
      formatGaCacheService.refreshCacheFormatGas(
          formatGaSearchService.getAllFormatGa(PageUtils.DEF_PAGE).getContent());
      String gaid = GAID_51;
      final Map<String, FormatGaDoc> formats =
          formatGaCacheService.searchFormatGaByGaIds(Arrays.asList(gaid));

      FormatGaDoc formatGaDoc = formats.get(gaid);
      if (formatGaDoc == null) {
        return;
      }
      List<String> ctxtDes = formatGaDoc.getElements().parallelStream()
          .map(FormatGaElement::getCtxtDe).collect(Collectors.toList());
      Assert.assertTrue(ctxtDes.contains("Ausstattungsvariante"));
    }
  }

  @Test
  public void testSearchFormatGaByGaIdsWithFrench() {
    LocaleContextHolder.setLocale(Locale.FRENCH);
    formatGaCacheService.refreshCacheFormatGas(
        formatGaSearchService.getAllFormatGa(PageUtils.DEF_PAGE).getContent());
    String gaid = "62";
    final Map<String, FormatGaDoc> formats =
        formatGaCacheService.searchFormatGaByGaIds(Arrays.asList(gaid));
    Assert.assertThat(formats, Matchers.notNullValue());
  }

  @Test
  public void testSearchFormatGaByGaIdsWithItilian() {
    LocaleContextHolder.setLocale(Locale.ITALIAN);
    formatGaCacheService.refreshCacheFormatGas(
        formatGaSearchService.getAllFormatGa(PageUtils.DEF_PAGE).getContent());
    String gaid = "82";
    final Map<String, FormatGaDoc> formats =
        formatGaCacheService.searchFormatGaByGaIds(Arrays.asList(gaid));
    Assert.assertThat(formats, Matchers.notNullValue());
  }

  @Test
  @Ignore("Data is not ready")
  public void testSearchFormatGaByGaIdsWithEnglish() {
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    formatGaCacheService.refreshCacheFormatGas(
        formatGaSearchService.getAllFormatGa(PageUtils.DEF_PAGE).getContent());
    String gaid = GAID_51;
    final Map<String, FormatGaDoc> formats =
        formatGaCacheService.searchFormatGaByGaIds(Arrays.asList(gaid));

    FormatGaDoc formatGaDoc = formats.get(gaid);
    Assert.assertThat(formatGaDoc, Matchers.notNullValue());
    List<String> ctxtDes = ListUtils.emptyIfNull(formatGaDoc.getElements())
        .parallelStream().map(FormatGaElement::getCtxtDe).collect(Collectors.toList());
    Assert.assertTrue(ctxtDes.contains("für Lenkgetriebehersteller"));
  }
}
