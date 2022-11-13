package com.sagag.services.elasticsearch.indices.impl;

import com.sagag.services.common.annotation.CzEshopIntegrationTest;
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
@CzEshopIntegrationTest
public class CzEsIndexAliasMapperImplIT extends AbstractEsIndexAliasMapperImplIT {

  @Test
  public void testMapAliasWithCzDe() {

    LocaleContextHolder.setLocale(Locale.GERMAN);

    final Map<String, String> actualAndExpectedMap = new HashMap<>();
    actualAndExpectedMap.put("articles", "cz_articles_de");
    actualAndExpectedMap.put("article_vehicle", "cz_article_vehicle_de");
    actualAndExpectedMap.put("vehicle_genart_art", "cz_vehicle_genart_art_de");
    actualAndExpectedMap.put("categories", "cz_categories_de");
    actualAndExpectedMap.put("wss_artgrp", "wss_artgrp_cz");
    actualAndExpectedMap.put("criteria", "criteria_de");
    actualAndExpectedMap.put("format_ga", "cz_format_ga_de");
    actualAndExpectedMap.put("genart", "genart_de");
    actualAndExpectedMap.put("models", "models_de");
    actualAndExpectedMap.put("vehicles", "vehicles_de");
    actualAndExpectedMap.put("customers", "cz_customers");
    actualAndExpectedMap.put("unitree", "cz_unitree_de");
    actualAndExpectedMap.put("license_plate", "astra");
    actualAndExpectedMap.put("makes", "makes");
    actualAndExpectedMap.put("vehicle_genart", "cz_vehicle_genart");
    actualAndExpectedMap.put("suppliers", "suppliers");
    actualAndExpectedMap.put("brand_priority", "brand_priority");
    actualAndExpectedMap.put("prod_alldata", "prod_alldata");

    testMapAliasByCountryAndLanguage(actualAndExpectedMap);
  }

  @Test
  public void testMapAliasWithCzCs() {

    LocaleContextHolder.setLocale(new Locale("cs"));

    final Map<String, String> actualAndExpectedMap = new HashMap<>();
    actualAndExpectedMap.put("articles", "cz_articles_cs");
    actualAndExpectedMap.put("article_vehicle", "cz_article_vehicle_cs");
    actualAndExpectedMap.put("vehicle_genart_art", "cz_vehicle_genart_art_cs");
    actualAndExpectedMap.put("categories", "cz_categories_cs");
    actualAndExpectedMap.put("wss_artgrp", "wss_artgrp_cz");
    actualAndExpectedMap.put("criteria", "criteria_cs");
    actualAndExpectedMap.put("format_ga", "cz_format_ga_cs");
    actualAndExpectedMap.put("genart", "genart_cs");
    actualAndExpectedMap.put("models", "models_cs");
    actualAndExpectedMap.put("vehicles", "vehicles_cs");
    actualAndExpectedMap.put("customers", "cz_customers");
    actualAndExpectedMap.put("unitree", "cz_unitree_cs");
    actualAndExpectedMap.put("license_plate", "astra");
    actualAndExpectedMap.put("makes", "makes");
    actualAndExpectedMap.put("vehicle_genart", "cz_vehicle_genart");
    actualAndExpectedMap.put("suppliers", "suppliers");
    actualAndExpectedMap.put("brand_priority", "brand_priority");
    actualAndExpectedMap.put("prod_alldata", "prod_alldata");

    testMapAliasByCountryAndLanguage(actualAndExpectedMap);
  }

  @Test
  public void testMapAliasWithCzEn() {

    LocaleContextHolder.setLocale(Locale.ENGLISH);

    final Map<String, String> actualAndExpectedMap = new HashMap<>();
    actualAndExpectedMap.put("articles", "cz_articles_en");
    actualAndExpectedMap.put("article_vehicle", "cz_article_vehicle_en");
    actualAndExpectedMap.put("vehicle_genart_art", "cz_vehicle_genart_art_en");
    actualAndExpectedMap.put("categories", "cz_categories_en");
    actualAndExpectedMap.put("wss_artgrp", "wss_artgrp_cz");
    actualAndExpectedMap.put("criteria", "criteria_en");
    actualAndExpectedMap.put("format_ga", "cz_format_ga_en");
    actualAndExpectedMap.put("genart", "genart_en");
    actualAndExpectedMap.put("models", "models_en");
    actualAndExpectedMap.put("vehicles", "vehicles_en");
    actualAndExpectedMap.put("customers", "cz_customers");
    actualAndExpectedMap.put("unitree", "cz_unitree_en");
    actualAndExpectedMap.put("license_plate", "astra");
    actualAndExpectedMap.put("makes", "makes");
    actualAndExpectedMap.put("vehicle_genart", "cz_vehicle_genart");
    actualAndExpectedMap.put("suppliers", "suppliers");
    actualAndExpectedMap.put("brand_priority", "brand_priority");
    actualAndExpectedMap.put("prod_alldata", "prod_alldata");

    testMapAliasByCountryAndLanguage(actualAndExpectedMap);
  }

}
