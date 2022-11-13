package com.sagag.services.service.utils;

import static org.mockito.Mockito.when;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.ivds.response.GtmotiveResponse;

import lombok.Getter;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

/**
 * Unit test class for Gtmotive Helper.
 */
@RunWith(MockitoJUnitRunner.class)
public class NormautoMailBuilderTest {


  @InjectMocks
  private NormautoMailBuilder builder;

  @Mock
  private GtmotiveCriteria criteria;

  @Mock
  private GtmotiveResponse response;

  @Mock
  @Getter
  private UserInfo user;

  @Test
  public void testBuildNormautoMailContentWithVehicle() throws Exception {

    when(response.getNonMatchedOperations()).thenReturn(Collections.emptyList());

    final String mailContent = builder.build(getUser(), criteria.getModifiedVin(),
        criteria.getVehicleCode(), response);

    Assert.assertThat(mailContent, Matchers.notNullValue());
  }

  @Test
  public void testBuildNormautoMailContentWithoutVehicle() throws Exception {

    when(response.getNonMatchedOperations()).thenReturn(Collections.emptyList());
    final String mailContent = builder.build(getUser(), criteria.getModifiedVin(),
        criteria.getVehicleCode(), response);

    Assert.assertThat(mailContent, Matchers.notNullValue());
  }
}
