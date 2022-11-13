package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.WssTourRepository;
import com.sagag.eshop.repo.entity.WssTour;
import com.sagag.eshop.service.converter.WssTourConverters;
import com.sagag.eshop.service.exception.WssTourValidationException;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.domain.eshop.dto.WssTourDto;
import com.sagag.services.domain.eshop.dto.WssTourTimesDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Test class for WSS Tour Service.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WssTourServiceImplTest {

  private static final int WHOLE_SALER_ORG_ID_137 = 137;

  @InjectMocks
  private WssTourServiceImpl wssTourService;

  @Mock
  private WssTourRepository wssTourRepo;

  @Test(expected = WssTourValidationException.class)
  public void testCreateWssTour_AlreadyExist() throws WssTourValidationException {
    WssTourDto wssTourDto = initTourData();
    WssTour wssTour = WssTourConverters.convertFromDto(wssTourDto);;
    wssTour.setId(1);
    wssTour.setOrgId(WHOLE_SALER_ORG_ID_137);
    Mockito.when(wssTourRepo.checkExistingTourByNameAndOrgId(Mockito.anyString(), Mockito.anyInt()))
        .thenReturn(true);

    wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssTourRepo, times(1)).checkExistingTourByNameAndOrgId(wssTourDto.getName(),
        WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssTourValidationException.class)
  public void testCreateWssTour_InvalidTourTime() throws WssTourValidationException {
    WssTourDto wssTourDto = initTourData();
    WssTour wssTour = WssTourConverters.convertFromDto(wssTourDto);;
    wssTour.setId(1);
    wssTour.setOrgId(WHOLE_SALER_ORG_ID_137);
    List<WssTourTimesDto> tourTimesDtos = new ArrayList<WssTourTimesDto>();
    WssTourTimesDto wssTourTimesDto = new WssTourTimesDto();
    wssTourTimesDto.setWeekDay(WeekDay.MONDAY);
    wssTourTimesDto.setDepartureTime("09:");
    tourTimesDtos.add(wssTourTimesDto);
    wssTourDto.setWssTourTimesDtos(tourTimesDtos);
    Mockito.when(wssTourRepo.checkExistingTourByNameAndOrgId(Mockito.anyString(), Mockito.anyInt()))
        .thenReturn(false);

    wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssTourRepo, times(1)).checkExistingTourByNameAndOrgId(wssTourDto.getName(),
        WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssTourValidationException.class)
  public void testUpdateWssTour_NotExist() throws WssTourValidationException {
    WssTourDto wssTourDto = initTourData();
    wssTourDto.setId(1);
    Mockito.when(wssTourRepo.checkExistingTourByIdAndNameAndOrgId(Mockito.anyInt(),
        Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
    wssTourService.update(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssTourRepo, times(1)).checkExistingTourByIdAndNameAndOrgId(wssTourDto.getId(),
        wssTourDto.getName(), wssTourDto.getOrgId());
  }

  @Test(expected = WssTourValidationException.class)
  public void testUpdateWssTour_InvalidTourTime() throws WssTourValidationException {
    WssTourDto wssTourDto = initTourData();
    WssTour wssTour = WssTourConverters.convertFromDto(wssTourDto);;
    wssTour.setId(1);
    List<WssTourTimesDto> tourTimesDtos = new ArrayList<WssTourTimesDto>();
    WssTourTimesDto wssTourTimesDto = new WssTourTimesDto();
    wssTourTimesDto.setWeekDay(WeekDay.MONDAY);
    wssTourTimesDto.setDepartureTime("09:");
    tourTimesDtos.add(wssTourTimesDto);
    wssTourDto.setWssTourTimesDtos(tourTimesDtos);

    wssTourService.update(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssTourRepo, times(1)).checkExistingTourByIdAndNameAndOrgId(wssTourDto.getId(),
        wssTourDto.getName(), wssTourDto.getOrgId());
  }


  @Test(expected = WssTourValidationException.class)
  public void testRemoveWssTour_NotExist() throws WssTourValidationException {
    final int wssTourId = 1;
    Mockito.when(wssTourRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.empty());
    wssTourService.remove(wssTourId, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssTourRepo, times(1)).findByIdAndOrgId(wssTourId, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testRemoveWssTour() throws WssTourValidationException {
    final int wssTourId = 1;
    Mockito.when(wssTourRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(new WssTour()));
    wssTourService.remove(wssTourId, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssTourRepo, times(1)).findByIdAndOrgId(wssTourId, WHOLE_SALER_ORG_ID_137);
  }

  private WssTourDto initTourData() {
    return WssTourDto.builder().name("Test tour").orgId(WHOLE_SALER_ORG_ID_137).build();
  }
}
