package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.common.exception.ValidationException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * UT for {@link EmptyVatRateServiceImplTest}.
 */
@RunWith(SpringRunner.class)
public class EmptyVatRateServiceImplTest {

  @InjectMocks
  private EmptyVatRateServiceImpl vatRateService;


  /**
   * Sets up the pre-condition for testing.
   *
   * @throws Exception throws when program fails.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetAll() throws ValidationException {
    List<VatRateDto> result = vatRateService.getAll();
    Assert.assertThat(result.size(), Matchers.is(0));
  }

}
