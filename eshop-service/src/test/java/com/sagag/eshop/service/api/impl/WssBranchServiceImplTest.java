package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.WssBranchOpeningTimeRepository;
import com.sagag.eshop.repo.api.WssBranchRepository;
import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.repo.entity.WssDeliveryProfile;
import com.sagag.eshop.service.exception.WssBranchValidationException;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.branch.dto.WssBranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.WssBranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssBranchDto;
import com.sagag.services.domain.eshop.dto.WssBranchOpeningTimeDto;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for WSS branch service.
 */
@RunWith(SpringRunner.class)
public class WssBranchServiceImplTest {
  private static final int WHOLE_SALER_ORG_ID_137 = 137;

  @InjectMocks
  private WssBranchServiceImpl wssBranchService;

  @Mock
  private WssBranchRepository wssBranchRepository;

  @Mock
  private WssBranchOpeningTimeRepository wssBranchOpeningTimeRepo;

  @Test
  public void testSearchByBranchNr() {
    WssBranch wssBranch = WssBranch.builder().branchNr(1001).build();
    when(wssBranchRepository.findOneByBranchNrAndOrgId(1001, WHOLE_SALER_ORG_ID_137))
        .thenReturn(Optional.of(wssBranch));
    final Optional<WssBranch> branchDoc =
        wssBranchService.searchByBranchNr(1001, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(branchDoc.isPresent(), Is.is(true));
    Assert.assertThat(branchDoc.get().getBranchNr(), Is.is(1001));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchByBranchNr_expectException() {
    wssBranchService.searchByBranchNr(null, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewBranch_requestNull() throws WssBranchValidationException {
    wssBranchService.create(null, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_BranchNrNull() throws WssBranchValidationException {
    wssBranchService.create(new WssBranchRequestBody(), WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_InvalidBranchCodeLength() throws WssBranchValidationException {
    WssBranchRequestBody request =
        WssBranchRequestBody.builder().branchCode("ABCDEFGHIKLM").build();
    wssBranchService.create(request, WHOLE_SALER_ORG_ID_137);
  }
  

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranchNotExistOpeningTime() throws WssBranchValidationException {
    WssBranchRequestBody request =
        WssBranchRequestBody.builder().branchNr(1234).branchCode("ABCDEFGHIKLM").build();
    wssBranchService.create(request, WHOLE_SALER_ORG_ID_137);
  }
  
  @Test(expected = WssBranchValidationException.class)
  public void testUpdateBranchNotExistOpeningTime() throws WssBranchValidationException {
    WssBranchRequestBody request =
        WssBranchRequestBody.builder().branchNr(1234).branchCode("ABCDEFGHIKLM").build();
    wssBranchService.update(request, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_DuplicatedWeekDay() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    WssBranchOpeningTimeDto weekDay1 =
        WssBranchOpeningTimeDto.builder().weekDay(WeekDay.FRIDAY).build();
    WssBranchOpeningTimeDto weekDay2 = weekDay1;
    branchRequest.setWssBranchOpeningTimes(Lists.newArrayList(weekDay1, weekDay2));
    branchRequest.setBranchNr(1001);
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_OutOfRange() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    WssBranchOpeningTimeDto weekDay =
        WssBranchOpeningTimeDto.builder().weekDay(WeekDay.MONDAY).openingTime("08:00")
            .closingTime("17:00").lunchStartTime("13:30").lunchEndTime("01:00").build();
    List<WssBranchOpeningTimeDto> wssBranchOpeningTimes = Lists.newArrayList(weekDay);
    branchRequest.setWssBranchOpeningTimes(wssBranchOpeningTimes);
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testUpdateBranch_NotExist() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setBranchNr(8888);
    wssBranchService.update(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testRemoveBranch_NotExist() throws WssBranchValidationException {
    wssBranchService.remove(8888, null);
  }

  @Test
  public void testRemoveBranch() throws WssBranchValidationException {
    final Integer branchNr = 1001;

    when(wssBranchRepository.findOneByBranchNrAndOrgId(branchNr, WHOLE_SALER_ORG_ID_137))
        .thenReturn(Optional.of(initWssBranch()));

    doNothing().when(wssBranchRepository).delete(Mockito.any(WssBranch.class));
    doNothing().when(wssBranchOpeningTimeRepo).deleteAllByWssBranch(Mockito.any(WssBranch.class));

    wssBranchService.remove(branchNr, WHOLE_SALER_ORG_ID_137);

    verify(wssBranchOpeningTimeRepo, times(1)).deleteAllByWssBranch(Mockito.any(WssBranch.class));
    verify(wssBranchRepository, times(1)).delete(Mockito.any(WssBranch.class));
  }

  @Test(expected = WssBranchValidationException.class)
  public void testRemoveBranch_expectError() throws WssBranchValidationException {
    final Integer branchNr = 1001;

    final WssBranch branch = initWssBranch();
    branch.setWssDeliveryProfile(Lists.newArrayList(new WssDeliveryProfile()));
    when(wssBranchRepository.findOneByBranchNrAndOrgId(branchNr, WHOLE_SALER_ORG_ID_137))
        .thenReturn(Optional.of(branch));

    wssBranchService.remove(branchNr, WHOLE_SALER_ORG_ID_137);

    verify(wssBranchRepository, times(1)).findOneByBranchNrAndOrgId(branchNr,
        WHOLE_SALER_ORG_ID_137);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testSearchBranchByCriteria_FullOpeningHour() {
    final WssBranchSearchRequestCriteria criteria = WssBranchSearchRequestCriteria.builder()
        .openingTime("08:00").orgId(WHOLE_SALER_ORG_ID_137).build();
    Page<WssBranch> result = new PageImpl<>(Lists.newArrayList(initWssBranch()));
    when(wssBranchRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
        .thenReturn(result);
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageUtils.MAX_PAGE);
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchBranchByCriteria_NullCriteria() {
    wssBranchService.searchBranchByCriteria(null, PageRequest.of(0, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBranchDetail_NullBranchNr() throws WssBranchValidationException {
    wssBranchService.getBranchDetail(null, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testGetBranchDetail_NotFound() throws WssBranchValidationException {
    Optional<WssBranchDto> branchDetail =
        wssBranchService.getBranchDetail(1234, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(branchDetail.isPresent(), Is.is(false));
  }

  @Test
  public void testGetBranchDetail() throws WssBranchValidationException {
    final int branchNr = 1001;
    WssBranch branch = initWssBranch();
    branch.setBranchNr(branchNr);
    when(wssBranchRepository.findOneByBranchNrAndOrgId(branchNr, WHOLE_SALER_ORG_ID_137))
        .thenReturn(Optional.of(branch));
    final Optional<WssBranchDto> branchDetail =
        wssBranchService.getBranchDetail(branchNr, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(branchDetail.isPresent(), Is.is(true));
    Assert.assertThat(branchDetail.get().getBranchNr(), Is.is(branchNr));
  }

  @Test
  public void testGetBranchList_expectEmpty() {
    final List<WssBranchDto> branches =
        wssBranchService.getBranchesByOrganisation(WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(CollectionUtils.isEmpty(branches), Is.is(true));
  }

  private void assertSearchResult(final Page<WssBranchDto> wssBranchPage, final Long expected) {
    Assert.assertThat(wssBranchPage, Matchers.notNullValue());
    Assert.assertThat(wssBranchPage.getContent(), Matchers.notNullValue());
    if (wssBranchPage.hasContent()) {
      Assert.assertThat(wssBranchPage.getTotalElements(), Is.is(expected));
    }
  }

  private WssBranchRequestBody initBranchData() {
    return WssBranchRequestBody.builder().branchNr(12345678).branchCode("LEOTEST")
        .openingTime("07:30").closingTime("17:00").lunchStartTime("10:00").lunchEndTime("13:00")
        .wssBranchOpeningTimes(Collections.emptyList()).build();
  }

  private WssBranch initWssBranch() {
    return WssBranch.builder().build();
  }
}
