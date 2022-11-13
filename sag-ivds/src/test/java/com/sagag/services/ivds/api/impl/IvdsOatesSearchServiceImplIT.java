package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.api.impl.CategoryCacheServiceImpl;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.oates.config.OatesProfile;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@ChEshopIntegrationTest
@OatesProfile
@Slf4j
public class IvdsOatesSearchServiceImplIT extends BaseSearchServiceImplIT {

  @Autowired
  private IvdsOatesSearchServiceImpl service;

  @Autowired
  private CategoryCacheServiceImpl categoryCacheService;

  private VehicleDto vehicle;

  private UserInfo userInfo;

  @Before
  public void setup() {
    this.vehicle = new VehicleDto();
    this.vehicle.setVehId(DataProvider.VEHICLE_ID);

    if (!categoryCacheService.exists()) {
      categoryCacheService.refreshCacheAll();
    }

    if (this.userInfo == null) {
      this.userInfo = this.loadUserInfo(SupportedAffiliate.TECHNOMAG, "4123455");
    }
  }

  @Test
  public void testGetOilTypesByVehicleId() throws ServiceException {
    final String[] inputGaIds = StringUtils.split(
        "402,3405,82,400001,407,3403,1164,2697,78,83,4149,1862,3224,3225,5685,4734,2312",
        SagConstants.COMMA_NO_SPACE);
    final List<String> oilGaIds = Arrays.asList(inputGaIds);
    final List<OilTypeIdsDto> typeIds =
        service.getOilTypesByVehicleId(Optional.of(this.vehicle), oilGaIds,
            Arrays.asList("105475", "105474"));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(typeIds));
    Assert.assertThat(typeIds.isEmpty(), Matchers.is(false));
  }

  @Test
  public void testGetOilTypesByVehicleId_V23178M23178() throws ServiceException {
    this.vehicle.setVehId("V23178M23178");
    final String[] inputGaIds = StringUtils.split(
        "402,3405,82,400001,407,3403,1164,2697,78,83,4149,1862,3224,3225,5685,4734,2312",
        SagConstants.COMMA_NO_SPACE);
    final List<String> oilGaIds = Arrays.asList(inputGaIds);
    final List<OilTypeIdsDto> typeIds =
        service.getOilTypesByVehicleId(Optional.of(this.vehicle), oilGaIds,
            Arrays.asList("105475", "105474"));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(typeIds));
    Assert.assertThat(typeIds.isEmpty(), Matchers.is(false));
  }

  @Test
  public void testSearchOilRecommendArticles() throws ServiceException {
    final List<String> oilGaIds = Arrays.asList(DataProvider.OIL_GAIDS);
    final List<String> guids = Arrays.asList("3413983604609849");
    final Page<ArticleDocDto> articles =
        service.searchOilRecommendArticles(userInfo, guids, oilGaIds, Optional.of(this.vehicle),
            Collections.emptyList());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articles));
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  public void testExtractOilGenericArticleIds() {
    final List<String> gaIds = Arrays.asList(DataProvider.OIL_GAIDS);
    final List<String> oilGaIds = service.extractOilGenericArticleIds(gaIds);
    Assert.assertThat(oilGaIds.toArray(new String[0]),
        Matchers.arrayContainingInAnyOrder(DataProvider.OIL_GAIDS));
  }

}
