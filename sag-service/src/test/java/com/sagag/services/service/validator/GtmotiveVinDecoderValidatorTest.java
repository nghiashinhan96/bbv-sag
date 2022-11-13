package com.sagag.services.service.validator;

import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.hazelcast.api.MakeCacheService;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class GtmotiveVinDecoderValidatorTest {

  @InjectMocks
  private GtmotiveVinDecoderValidator validator;

  @Mock
  private GtmotiveService gtmotiveService;

  @Mock
  private MakeCacheService makeCacheService;

  @Test
  public void shouldValidateVinDecoder_True_WithValidVin() {
    final String vin = "zfa19800004361472";
    final String expectedMakeCode = "01";
    final MakeItem expectedMakeItem = MakeItem.builder().gtVin(true).build();
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin(vin);

    Mockito.when(gtmotiveService.getMakeCodeFromVinDecoder(vin)).thenReturn(expectedMakeCode);
    Mockito.when(makeCacheService.findMakeItemByCode(expectedMakeCode))
      .thenReturn(Optional.of(expectedMakeItem));

    boolean actual = validator.validate(criteria);

    Mockito.verify(gtmotiveService, Mockito.times(1)).getMakeCodeFromVinDecoder(vin);
    Mockito.verify(makeCacheService, Mockito.times(1)).findMakeItemByCode(
      expectedMakeCode);
    Assert.assertThat(actual, Matchers.is(true));
    Assert.assertThat(criteria.getMakeCode(), Matchers.is(expectedMakeCode));
  }

  @Test
  public void shouldValidateVinDecoder_False_WithValidVin_ButGtVinFalse() {
    final String vin = "zfa19800004361472";
    final String expectedMakeCode = "01";
    final MakeItem expectedMakeItem = MakeItem.builder().gtVin(false).build();
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin(vin);

    Mockito.when(gtmotiveService.getMakeCodeFromVinDecoder(vin)).thenReturn(expectedMakeCode);
    Mockito.when(makeCacheService.findMakeItemByCode(expectedMakeCode))
      .thenReturn(Optional.of(expectedMakeItem));

    boolean actual = validator.validate(criteria);

    Mockito.verify(gtmotiveService, Mockito.times(1)).getMakeCodeFromVinDecoder(vin);
    Mockito.verify(makeCacheService, Mockito.times(1)).findMakeItemByCode(
      expectedMakeCode);
    Assert.assertThat(actual, Matchers.is(false));
    Assert.assertThat(criteria.getMakeCode(), Matchers.is(expectedMakeCode));
  }

  @Test
  public void shouldValidateVinDecoder_False_WithInValidVin() {
    final String vin = "zfa19800004361472_INVALID";
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin(vin);

    Mockito.when(gtmotiveService.getMakeCodeFromVinDecoder(vin)).thenReturn(StringUtils.EMPTY);
    boolean actual = validator.validate(criteria);

    Mockito.verify(gtmotiveService, Mockito.times(1)).getMakeCodeFromVinDecoder(vin);
    Assert.assertThat(actual, Matchers.is(false));
  }

}
