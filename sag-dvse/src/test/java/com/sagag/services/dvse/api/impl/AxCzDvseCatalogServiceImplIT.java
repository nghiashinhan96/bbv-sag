package com.sagag.services.dvse.api.impl;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.AxCzEshopIntegrationTest;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.dvse.DvseApplication;
import com.sagag.services.dvse.DvseDataProvider;
import com.sagag.services.dvse.wsdl.tmconnect.ErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { DvseApplication.class })
@AxCzEshopIntegrationTest
@Slf4j
public class AxCzDvseCatalogServiceImplIT extends AbstractDvseCatalogServiceImplIT{

  private static final int FIRST_INDEX = 0;
  @Autowired
  private AxCzCatalogServiceImpl axCzcatalogService;

  @Override
  @Before
  public void init() {
    Optional<Customer> customer = custExtService
        .findCustomerByNumber(SupportedAffiliate.SAG_CZECH.getCompanyName(), "1100007");
    UserInfo userInfo = DvseDataProvider.createUserInfo(SupportedAffiliate.SAG_CZECH, 41465L, customer);
    userInfo.getSettings().setPriceTypeDisplayEnum(PriceDisplayTypeEnum.DPC_GROSS);
    userCacheService.put(userInfo);
    initializeUserAppContext(userInfo.key());
  }

  @Test
  public void testGetArticleInformationSuccessfully() {

    GetErpInformation request = DvseDataProvider.createArticleInformationRequestAxCz();

    log.info("The [GetErpInformation] XML request: \n{} ",
        XmlUtils.marshalWithPrettyMode(request));
    GetErpInformationResponse response = axCzcatalogService.getArticleInfos(request);

    log.info("The [GetErpInformationResponse] XML request: \n{} ",
        XmlUtils.marshalWithPrettyMode(response));

    Assert.assertNotNull(response);
    List<ErpInformation> erpInfo =
        response.getGetErpInformationResult().getErpArticleInformation().getErpInformation();
    Assert.assertNotNull(erpInfo);
    Assert.assertThat(erpInfo.size(), Matchers.is(1));
    Assert.assertNotNull(erpInfo.get(FIRST_INDEX).getPrices().getPrice());
    //Return 4 types of price
    Assert.assertThat(erpInfo.get(FIRST_INDEX).getPrices().getPrice().size(), Matchers.is(4));

    Assert.assertNotNull(response.getGetErpInformationResult().getMasterData()
        .getAvailabilityStates().getAvailabilityState());
    Assert.assertNotNull(response.getGetErpInformationResult().getMasterData()
        .getAvailabilityStates().getAvailabilityState().get(FIRST_INDEX).getDescription());

  }

}
