package com.sagag.services.ivds.promotion.impl;

import static org.junit.Assert.assertThat;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.ivds.promotion.ArticleComparator;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StockComparatorTest {

  @Test
  public void compare_shouldReturnSortedArticle_givenArticlesWithStocks() throws Exception {
    ArticleComparator stockComparator = StockComparator.getInstance();

    ArticleStock stock1 = ArticleStock.builder().stock(2d).build();
    ArticleDocDto art1 = new ArticleDocDto();
    art1.setIdSagsys("1000001");
    art1.setStock(stock1);
    art1.setTotalAxStock(10d);


    ArticleStock stock2 = ArticleStock.builder().stock(2d).build();
    ArticleDocDto art2 = new ArticleDocDto();
    art2.setIdSagsys("1000002");
    art2.setStock(stock2);
    art2.setTotalAxStock(11d);

    ArticleStock stock3 = ArticleStock.builder().stock(5d).build();
    ArticleDocDto art3 = new ArticleDocDto();
    art3.setIdSagsys("1000003");
    art3.setStock(stock3);
    art3.setTotalAxStock(6d);


    ArticleDocDto art4 = new ArticleDocDto();
    art4.setIdSagsys("1000004");
    art4.setTotalAxStock(100d);

    ArticleStock stock5 = ArticleStock.builder().stock(4d).build();
    ArticleDocDto art5 = new ArticleDocDto();
    art5.setIdSagsys("1000005");
    art5.setStock(stock5);
    art5.setTotalAxStock(20d);

    ArticleDocDto art6 = new ArticleDocDto();
    art6.setIdSagsys("1000006");

    List<ArticleDocDto> sortedArts = Arrays.asList(art1, art2, art3, art4, art6, art5);
    sortedArts.sort(stockComparator);

    assertThat(sortedArts.get(0).getIdSagsys(), Matchers.is("1000005"));
    assertThat(sortedArts.get(1).getIdSagsys(), Matchers.is("1000003"));
    assertThat(sortedArts.get(2).getIdSagsys(), Matchers.is("1000004"));
    assertThat(sortedArts.get(3).getIdSagsys(), Matchers.is("1000002"));
    assertThat(sortedArts.get(4).getIdSagsys(), Matchers.is("1000001"));
    assertThat(sortedArts.get(5).getIdSagsys(), Matchers.is("1000006"));
  }
}
