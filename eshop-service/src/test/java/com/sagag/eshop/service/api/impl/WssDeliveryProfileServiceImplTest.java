package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.WssBranchRepository;
import com.sagag.eshop.repo.api.WssDeliveryProfileRepository;
import com.sagag.eshop.repo.api.WssDeliveryProfileToursRepository;
import com.sagag.eshop.repo.api.WssTourRepository;
import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.repo.entity.WssDeliveryProfile;
import com.sagag.eshop.repo.entity.WssDeliveryProfileTours;
import com.sagag.eshop.repo.entity.WssTour;
import com.sagag.eshop.service.exception.WssDeliveryProfileValidationException;
import com.sagag.services.common.enums.WssDeliveryProfileDay;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileRequestDto;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Test class for WSS Delivery Profile Service.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WssDeliveryProfileServiceImplTest {

  private static final int WHOLE_SALER_ORG_ID_137 = 137;

  @Mock
  private WssTourRepository wssTourRepo;

  @Mock
  private WssBranchRepository wssBranchRepo;

  @Mock
  private CustomerSettingsRepository customerSettingsRepo;

  @InjectMocks
  private WssDeliveryProfileServiceImpl wssDeliveryProfileService;

  @Mock
  private WssDeliveryProfileRepository wssDeliveryProfileRepo;

  @Mock
  private WssDeliveryProfileToursRepository wssDeliveryProfileToursRepo;

  @Test
  public void testCreateDeliveryProfile() throws WssDeliveryProfileValidationException {
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    Mockito.when(wssBranchRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(new WssBranch()));
    Mockito.when(wssTourRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(new WssTour()));

    wssDeliveryProfileService.createWssDeliveryProfile(request, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).save(Mockito.any(WssDeliveryProfile.class));
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testCreateDeliveryProfile_NotExistBranch()
      throws WssDeliveryProfileValidationException {
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    Mockito.when(wssBranchRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.empty());
    Mockito.when(wssDeliveryProfileRepo.checkExistDeliveryProfileNameByOrgId(Mockito.anyString(),
        Mockito.anyInt())).thenReturn(false);

    wssDeliveryProfileService.createWssDeliveryProfile(request, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).save(Mockito.any(WssDeliveryProfile.class));
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testCreateDeliveryProfile_NotExistTour()
      throws WssDeliveryProfileValidationException {
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    Mockito.when(wssBranchRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(new WssBranch()));
    Mockito.when(wssTourRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.empty());

    wssDeliveryProfileService.createWssDeliveryProfile(request, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).save(Mockito.any(WssDeliveryProfile.class));
  }

  @Test
  public void testUpdateDeliveryProfile() throws WssDeliveryProfileValidationException {
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    request.setId(1);
    WssDeliveryProfile deliveryProfile = initWssDeliveryProfileEntityData();
    Mockito.when(wssBranchRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(new WssBranch()));
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(deliveryProfile));

    wssDeliveryProfileService.updateWssDeliveryProfile(request, WHOLE_SALER_ORG_ID_137);

    Mockito.verify(wssDeliveryProfileRepo, times(1)).save(deliveryProfile);
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testUpdateDeliveryProfile_NotExistBranch()
      throws WssDeliveryProfileValidationException {
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    request.setId(1);
    WssDeliveryProfile deliveryProfile = initWssDeliveryProfileEntityData();
    Mockito.when(wssBranchRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.empty());
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(deliveryProfile));

    wssDeliveryProfileService.updateWssDeliveryProfile(request, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).save(Mockito.any(WssDeliveryProfile.class));
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testRemoveWssDeliveryProfile_NotExist() throws WssDeliveryProfileValidationException {
    final int wssDeliveryProfileId = 1;
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.empty());
    wssDeliveryProfileService.removeWssDeliveryProfile(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).deleteById(Mockito.any());
  }

  @Test
  public void testRemoveWssDeliveryProfile() throws WssDeliveryProfileValidationException {
    WssDeliveryProfile deliveryProfile = initWssDeliveryProfileEntityData();
    final int wssDeliveryProfileId = 1;
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(deliveryProfile));
    Mockito.when(customerSettingsRepo.checkDeliveryProfileBeingUsed(Mockito.anyInt()))
    .thenReturn(false);

    wssDeliveryProfileService.removeWssDeliveryProfile(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).deleteById(Mockito.any());
  }

  @Test
  public void testAddTourWssDeliveryProfile() throws WssDeliveryProfileValidationException {
    WssDeliveryProfile deliveryProfile = initWssDeliveryProfileEntityData();
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    final int wssDeliveryProfileId = 1;
    request.setId(wssDeliveryProfileId);
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(deliveryProfile));
    Mockito.when(wssTourRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(new WssTour()));

    wssDeliveryProfileService.addWssDeliveryProfileTour(request, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileToursRepo, times(1))
        .save(Mockito.any(WssDeliveryProfileTours.class));
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testAddTourWssDeliveryProfile_NotExistTour()
      throws WssDeliveryProfileValidationException {
    WssDeliveryProfile deliveryProfile = initWssDeliveryProfileEntityData();
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    final int wssDeliveryProfileId = 1;
    request.setId(wssDeliveryProfileId);
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(deliveryProfile));

    wssDeliveryProfileService.addWssDeliveryProfileTour(request, WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileToursRepo, times(1))
        .save(Mockito.any(WssDeliveryProfileTours.class));
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testRemoveTourWssDeliveryProfile_NotExistDeliveryProfileTour()
      throws WssDeliveryProfileValidationException {
    WssDeliveryProfileRequestDto request = initWssDeliveryProfileRequestData();
    final int wssDeliveryProfileId = 1;
    final int wssDeliveryProfileTourId = 1;
    request.setId(wssDeliveryProfileId);

    wssDeliveryProfileService.removeWssDeliveryProfileTour(wssDeliveryProfileTourId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileToursRepo, times(1))
        .delete(Mockito.any(WssDeliveryProfileTours.class));
  }

  @Test(expected = WssDeliveryProfileValidationException.class)
  public void testGetWssDeliveryProfile_NotExistDeliveryProfile()
      throws WssDeliveryProfileValidationException {
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.empty());

    final int wssDeliveryProfileId = 1;

    wssDeliveryProfileService.getWssDeliveryProfileDetail(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testGetWssDeliveryProfile() throws WssDeliveryProfileValidationException {
    Mockito.when(wssDeliveryProfileRepo.findByIdAndOrgId(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(Optional.of(initWssDeliveryProfileEntityData()));

    final int wssDeliveryProfileId = 1;

    WssDeliveryProfileDto wssDeliveryProfileDetail = wssDeliveryProfileService
        .getWssDeliveryProfileDetail(wssDeliveryProfileId, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(wssDeliveryProfileDetail);
    Mockito.verify(wssDeliveryProfileRepo, times(1)).findByIdAndOrgId(wssDeliveryProfileId,
        WHOLE_SALER_ORG_ID_137);
  }

  private WssDeliveryProfileRequestDto initWssDeliveryProfileRequestData() {
    return WssDeliveryProfileRequestDto.builder().name("test create DP")
        .description("DP Description").wssBranchId(1).supplierTourDay(WssDeliveryProfileDay.ALL)
        .supplierDepartureTime("08:00").isOverNight(true).wssTourId(1).pickupWaitDuration(0)
        .build();
  }

  private WssDeliveryProfile initWssDeliveryProfileEntityData() {
    return WssDeliveryProfile.builder().id(1).name("DP Exist").description("DP Description")
        .wssDeliveryProfileTours(new ArrayList<WssDeliveryProfileTours>())
        .wssBranch(new WssBranch()).build();
  }
}
