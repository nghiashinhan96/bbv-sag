package com.sagag.services.ivds.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.api.IvdsGtmotiveSearchService;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import com.sagag.services.ivds.request.vehicle.GTmotiveDirectMatches;
import com.sagag.services.ivds.response.GtmotiveResponse;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class IvdsGtmotiveSearchServiceImplIT extends BaseSearchServiceImplIT {

  @Autowired
  private IvdsGtmotiveSearchService service;

  @Test
  public void shouldSearchArticlesByGtOperations() {
    GtmotiveOperationRequest request = DataProvider.gtmotiveOperationRequest();
    GtmotiveResponse result = service.searchArticlesByGtOperations(user, request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(result));
  }

  @Test
  public void testDirectMatchesArticlesFilledToTheResponse() {
    GtmotiveOperationRequest request = DataProvider.gtmotiveOperationRequestDirectMatchesCase();
    GtmotiveResponse result = service.searchArticlesByGtOperations(user, request);
    Assert.assertNotNull(result);
    Page<GTmotiveDirectMatches> directMatches = result.getDirectMatches();
    Assert.assertNotNull(directMatches);
    Assert.assertEquals(2, directMatches.getContent().size());
    List<String> expectedReference = directMatches.stream().map(GTmotiveDirectMatches::getReference)
        .collect(Collectors.toList());
    String reference1 = "1K0199867Q";
    String reference2 = "1K0199868Q";
    Assert.assertEquals(true, expectedReference.contains(reference1));
    Assert.assertEquals(true, expectedReference.contains(reference2));
    Assert.assertEquals(0, result.getNonMatchedOperations().size());
  }
}
