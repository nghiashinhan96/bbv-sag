package com.sagag.services.stakis.erp.converter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.config.StakisConfigData;
import com.sagag.services.stakis.erp.config.StakisErpProperties;
import com.sagag.services.stakis.erp.converter.impl.article.TmArticleConverter;
import com.sagag.services.stakis.erp.converter.impl.article.TmAvailabilityConverter;
import com.sagag.services.stakis.erp.converter.impl.article.TmDepositItemConverter;
import com.sagag.services.stakis.erp.converter.impl.article.TmMemoTextConverter;
import com.sagag.services.stakis.erp.converter.impl.article.TmPriceConverterV2;
import com.sagag.services.stakis.erp.converter.impl.article.TmQuantityMultipleConverter;
import com.sagag.services.stakis.erp.converter.impl.article.TmStatusInformationConverter;
import com.sagag.services.stakis.erp.utils.ErpInformationExtractors;
import com.sagag.services.stakis.erp.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.MasterData;

@RunWith(SpringRunner.class)
public class TmArticleConverterTest {

  private static final String LANGUAGE_CS = "cs";

  @InjectMocks
  private TmArticleConverter converter;

  @Mock
  private TmPriceConverterV2 priceConverter;

  @Mock
  private TmAvailabilityConverter availabilityConverter;

  @Mock
  private TmStatusInformationConverter statusInformationConverter;

  @Mock
  private TmMemoTextConverter memoTextConverter;

  @Mock
  private TmQuantityMultipleConverter tmQuantityMultipleConverter;

  @Mock
  private TmDepositItemConverter depositItemConverter;

  @Mock
  private StakisErpProperties erpProps;

  @Mock
  private StakisConfigData configData;

  @Before
  public void setup() throws Exception {
    LocaleContextHolder.setLocale(new Locale(LANGUAGE_CS));
    Mockito.doCallRealMethod().when(priceConverter).apply(Mockito.any(), Mockito.any(), Mockito.anyDouble());
    Mockito.doCallRealMethod().when(availabilityConverter).apply(Mockito.any(), Mockito.any(), Mockito.anyString());
    Mockito.doCallRealMethod().when(statusInformationConverter).apply(Mockito.any());
    Mockito.doCallRealMethod().when(memoTextConverter).apply(Mockito.any());
    Mockito.doCallRealMethod().when(tmQuantityMultipleConverter).apply(Mockito.any());

    // Set data configuration for ERP properties.
    Mockito.when(erpProps.getConfig()).thenReturn(configData);
    Mockito.when(configData.getFgasItemText()).thenReturn(DataProvider.buildFGasTextMap());

    converter.afterPropertiesSet();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllArgExceptionWithNullArticle() {
    converter.convert(null, Collections.emptyMap(), Collections.emptyList(),
        Collections.emptyMap(), LANGUAGE_CS, DataProvider.DF_VAT_FOR_CZ);
  }

  @Test
  public void shouldConvertSafelyWithEmptyResponse() throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10403173").get(0);

    converter.convert(article, Collections.emptyMap(),
        Collections.emptyList(), Collections.emptyMap(), LANGUAGE_CS, DataProvider.DF_VAT_FOR_CZ);

    Assert.assertThat(article.getIdSagsys(), Matchers.equalTo("10403173"));

    Mockito.verify(priceConverter, Mockito.times(0)).apply(Mockito.any(), Mockito.any(), Mockito.anyDouble());
    Mockito.verify(availabilityConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(), Mockito.anyString());
    Mockito.verify(statusInformationConverter, Mockito.times(1)).apply(Mockito.any());
    Mockito.verify(memoTextConverter, Mockito.times(1)).apply(Mockito.any());
    Mockito.verify(depositItemConverter, Mockito.times(0)).apply(Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyInt());
  }

  @Test
  public void shouldConvertArticleSucceed() throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10403173").get(0);

    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10403173.xml").getValue();

    final List<ErpInformation> erpInfos =
        ErpInformationExtractors.extractErpInformationList(responseBody);

    final Optional<MasterData> masterDataOpt =
        ErpInformationExtractors.extractMasterData(responseBody);

    final MasterData masterData = masterDataOpt.get();
    final Map<String, String> articleIdUuidMap =
        ErpInformationExtractors.extractArticleIdUUIDMap(masterData);

    final Map<String, AvailabilityState> availabilityStateTypesMasterData =
        ErpInformationExtractors.extractAvailabilityStateMap(masterData);

    converter.convert(article, articleIdUuidMap, erpInfos,
        availabilityStateTypesMasterData, LANGUAGE_CS, DataProvider.DF_VAT_FOR_CZ);

    Assert.assertThat(article.getIdSagsys(), Matchers.equalTo("10403173"));

    Mockito.verify(priceConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(), Mockito.anyDouble());
    Mockito.verify(availabilityConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(), Mockito.anyString());
    Mockito.verify(statusInformationConverter, Mockito.times(1)).apply(Mockito.any());
    Mockito.verify(memoTextConverter, Mockito.times(1)).apply(Mockito.any());
    Mockito.verify(depositItemConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyInt());
  }

  @Test
  public void shouldConvertArticleSucceed_10756280() throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10756280").get(0);

    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10756280.xml").getValue();

    final List<ErpInformation> erpInfos =
        ErpInformationExtractors.extractErpInformationList(responseBody);

    final Optional<MasterData> masterDataOpt =
        ErpInformationExtractors.extractMasterData(responseBody);

    final MasterData masterData = masterDataOpt.get();
    final Map<String, String> articleIdUuidMap =
        ErpInformationExtractors.extractArticleIdUUIDMap(masterData);

    final Map<String, AvailabilityState> availabilityStateTypesMasterData =
        ErpInformationExtractors.extractAvailabilityStateMap(masterData);

    converter.convert(article, articleIdUuidMap, erpInfos,
        availabilityStateTypesMasterData, LANGUAGE_CS, DataProvider.DF_VAT_FOR_CZ);

    Assert.assertThat(article.getIdSagsys(), Matchers.equalTo("10756280"));

    Assert.assertThat(article.isAllowedAddToShoppingCart(), Matchers.is(true));

    Mockito.verify(priceConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(), Mockito.anyDouble());
    Mockito.verify(availabilityConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(), Mockito.anyString());
    Mockito.verify(statusInformationConverter, Mockito.times(1)).apply(Mockito.any());
    Mockito.verify(memoTextConverter, Mockito.times(1)).apply(Mockito.any());
    Mockito.verify(depositItemConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyInt());
  }

}
