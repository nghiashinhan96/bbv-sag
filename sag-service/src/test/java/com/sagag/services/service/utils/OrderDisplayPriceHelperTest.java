package com.sagag.services.service.utils;

import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.enums.PriceEnum;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.service.utils.order.OrderDisplayPriceHelper;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderDisplayPriceHelperTest {

  @Test
  public void givenArticleDocDto_should_updateSelectedPrice() {
    final String brand = "FORD";
    final String type = PriceEnum.OEP.toString();

    ArticleRequest articleRequest = AxArticleUtils.createErpArticleRequest("123", 2, "", null);
    ArticleDocDto article = ArticleDocDtoDataTestProvider.createArticleDoc();
    DisplayedPriceDto displayedPriceDto =
        DisplayedPriceDto.builder().brand(brand).brandId(1000L).type(type).build();
    article.setDisplayedPrice(displayedPriceDto);
    OrderDisplayPriceHelper.updateSelectedPrice(articleRequest, article);
    Assert.assertThat(articleRequest.getBrand(), Matchers.comparesEqualTo(brand));
    Assert.assertThat(articleRequest.getPriceDiscTypeId(),
        Matchers.comparesEqualTo(PriceEnum.OEP.getAxId()));
  }

}
