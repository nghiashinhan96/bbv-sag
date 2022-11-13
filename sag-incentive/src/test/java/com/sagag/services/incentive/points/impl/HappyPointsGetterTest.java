package com.sagag.services.incentive.points.impl;

import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProperties;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class HappyPointsGetterTest {

  @InjectMocks
  private HappyPointsGetter getter;

  @Mock
  private IncentiveProperties incentiveProps;

  @Mock
  private IncentivePointProperties props;

  @Mock
  private RestTemplate incentiveRestTemplate;

  @Before
  public void setUp() throws Exception {
    Mockito.when(incentiveProps.getHappyPoints()).thenReturn(props);
    Mockito.when(props.getUrl())
    .thenReturn("http://dd.jillix.net/@/api/happybonus/accounts/%s/points");
  }

  @Test
  public void testBuildSucceedHappyPoints() {
    final String custNr = "235310";
    final long expectedPoints = 10l;

    Mockito.when(incentiveRestTemplate.getForEntity(
        Mockito.eq("http://dd.jillix.net/@/api/happybonus/accounts/235310/points"),
        Mockito.eq(Long.class)))
    .thenReturn(ResponseEntity.ok(expectedPoints));

    final long result = getter.get(custNr);

    Assert.assertThat(result, Matchers.is(expectedPoints));
  }

  @Test(expected = RestClientException.class)
  public void testBuildFailedHappyPoints() {
    final String custNr = "1111";
    Mockito.when(incentiveRestTemplate.getForEntity(
        Mockito.eq("http://dd.jillix.net/@/api/happybonus/accounts/1111/points"),
        Mockito.eq(Long.class)))
    .thenThrow(RestClientException.class);

    getter.get(custNr);
  }
}
