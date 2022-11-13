package com.sagag.services.common.currency.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.number.impl.DefaultNumberFormatterImpl;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Ignore
public class DefaultSagCurrencyParserImplTest {

  @InjectMocks
  private DefaultSagCurrencyParserImpl parser;

  @Spy
  private LocaleContextHelper localeContextHelper;

  @InjectMocks
  private DefaultNumberFormatterImpl numberFormatter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFormatNumber() {
    double price = 1005.926;

    String ch = "null" + StringUtils.SPACE + "1'005.93";
    String cz = "null" + StringUtils.SPACE + "1.005,93";

    String resultCz = parser.parse(price, numberFormatter.getNumberFormatter("cs_CZ"),
        SupportedAffiliate.STAKIS_CZECH, null);

    String resultCh = parser.parse(price, numberFormatter.getNumberFormatter("de_CH"),
        SupportedAffiliate.DERENDINGER_CH, null);

    Assert.assertEquals(resultCz, cz);
    Assert.assertEquals(resultCh, ch);
  }
}
