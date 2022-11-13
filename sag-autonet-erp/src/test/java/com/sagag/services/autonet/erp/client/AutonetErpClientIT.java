package com.sagag.services.autonet.erp.client;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.sagag.services.autonet.erp.AutonetErpApplication;
import com.sagag.services.autonet.erp.DataProvider;
import com.sagag.services.autonet.erp.builder.GetErpInformationRequestBuilder;
import com.sagag.services.autonet.erp.config.AutonetErpProperties;
import com.sagag.services.autonet.erp.domain.AutonetErpUserInfo;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationRequestBodyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationResponseBodyType;
import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.domain.article.ArticleDocDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { AutonetErpApplication.class })
@AutonetEshopIntegrationTest
public class AutonetErpClientIT {

  @Autowired
  private AutonetErpClient client;

  @Autowired
  private AutonetErpProperties props;

  @Autowired
  private GetErpInformationRequestBuilder getErpInformationRequestBuilder;

  @Test
  public void testWsCommunication() {
    final AutonetErpUserInfo userInfo = DataProvider.buildAutonetUserInfo();
    final ArticleDocDto article = DataProvider.buildArticleInfo();
    final GetErpInformationRequestBodyType request =
        getErpInformationRequestBuilder.buildRequest(userInfo, Arrays.asList(article));

    final GetErpInformationResponseBodyType response =
        client.getErpInformation(request, new SoapActionCallback(
            props.getSoapActionGetErpInformation()));
    Assert.assertThat(response, Matchers.notNullValue());
  }

}
