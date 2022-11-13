package com.sagag.services.domain.autonet.erp;

import com.google.common.collect.Lists;
import com.sagag.services.domain.sag.erp.ErpArticleAvailability;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.domain.sag.erp.ErpArticlePrice;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class AutonetArticleInfosTest {

  private static final String CHF = "CHF";

  private AutonetArticleInfos articleInfos;

  @Before
  public void init() {
    this.articleInfos = new AutonetArticleInfos();
    this.articleInfos.setPrices(initErpArticlePrices());
    this.articleInfos.setMemos(initErpArticleMemos());
    this.articleInfos.setAvailability(initErpArticleAvailability());
  }

  private List<ErpArticlePrice> initErpArticlePrices() {
    final ErpArticlePrice erpArtPrice = new ErpArticlePrice();
    erpArtPrice.setType(0);
    erpArtPrice.setDescription(StringUtils.EMPTY);
    erpArtPrice.setValue(0d);
    erpArtPrice.setRebateValue(0d);
    erpArtPrice.setRebate(0d);
    erpArtPrice.setCurrencyCode(CHF);
    erpArtPrice.setCurrencySymbol(CHF);
    erpArtPrice.setPriceUnit(0d);
    erpArtPrice.setVat(0d);
    erpArtPrice.setTaxIncluded(false);
    return Lists.newArrayList(erpArtPrice);
  }

  private List<ErpArticleMemo> initErpArticleMemos() {
    final ErpArticleMemo erpArtMemo = new ErpArticleMemo();
    return Lists.newArrayList(erpArtMemo);
  }

  private ErpArticleAvailability initErpArticleAvailability() {
    final ErpArticleAvailability availability = new ErpArticleAvailability();
    return availability;
  }

  @Test
  public void testInitObject() {

    Assert.assertThat(this.articleInfos, Matchers.notNullValue());
    Assert.assertThat(this.articleInfos.getPrices(), Matchers.notNullValue());
    Assert.assertThat(this.articleInfos.getPrices().isEmpty(), Matchers.is(false));

    final ErpArticlePrice erpArtPrice = this.articleInfos.getPrices().get(0);
    Assert.assertThat(erpArtPrice.getType(), Matchers.equalTo(0));
    Assert.assertThat(erpArtPrice.getDescription(), Matchers.equalTo(StringUtils.EMPTY));
    Assert.assertThat(erpArtPrice.getValue(), Matchers.equalTo(0d));
    Assert.assertThat(erpArtPrice.getRebateValue(), Matchers.equalTo(0d));
    Assert.assertThat(erpArtPrice.getRebate(), Matchers.equalTo(0d));
    Assert.assertThat(erpArtPrice.getCurrencySymbol(), Matchers.equalTo(CHF));
    Assert.assertThat(erpArtPrice.getCurrencyCode(), Matchers.equalTo(CHF));
    Assert.assertThat(erpArtPrice.getPriceUnit(), Matchers.equalTo(0d));
    Assert.assertThat(erpArtPrice.getVat(), Matchers.equalTo(0d));
    Assert.assertThat(erpArtPrice.isTaxIncluded(), Matchers.equalTo(false));
  }



}
