package com.sagag.services.ivds.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Class to verify business logic for part description builder.
 */
@RunWith(SpringRunner.class)
@Slf4j
public class PartDescriptionBuilderTest {

  @InjectMocks
  private PartDescriptionBuilder partDescriptionBuilder;

  private static ObjectMapper objMapper;

  @Before
  public void setup() {
    objMapper = new ObjectMapper();
    objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  public void shouldBuildPartDescriptionWithHighestCriteriaAndHasConstructionYear()
      throws IOException {

    final String expectedPartDescription =
        "123 / - 61,2  - 16 mm -  Low-Metallic - <strong>Baujahr ab</strong>: 01.07.1982 - <strong>Baujahr bis</strong>: 30.06.1982 - <strong>Baujahr</strong>: 30.06.1982";

    String partDesc = partDescriptionBuilder.build(IvdsDataTestUtils.article("article_1455.json"),
        IvdsDataTestUtils.formatGaDoc("format_ga_1455.json"));

    log.debug("Actual part description = {}", partDesc);

    Assert.assertThat(partDesc, Matchers.equalTo(expectedPartDescription));
  }

  @Test
  public void shouldBuildPartDescriptionWithNullFormatGaAndHasConstructionYear()
      throws IOException {

    final String expectedPartDescription =
        "<strong>Baujahr ab</strong>: 01.07.1982 - <strong>Baujahr bis</strong>: 30.06.1982 - <strong>Baujahr</strong>: 30.06.1982";

    String partDesc = partDescriptionBuilder.build(IvdsDataTestUtils.article("article_1455.json"),
        null);

    log.debug("Actual part description = {}", partDesc);

    Assert.assertThat(partDesc, Matchers.equalTo(expectedPartDescription));
  }

  @Test
  public void shouldBuildPartDescriptionWithEmptyFormatGaElementsAndHasConstructionYear()
      throws IOException {

    final String expectedPartDescription =
        "<strong>Baujahr ab</strong>: 01.07.1982 - <strong>Baujahr bis</strong>: 30.06.1982 - <strong>Baujahr</strong>: 30.06.1982";

    String partDesc = partDescriptionBuilder.build(IvdsDataTestUtils.article("article_1455.json"),
        IvdsDataTestUtils.formatGaDoc("format_ga_1455_empty_elements.json"));

    log.debug("Actual part description = {}", partDesc);

    Assert.assertThat(partDesc, Matchers.equalTo(expectedPartDescription));
  }

  @Test
  public void shouldBuildPartDescriptionWithMultipleCriteriaHasSameCid() throws IOException {
    final String expectedPartDesc =
        "mit Steuergerät für autom.LWR / mit Steuergerät für Xenon / mit Zündgerät / ohne Gasentladungslampe";
    final String partDesc = partDescriptionBuilder.build(
        IvdsDataTestUtils.article("article_1683.json"),
        IvdsDataTestUtils.formatGaDoc("format_ga_1683.json"));

    log.debug("Actual part description = {}", partDesc);

    Assert.assertThat(partDesc, Matchers.equalTo(expectedPartDesc));
  }

  @Test
  public void shouldBuildPartDescriptionWithMultipleCriteriasContainDelimiter()
      throws IOException {
    final String expectedPartDesc =
        "1-Masseelektrode / Anschlussausführung Ring (tassenförmig) / entstört, 1 kOhm / mit Flachdichtsitz / Platin-Mittelelektrode";


    final String partDesc = partDescriptionBuilder.build(
        IvdsDataTestUtils.article("article_1683_2.json"),
        IvdsDataTestUtils.formatGaDoc("format_ga_1683_2.json"));

    log.debug("Actual part description = {}", partDesc);

    Assert.assertThat(partDesc, Matchers.equalTo(expectedPartDesc));
  }

  @Test
  public void verifyWithArticleNr_DDF1225C() throws IOException {
    String articlesJson = IvdsDataTestUtils.readFromTestResources("articles_2896.json");
    List<ArticleDocDto> articles =
        objMapper.readValue(articlesJson, new TypeReference<List<ArticleDocDto>>() {});
    String formatGaMapJson = IvdsDataTestUtils.readFromTestResources("format_ga_2896.json");
    Map<String, FormatGaDoc> formatGaMap =
        objMapper.readValue(formatGaMapJson, new TypeReference<Map<String, FormatGaDoc>>() {});
    for (ArticleDocDto article : articles) {
      String partDesc = partDescriptionBuilder.build(article, formatGaMap.get(article.getGaId()));
      log.debug("Actual part description = {}", partDesc);
    }
  }

  @Test
  public void verifyWithArticleNr_VM220() throws IOException {
    String articlesJson = IvdsDataTestUtils.readFromTestResources("articles_2896_VM220.json");
    List<ArticleDocDto> articles =
        objMapper.readValue(articlesJson, new TypeReference<List<ArticleDocDto>>() {});
    String formatGaMapJson = IvdsDataTestUtils.readFromTestResources("format_ga_2896_VM220.json");
    Map<String, FormatGaDoc> formatGaMap =
        objMapper.readValue(formatGaMapJson, new TypeReference<Map<String, FormatGaDoc>>() {});
    for (ArticleDocDto article : articles) {
      String partDesc = partDescriptionBuilder.build(article, formatGaMap.get(article.getGaId()));
      log.debug("Actual part description = {}", partDesc);
    }
  }
}
