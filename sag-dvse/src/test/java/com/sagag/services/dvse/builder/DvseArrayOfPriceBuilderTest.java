package com.sagag.services.dvse.builder;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfPrice;

/**
 * UT for {@link DvseArrayOfPriceBuilder}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DvseArrayOfPriceBuilderTest {

  @InjectMocks
  private DvseArrayOfPriceBuilder builder;

  @Test
  public void givenArticlePrice_shouldReturnFullyPriceInfo() {
    final ArticleDocDto article = new ArticleDocDto();
    final PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(10d);
    price.getPrice().setRecommendedRetailPrice(11d);
    price.getPrice().setNetPrice(0.6d);
    price.getPrice().setOepPrice(100d);
    article.setPrice(price);

    final ArrayOfPrice prices = builder.buildArrayOfPrice(Optional.of(article));
    Assert.assertThat(prices.getPrice().size(), Matchers.is(3));
  }

  @Test
  public void givenEmptyArticlePrice_shouldReturnFullyPriceInfo() {
    final ArrayOfPrice prices = builder.buildArrayOfPrice(Optional.empty());
    Assert.assertThat(prices.getPrice().size(), Matchers.is(0));
  }

}
