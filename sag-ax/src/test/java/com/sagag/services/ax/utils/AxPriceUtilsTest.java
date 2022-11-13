package com.sagag.services.ax.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

/**
 * Ax price Utilities.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public final class AxPriceUtilsTest {

  @Test
  public void testUpdateVatRatePrice() {
    final Double vatRate = 10D;
    final PriceWithArticlePrice articlePrice = new PriceWithArticlePrice();
    articlePrice.setUvpePrice(1.0);
    articlePrice.setNetPrice(100D);
    articlePrice.setGrossPrice(200D);
    
    AxPriceUtils.updateVatRatePrice(PriceWithArticle.builder().price(articlePrice).build(), vatRate);

    assertNotNull(articlePrice.getGrossPriceWithVat());
    assertNotNull(articlePrice.getNetPriceWithVat());
    assertNotNull(articlePrice.getUvpePriceWithVat());
  }
	
 
}
