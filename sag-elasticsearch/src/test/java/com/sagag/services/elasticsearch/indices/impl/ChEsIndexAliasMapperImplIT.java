package com.sagag.services.elasticsearch.indices.impl;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.elasticsearch.ElasticsearchApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@ChEshopIntegrationTest
public class ChEsIndexAliasMapperImplIT extends AbstractEsIndexAliasMapperImplIT {

  @Test
  public void testMapAliasWithChDe() {

    LocaleContextHolder.setLocale(Locale.GERMAN);

    final Map<String, String> actualAndExpectedMap = new HashMap<>();
    actualAndExpectedMap.put("articles", "ax_ch_articles_de");
    actualAndExpectedMap.put("article_vehicle", "ax_ch_article_vehicle_de");
    actualAndExpectedMap.put("vehicle_genart_art", "ax_ch_vehicle_genart_art_de");
    actualAndExpectedMap.put("categories", "ch_categories_de");
    actualAndExpectedMap.put("wss_artgrp", "wss_artgrp_ch");
    actualAndExpectedMap.put("criteria", "criteria_de");
    actualAndExpectedMap.put("format_ga", "ch_format_ga_de");
    actualAndExpectedMap.put("genart", "genart_de");
    actualAndExpectedMap.put("models", "models_de");
    actualAndExpectedMap.put("vehicles", "vehicles_de");
    actualAndExpectedMap.put("customers", "ax_ch_customers");
    actualAndExpectedMap.put("unitree", "ch_unitree_de");
    actualAndExpectedMap.put("license_plate", "astra");
    actualAndExpectedMap.put("makes", "makes");
    actualAndExpectedMap.put("vehicle_genart", "ch_vehicle_genart");
    actualAndExpectedMap.put("suppliers", "suppliers");
    actualAndExpectedMap.put("brand_priority", "brand_priority");
    actualAndExpectedMap.put("prod_alldata", "prod_alldata");

    testMapAliasByCountryAndLanguage(actualAndExpectedMap);
  }

  @Test
  public void testMapAliasWithChFr() {

    LocaleContextHolder.setLocale(Locale.FRENCH);

    final Map<String, String> actualAndExpectedMap = new HashMap<>();
    actualAndExpectedMap.put("articles", "ax_ch_articles_fr");
    actualAndExpectedMap.put("article_vehicle", "ax_ch_article_vehicle_fr");
    actualAndExpectedMap.put("vehicle_genart_art", "ax_ch_vehicle_genart_art_fr");
    actualAndExpectedMap.put("categories", "ch_categories_fr");
    actualAndExpectedMap.put("wss_artgrp", "wss_artgrp_ch");
    actualAndExpectedMap.put("criteria", "criteria_fr");
    actualAndExpectedMap.put("format_ga", "ch_format_ga_fr");
    actualAndExpectedMap.put("genart", "genart_fr");
    actualAndExpectedMap.put("models", "models_fr");
    actualAndExpectedMap.put("vehicles", "vehicles_fr");
    actualAndExpectedMap.put("customers", "ax_ch_customers");
    actualAndExpectedMap.put("unitree", "ch_unitree_fr");
    actualAndExpectedMap.put("license_plate", "astra");
    actualAndExpectedMap.put("makes", "makes");
    actualAndExpectedMap.put("vehicle_genart", "ch_vehicle_genart");
    actualAndExpectedMap.put("suppliers", "suppliers");
    actualAndExpectedMap.put("brand_priority", "brand_priority");
    actualAndExpectedMap.put("prod_alldata", "prod_alldata");

    testMapAliasByCountryAndLanguage(actualAndExpectedMap);
  }

  @Test
  public void testMapAliasWithChIt() {

    LocaleContextHolder.setLocale(Locale.ITALIAN);

    final Map<String, String> actualAndExpectedMap = new HashMap<>();
    actualAndExpectedMap.put("articles", "ax_ch_articles_it");
    actualAndExpectedMap.put("article_vehicle", "ax_ch_article_vehicle_it");
    actualAndExpectedMap.put("vehicle_genart_art", "ax_ch_vehicle_genart_art_it");
    actualAndExpectedMap.put("categories", "ch_categories_it");
    actualAndExpectedMap.put("wss_artgrp", "wss_artgrp_ch");
    actualAndExpectedMap.put("criteria", "criteria_it");
    actualAndExpectedMap.put("format_ga", "ch_format_ga_it");
    actualAndExpectedMap.put("genart", "genart_it");
    actualAndExpectedMap.put("models", "models_it");
    actualAndExpectedMap.put("vehicles", "vehicles_it");
    actualAndExpectedMap.put("customers", "ax_ch_customers");
    actualAndExpectedMap.put("unitree", "ch_unitree_it");
    actualAndExpectedMap.put("license_plate", "astra");
    actualAndExpectedMap.put("makes", "makes");
    actualAndExpectedMap.put("vehicle_genart", "ch_vehicle_genart");
    actualAndExpectedMap.put("suppliers", "suppliers");
    actualAndExpectedMap.put("brand_priority", "brand_priority");
    actualAndExpectedMap.put("prod_alldata", "prod_alldata");

    testMapAliasByCountryAndLanguage(actualAndExpectedMap);
  }

}
