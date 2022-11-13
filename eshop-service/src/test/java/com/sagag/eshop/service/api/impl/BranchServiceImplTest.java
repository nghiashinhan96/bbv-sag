package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.BranchOpeningTimeRepository;
import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.api.CountryRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.service.converter.BranchConverters;
import com.sagag.eshop.service.exception.BranchValidationException;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Test class for Branch Service.
 *
 */
@RunWith(SpringRunner.class)
public class BranchServiceImplTest {

  @InjectMocks
  private BranchServiceImpl branchService;

  @Mock
  private BranchRepository branchRepository;

  @Mock
  private CountryRepository countryRepository;

  @Mock
  private BranchOpeningTimeRepository branchOpeningTimeRepo;

  @Test(expected = BranchValidationException.class)
  public void testCreateBranchAlreadyExit() throws BranchValidationException {
    BranchRequestBody request = initRequestBody();
    Mockito.when(branchRepository.checkExistingBranchByBranchNr(Mockito.anyInt())).thenReturn(true);

    branchService.create(request);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(request.getBranchNr());
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateBranchNotHaveOpeningTime() throws BranchValidationException {
    BranchRequestBody request = initRequestBody();
    request.setBranchOpeningTimes(Collections.emptyList());
    Mockito.when(branchRepository.checkExistingBranchByBranchNr(Mockito.anyInt())).thenReturn(true);

    branchService.create(request);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(request.getBranchNr());
  }

  @Test(expected = BranchValidationException.class)
  public void testUpdateBranchNotHaveOpeningTime() throws BranchValidationException {
    BranchRequestBody request = initRequestBody();
    request.setBranchOpeningTimes(Collections.emptyList());
    Mockito.when(branchRepository.checkExistingBranchByBranchNr(Mockito.anyInt())).thenReturn(true);

    branchService.update(request);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(request.getBranchNr());
  }

  @Test
  public void testCreateBranch() throws BranchValidationException {
    final BranchRequestBody request = initRequestBody();
    final Branch branch = BranchConverters.convertFromRequest(request);
    request.setBranchNr(1234);
    Mockito.when(branchRepository.checkExistingBranchByBranchNr(Mockito.anyInt())).thenReturn(false);
    Mockito.when(branchRepository.checkExistingBranchByBranchCode(Mockito.anyString())).thenReturn(false);
    Mockito.when(branchRepository.save(Mockito.any())).thenReturn(branch);
    branchService.create(request);
    Mockito.verify(branchRepository, times(1)).checkExistingBranchByBranchNr(request.getBranchNr());
    Mockito.verify(branchRepository, times(1)).checkExistingBranchByBranchCode(request.getBranchCode());
    Mockito.verify(branchRepository, times(1)).save(Mockito.any());
  }

  @Test(expected = BranchValidationException.class)
  public void testUpdateBranchNotExit() throws BranchValidationException {
    BranchRequestBody request = initRequestBody();
    branchService.update(request);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(request.getBranchNr());
  }

  @Test
  public void testUpdateBranch() throws BranchValidationException {
    final BranchRequestBody request = initRequestBody();
    final Branch branch = BranchConverters.convertFromRequest(request);
    final int existingBranchId = 1;
    branch.setId(existingBranchId);
    Mockito.when(branchRepository.findIdByBranchNr(Mockito.anyInt())).thenReturn(Optional.of(existingBranchId));
    Mockito.when(branchRepository.save(Mockito.any())).thenReturn(branch);

    branchService.update(request);

    Mockito.verify(branchRepository, times(1)).findIdByBranchNr(request.getBranchNr());
    Mockito.verify(branchRepository, times(1)).save(branch);
  }

  @Test(expected = BranchValidationException.class)
  public void testRemoveBranchNotExit() throws BranchValidationException {
    final int branchNr = 1234;
    Mockito.when(branchRepository.findOneByBranchNr(Mockito.anyInt())).thenReturn(Optional.empty());
    branchService.remove(branchNr);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(branchNr);
  }

  @Test
  public void testRemoveBranch() throws BranchValidationException {
    final int branchNr = 1234;
    Mockito.when(branchRepository.findOneByBranchNr(Mockito.anyInt()))
        .thenReturn(Optional.of(new Branch()));
    branchService.remove(branchNr);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(branchNr);
    Mockito.verify(branchRepository, times(1)).delete(new Branch());
  }

  @Test
  public void testGetBranchDetailNotExit() throws BranchValidationException {
    final int branchNr = 1234;
    Mockito.when(branchRepository.findOneByBranchNr(Mockito.anyInt())).thenReturn(Optional.empty());
    branchService.getBranchDetail(branchNr);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(branchNr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBranchDetailNullBranchNr() throws BranchValidationException {
    final Integer branchNr = null;
    branchService.getBranchDetail(branchNr);
  }

  @Test
  public void testGetBranchDetail() throws BranchValidationException {
    final Branch branch = BranchConverters.convertFromRequest(initRequestBody());
    final int branchNr = branch.getBranchNr();
    Mockito.when(branchRepository.findOneByBranchNr(Mockito.anyInt()))
        .thenReturn(Optional.of(branch));
    branchService.getBranchDetail(branchNr);
    Mockito.verify(branchRepository, times(1)).findOneByBranchNr(branchNr);
  }

  @Test
  public void testGetBranchList() {
    Mockito.when(branchRepository.findAll())
        .thenReturn(Collections.singletonList(initMockBranch()));
    branchService.getBranches();
    Mockito.verify(branchRepository, times(1)).findAll();
  }

  @Test(expected = BranchValidationException.class)
  public void testGetBranchListByCountryNotFound() throws BranchValidationException {
    branchService.getBranchesByCountry("abc");
  }

  @Test
  public void testGetBranchListByCountry() throws BranchValidationException {
    Mockito.when(countryRepository.findIdsByShortCode(Mockito.anyString()))
        .thenReturn(Collections.singletonList(17));
    Mockito.when(branchRepository.findByCountries(Mockito.any()))
        .thenReturn(Collections.singletonList(initMockBranch()));
    branchService.getBranchesByCountry("at");
    Mockito.verify(countryRepository, times(1)).findIdsByShortCode(Mockito.anyString());
    Mockito.verify(branchRepository, times(1)).findByCountries(Mockito.any());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBranchListByCountryNull() throws BranchValidationException {
    branchService.getBranchesByCountry(null);
  }

  private BranchRequestBody initRequestBody() {
    return BranchRequestBody.builder().branchNr(1234)
        .branchCode("LEO")
        .orgId(10003)
        .zip("4060")
        .countryId(17)
        .addressStreet("Edtstrasse 13")
        .addressCity("Leonding")
        .addressDesc("Filiale Leonding")
        .addressCountry("Österreich")
        .regionId("AUT")
        .primaryFax("+43 732 678 181 200")
        .primaryEmail("filleo@derendinger.at")
        .primaryPhone("+43 732 678 181")
        .primaryUrl("www.derendinger.at")
        .validForKSL(true)
        .openingTime("07:30:00")
        .closingTime("17:00:00")
        .lunchStartTime("10:00:00")
        .lunchEndTime("13:00:00")
        .branchOpeningTimes(Arrays.asList(BranchOpeningTimeDto.builder().openingTime("07:30:00")
            .closingTime("17:00:00").weekDay(WeekDay.MONDAY).build()))
        .build();
  }

  private Branch initMockBranch() {
    return Branch.builder().id(1).hideFromCustomers(false).hideFromSales(false)
        .build();
  }
}
