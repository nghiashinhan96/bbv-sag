package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.VatRateRepository;
import com.sagag.eshop.repo.entity.VatRate;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.common.exception.ValidationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * UT for {@link DefaultVatRateServiceImpl}.
 */
@RunWith(SpringRunner.class)
public class DefaultVatRateServiceImplTest {

  @InjectMocks
  private DefaultVatRateServiceImpl vatRateService;

  @Mock
  private VatRateRepository vatRateRepository;

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
    when(vatRateRepository.findAll()).thenReturn(Arrays.asList(new VatRate()));
    List<VatRateDto> result = vatRateService.getAll();

    verify(vatRateRepository, times(1)).findAll();
    Assert.assertEquals(result.size(), 1);
  }
}
