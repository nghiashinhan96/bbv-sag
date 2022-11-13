package com.sagag.services.ax.translator.wint;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.sagag.services.ax.translator.AxSalesOrderPoolTranslator;
import com.sagag.services.common.enums.SupportedAffiliate;

/**
 * Class to verify {@link AxSalesOrderPoolTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WintSalesOrderPoolTranslatorTest {

  @InjectMocks
  private WintSalesOrderPoolTranslator translator;

  @Test
  public void testAlwaysReturnsWintSupportedAff() {
    final List<String> salesOrderPools = new ArrayList<>();
    salesOrderPools.add("-01");
    Lists.newArrayList(SupportedAffiliate.values()).stream()
    .map(SupportedAffiliate::getSalesOrderPool).forEach(salesOrderPools::add);

    salesOrderPools.stream().map(translator::translateToConnect)
    .forEach(aff -> assertThat(aff, Matchers.is(SupportedAffiliate.WINT_SB)));
  }

}
