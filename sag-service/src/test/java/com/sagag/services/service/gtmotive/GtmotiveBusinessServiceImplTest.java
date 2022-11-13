package com.sagag.services.service.gtmotive;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.service.api.impl.GtmotiveBusinessServiceImpl;
import com.sagag.services.service.validator.GtmotiveVinDecoderValidator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests class for GTMotive Business Services.
 */
@RunWith(MockitoJUnitRunner.class)
public class GtmotiveBusinessServiceImplTest {

  @InjectMocks
  private GtmotiveBusinessServiceImpl service;

  @Mock
  private GtmotiveService gtmotiveService;

  @Mock
  private GtmotiveCriteria criteria;

  @Mock
  private UserInfo user;

  @Mock
  private GtmotiveVinDecoderValidator gtmotiveVinDecoderValidator;

  @Before
  public void init() {
  }

  @After
  public void after() {
  }

  @Test
  public void shouldGetGraphicalFrameInfo() throws Exception {

  }

}
