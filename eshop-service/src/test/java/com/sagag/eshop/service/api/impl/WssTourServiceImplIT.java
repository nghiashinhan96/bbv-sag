package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.WssTourRepository;
import com.sagag.eshop.repo.entity.WssTour;
import com.sagag.eshop.service.api.WssTourService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.WssTourValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.domain.eshop.dto.WssTourDto;
import com.sagag.services.domain.eshop.dto.WssTourTimesDto;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * Integration tests for WSS Tour service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class WssTourServiceImplIT {
  private static final int WHOLE_SALER_ORG_ID_137 = 137;

  @Autowired
  private WssTourService wssTourService;

  @Autowired
  private WssTourRepository wssTourRepo;

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewTour_requestNull() throws WssTourValidationException {
    wssTourService.create(null, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssTourValidationException.class)
  public void testCreateNewTour_NameNull() throws WssTourValidationException {
    wssTourService.create(new WssTourDto(), WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssTourValidationException.class)
  public void testCreateNewTour_duplicateTourTimeWeekDay() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    List<WssTourTimesDto> tourTimesDtos = new ArrayList<WssTourTimesDto>();
    WssTourTimesDto wssTourTimesDto = new WssTourTimesDto();
    wssTourTimesDto.setWeekDay(WeekDay.MONDAY);
    wssTourTimesDto.setDepartureTime("08:00");
    tourTimesDtos.add(wssTourTimesDto);
    tourTimesDtos.add(wssTourTimesDto);
    wssTourDto.setWssTourTimesDtos(tourTimesDtos);
    wssTourService.create(new WssTourDto(), WHOLE_SALER_ORG_ID_137);
  }

  @Test()
  public void testCreateNewTour_EmptyTourTimes() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    WssTourDto createdWssTourDto = wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(createdWssTourDto);
    Assert.assertThat(createdWssTourDto.getName(), Is.is(wssTourDto.getName()));
  }

  @Test()
  public void testCreateNewTour_ValidTourTimes() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    List<WssTourTimesDto> tourTimesDtos = new ArrayList<WssTourTimesDto>();
    WssTourTimesDto wssTourTimesDto = new WssTourTimesDto();
    wssTourTimesDto.setWeekDay(WeekDay.MONDAY);
    wssTourTimesDto.setDepartureTime("08:00");
    tourTimesDtos.add(wssTourTimesDto);
    wssTourDto.setWssTourTimesDtos(tourTimesDtos);
    WssTourDto createdWssTourDto = wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(createdWssTourDto.getName(), Is.is(wssTourDto.getName()));
    Assert.assertNotNull(createdWssTourDto);
  }

  @Test(expected = WssTourValidationException.class)
  public void testCreateNewTour_duplicatedTourName() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
    wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssTourValidationException.class)
  public void testCreateNewTour_TourTimeInvalid() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    List<WssTourTimesDto> tourTimesDtos = new ArrayList<WssTourTimesDto>();
    WssTourTimesDto wssTourTimesDto = new WssTourTimesDto();
    wssTourTimesDto.setWeekDay(WeekDay.MONDAY);
    wssTourTimesDto.setDepartureTime("09:");
    tourTimesDtos.add(wssTourTimesDto);
    wssTourDto.setWssTourTimesDtos(tourTimesDtos);
    wssTourService.create(wssTourDto, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssTourValidationException.class)
  public void testUpdateTour_NotExist() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    wssTourDto.setId(9999);
    wssTourService.update(wssTourDto, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testUpdateExistingTour() throws WssTourValidationException {
    final WssTourDto wssTourDto = initTourData();
    wssTourDto.setId(1);
    final WssTourDto updatedWssTourDto = wssTourService.update(wssTourDto, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(updatedWssTourDto);
    Assert.assertThat(updatedWssTourDto.getName(), Is.is(wssTourDto.getName()));
  }

  @Test(expected = WssTourValidationException.class)
  public void testRemoveTour_NotExist() throws WssTourValidationException {
    wssTourService.remove(9999, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testRemoveTour() throws WssTourValidationException {
    final Integer tourId = 1;

    final Optional<WssTour> existingTour =
        wssTourRepo.findByIdAndOrgId(tourId, WHOLE_SALER_ORG_ID_137);
    if (!existingTour.isPresent()) {
      return;
    }
    Assert.assertThat(existingTour.isPresent(), Is.is(true));

    wssTourService.remove(tourId, WHOLE_SALER_ORG_ID_137);
    final Optional<WssTour> deletedTour =
        wssTourRepo.findByIdAndOrgId(tourId, WHOLE_SALER_ORG_ID_137);

    Assert.assertThat(deletedTour.isPresent(), Is.is(false));
  }

  private WssTourDto initTourData() {
    return WssTourDto.builder().name("Test tour").build();
  }
}
