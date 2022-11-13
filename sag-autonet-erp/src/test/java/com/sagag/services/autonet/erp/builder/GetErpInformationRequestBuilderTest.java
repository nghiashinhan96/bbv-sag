package com.sagag.services.autonet.erp.builder;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.autonet.erp.domain.AutonetErpUserInfo;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationRequestBodyType;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class GetErpInformationRequestBuilderTest {

  @InjectMocks
  private GetErpInformationRequestBuilder builder;

  @Spy
  protected com.sagag.services.autonet.erp.wsdl.tmconnect.ObjectFactory tmcObjectFactory =
    new com.sagag.services.autonet.erp.wsdl.tmconnect.ObjectFactory();

  private AutonetErpUserInfo userInfo;

  private ArticleDocDto article;

  private AdditionalSearchCriteria additional;

  @Before
  public void setUp() {
    this.userInfo = new AutonetErpUserInfo();
    this.article = new ArticleDocDto();
    this.article.setAmountNumber(1);
    this.article.setGaId("7");
    this.additional = new AdditionalSearchCriteria();
    this.additional.setSearchString("oc90");
    this.additional.setKTypeNr("1234");
    this.additional.setDetailArticleRequest(true);
  }

  @Test
  public void givenAdditionalThenBuildRequest() {
    testAndAssert(this.userInfo, Arrays.asList(this.article), this.additional);
  }

  @Test
  public void givenNullAdditionalThenBuildRequest() {
    testAndAssert(this.userInfo, Arrays.asList(this.article), null);
  }

  private void testAndAssert(AutonetErpUserInfo userInfo,
      List<ArticleDocDto> articles, AdditionalSearchCriteria additional) {
    GetErpInformationRequestBodyType body = builder.buildRequest(userInfo, articles, additional);
    log.debug("{}", XmlUtils.marshalWithPrettyMode(body));
  }

}
