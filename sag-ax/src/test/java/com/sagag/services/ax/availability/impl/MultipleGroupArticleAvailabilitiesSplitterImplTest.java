package com.sagag.services.ax.availability.impl;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.availability.helper.MultipleGroupArticleAvailabilitiesSplitterImpl;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

@RunWith(MockitoJUnitRunner.class)
public class MultipleGroupArticleAvailabilitiesSplitterImplTest {

  @InjectMocks
  private MultipleGroupArticleAvailabilitiesSplitterImpl multipleGroupArticleAvailabilitiesSplitterImpl;

  private String testArticleId = "1001261486" ;
  @Test
  public void splitAvailabilities_shouldCorrectAvai_givenAllAxAvaiAndNotSplit() throws Exception {
    Availability a1_axAvai = Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(15)
        .articleId(testArticleId).build();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setIdSagsys(testArticleId);
    article1.setAmountNumber(10);

    List<Availability> avai1s = new ArrayList<>();
    avai1s.add(a1_axAvai);
    article1.setAvailabilities(avai1s);

    Availability a2_axAvai = Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(15)
        .articleId(testArticleId).build();

    ArticleDocDto article2 = new ArticleDocDto();
    article2.setIdSagsys(testArticleId);
    article2.setAmountNumber(5);
    article2.setAvailabilities(Arrays.asList(a1_axAvai));

    List<Availability> avai2s = new ArrayList<>();
    avai2s.add(a2_axAvai);
    article2.setAvailabilities(avai2s);

    multipleGroupArticleAvailabilitiesSplitterImpl
        .splitAvailabilities(Arrays.asList(article1, article2));

    assertThat(article1.getAvailabilities().size(), Matchers.is(1));
    assertThat(article1.getAvailabilities().get(0).getQuantity(), Matchers.is(10));

    assertThat(article2.getAvailabilities().size(), Matchers.is(1));
    assertThat(article2.getAvailabilities().get(0).getQuantity(), Matchers.is(5));
  }

  @Test
  public void splitAvailabilities_shouldCorrectAvai_givenAllAxAvaiAndSplit() throws Exception {
    Availability a1_axAvai1 = Availability.builder().arrivalTime("2020-02-20T06:10:00Z")
        .quantity(11).articleId(testArticleId).build();
    Availability a1_axAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z").quantity(4)
        .articleId(testArticleId).build();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setIdSagsys(testArticleId);
    article1.setAmountNumber(10);

    List<Availability> avai1s = new ArrayList<>();
    avai1s.add(a1_axAvai1);
    avai1s.add(a1_axAvai2);
    article1.setAvailabilities(avai1s);

    Availability a2_axAvai1 = Availability.builder().arrivalTime("2020-02-20T06:10:00Z")
        .quantity(11).articleId(testArticleId).build();
    Availability a2_axAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z").quantity(4)
        .articleId(testArticleId).build();


    ArticleDocDto article2 = new ArticleDocDto();
    article2.setIdSagsys("1001261486");
    article2.setAmountNumber(5);

    List<Availability> avai2s = new ArrayList<>();
    avai2s.add(a2_axAvai1);
    avai2s.add(a2_axAvai2);
    article2.setAvailabilities(avai2s);

    multipleGroupArticleAvailabilitiesSplitterImpl
        .splitAvailabilities(Arrays.asList(article1, article2));

    assertThat(article1.getAvailabilities().size(), Matchers.is(1));
    assertThat(article1.getAvailabilities().get(0).getQuantity(), Matchers.is(10));
    assertThat(article1.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T06:10:00Z"));

    assertThat(article2.getAvailabilities().size(), Matchers.is(2));
    assertThat(article2.getAvailabilities().get(0).getQuantity(), Matchers.is(1));
    assertThat(article2.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T06:10:00Z"));
    assertThat(article2.getAvailabilities().get(1).getQuantity(), Matchers.is(4));
    assertThat(article2.getAvailabilities().get(1).getArrivalTime(),
        Matchers.is("2020-02-20T08:10:00Z"));
  }

  @Test
  public void splitAvailabilities_shouldCorrectAvai_givenAllAxAndVenAvaiAndSplitPosition()
      throws Exception {
    Availability a1_axAvai1 = Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(5)
        .articleId(testArticleId).build();
    Availability a1_axAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z").quantity(6)
        .articleId(testArticleId).venExternalSource(true).externalSource(true).build();
    Availability a1_axAvai3 = Availability.builder().arrivalTime("2020-02-20T10:10:00Z").quantity(4)
        .articleId(testArticleId).build();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setIdSagsys("1001261486");
    article1.setAmountNumber(10);

    List<Availability> avai1s = new ArrayList<>();
    avai1s.add(a1_axAvai1);
    avai1s.add(a1_axAvai2);
    avai1s.add(a1_axAvai3);
    article1.setAvailabilities(avai1s);

    Availability a2_axAvai1 = Availability.builder().arrivalTime("2020-02-20T06:10:00Z").quantity(5)
        .articleId(testArticleId).build();
    Availability a2_axAvai2 = Availability.builder().arrivalTime("2020-02-20T08:10:00Z").quantity(6)
        .articleId(testArticleId).venExternalSource(true).externalSource(true).build();
    Availability a2_axAvai3 = Availability.builder().arrivalTime("2020-02-20T10:10:00Z").quantity(4)
        .articleId(testArticleId).build();


    ArticleDocDto article2 = new ArticleDocDto();
    article2.setIdSagsys("1001261486");
    article2.setAmountNumber(5);

    List<Availability> avai2s = new ArrayList<>();
    avai2s.add(a2_axAvai1);
    avai2s.add(a2_axAvai2);
    avai2s.add(a2_axAvai3);
    article2.setAvailabilities(avai2s);

    multipleGroupArticleAvailabilitiesSplitterImpl
        .splitAvailabilities(Arrays.asList(article1, article2));

    assertThat(article1.getAvailabilities().size(), Matchers.is(3));
    assertThat(article1.getAvailabilities().get(0).getQuantity(), Matchers.is(5));
    assertThat(article1.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T06:10:00Z"));

    assertThat(article1.getAvailabilities().get(1).getQuantity(), Matchers.is(1));
    assertThat(article1.getAvailabilities().get(1).getArrivalTime(),
        Matchers.is("2020-02-20T08:10:00Z"));

    assertThat(article1.getAvailabilities().get(2).getQuantity(), Matchers.is(4));
    assertThat(article1.getAvailabilities().get(2).getArrivalTime(),
        Matchers.is("2020-02-20T10:10:00Z"));

    assertThat(article2.getAvailabilities().size(), Matchers.is(1));
    assertThat(article2.getAvailabilities().get(0).getQuantity(), Matchers.is(5));
    assertThat(article2.getAvailabilities().get(0).getArrivalTime(),
        Matchers.is("2020-02-20T08:10:00Z"));
  }
}
