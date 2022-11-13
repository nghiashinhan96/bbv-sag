package com.sagag.services.ax.utils;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

@RunWith(SpringRunner.class)
public class AxArticleUtilsTest {

  private static final String ARRIVAL_TIME_SAMPLE = "2020-02-20T06:10:00Z";

  @Test
  public void processShouldPreferFasterDelivery() {
    Availability avai1 = new Availability();
    avai1.setArrivalTime("2020-02-20T08:10:00Z");
    avai1.setQuantity(10);
    avai1.setStockWarehouse("1026");

    Availability avai2 = new Availability();
    avai2.setArrivalTime("2020-02-20T10:10:00Z");
    avai2.setQuantity(10);
    avai2.setStockWarehouse("1013");

    Availability avai3 = new Availability();
    avai3.setArrivalTime(ARRIVAL_TIME_SAMPLE);
    avai3.setQuantity(10);
    avai3.setStockWarehouse("1014");


    List<Availability> avais = new ArrayList<>();
    avais.add(avai1);
    avais.add(avai2);
    avais.add(avai3);

    ArticleDocDto article = new ArticleDocDto();
    article.setAmountNumber(20);
    article.setArtid("10000");

    ArticleSearchCriteria artCriteria = new ArticleSearchCriteria();

    ArticleDocDto results = AxArticleUtils.findArticleContainsAvailabilities(article, avais, artCriteria);
    List<Availability> avaisResult = results.getAvailabilities();
    assertThat(avaisResult.get(0).getStockWarehouse(), Matchers.is("1014"));
    assertThat(avaisResult.get(1).getStockWarehouse(), Matchers.is("1026"));

  }

  @Test
  public void processShouldPreferAx() {
    Availability avai1 = new Availability();
    avai1.setArrivalTime("2020-02-20T08:10:00Z");
    avai1.setQuantity(10);
    avai1.setStockWarehouse("1026");

    Availability avai2 = new Availability();
    avai2.setArrivalTime(ARRIVAL_TIME_SAMPLE);
    avai2.setQuantity(10);
    avai2.setStockWarehouse("1013");

    Availability autonetAvai = new Availability();
    autonetAvai.setArrivalTime(ARRIVAL_TIME_SAMPLE);
    autonetAvai.setQuantity(10);
    autonetAvai.setStockWarehouse("1015");
    autonetAvai.setExternalSource(true);

    List<Availability> avais = new ArrayList<>();
    avais.add(avai1);
    avais.add(avai2);
    avais.add(autonetAvai);

    ArticleDocDto article = new ArticleDocDto();
    article.setAmountNumber(20);
    article.setArtid("10000");

    ArticleSearchCriteria artCriteria = new ArticleSearchCriteria();

    ArticleDocDto results = AxArticleUtils.findArticleContainsAvailabilities(article, avais, artCriteria);
    List<Availability> avaisResult = results.getAvailabilities();
    assertThat(avaisResult.get(0).getStockWarehouse(), Matchers.is("1015"));
    assertThat(avaisResult.get(1).getStockWarehouse(), Matchers.is("1013"));

  }
}
