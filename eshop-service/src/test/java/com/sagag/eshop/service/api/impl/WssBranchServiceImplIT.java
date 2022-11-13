package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.WssBranchRepository;
import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.service.api.WssBranchService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.WssBranchValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.branch.dto.WssBranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.WssBranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssBranchDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * Integration tests for WSS branch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Need maintain with IT DB")
public class WssBranchServiceImplIT {
  private static final int WHOLE_SALER_ORG_ID_137 = 137;

  @Autowired
  private WssBranchService wssBranchService;

  @Autowired
  private WssBranchRepository branchRepository;

  @Test
  public void testSearchByBranchNr() {
    final Optional<WssBranch> branchDoc = wssBranchService.searchByBranchNr(1001, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(branchDoc.isPresent(), Is.is(true));
    Assert.assertThat(branchDoc.get().getBranchNr(), Is.is(1001));
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
  public void testCreateNewBranch_OpeningTimeNull() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setOpeningTime(null);
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_InvalidBranchCodeLength() throws WssBranchValidationException {
    WssBranchRequestBody request = WssBranchRequestBody.builder().branchCode("ABCDEFGHIKLM").build();
    wssBranchService.create(request, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_OpeningTimeInvalid() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setOpeningTime("09:");
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_ClosingTimeInvalid() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setClosingTime("77:00");
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_TimeInvalid() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setClosingTime("abc");
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_DuplicatedBranchNr() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setBranchNr(1001);
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_StartAfterEndTime() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setClosingTime("07:30:00");
    branchRequest.setOpeningTime("17:30:00");
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_StartAfterEndLunchTime() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setLunchEndTime("10:00:00");
    branchRequest.setLunchStartTime("13:30:00");
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test(expected = WssBranchValidationException.class)
  public void testCreateNewBranch_OutOfRange() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setLunchEndTime("01:00:00");
    branchRequest.setLunchStartTime("13:30:00");
    wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testCreateNewBranch() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    final WssBranchDto branchDto = wssBranchService.create(branchRequest, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(branchDto);
    Assert.assertNotNull(branchDto.getId());
    Assert.assertThat(branchDto.getBranchNr(), Is.is(branchRequest.getBranchNr()));
    Assert.assertThat(branchDto.getBranchCode(), Is.is(branchRequest.getBranchCode()));
  }

  @Test(expected = WssBranchValidationException.class)
  public void testUpdateBranch_NotExist() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setBranchNr(8888);
    wssBranchService.update(branchRequest, WHOLE_SALER_ORG_ID_137);
  }

  @Test
  public void testUpdateExistingBranch() throws WssBranchValidationException {
    final WssBranchRequestBody branchRequest = initBranchData();
    branchRequest.setBranchNr(1001);
    final WssBranchDto branchDto = wssBranchService.update(branchRequest, WHOLE_SALER_ORG_ID_137);
    Assert.assertNotNull(branchDto);
    Assert.assertThat(branchDto.getBranchNr(), Is.is(branchRequest.getBranchNr()));
  }

  @Test(expected = WssBranchValidationException.class)
  public void testRemoveBranch_NotExist() throws WssBranchValidationException {
    wssBranchService.remove(8888, null);
  }

  @Test
  public void testRemoveBranch() throws WssBranchValidationException {
    final Integer branchNr = 1001;

    final Optional<WssBranch> existingBranch = wssBranchService.searchByBranchNr(branchNr, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(existingBranch.isPresent(), Is.is(true));

    wssBranchService.remove(branchNr, WHOLE_SALER_ORG_ID_137);
    final Optional<WssBranch> deletedBranch = branchRepository.findOneByBranchNrAndOrgId(branchNr, WHOLE_SALER_ORG_ID_137);

    Assert.assertThat(deletedBranch.isPresent(), Is.is(false));
  }

  @Test
  public void testSearchBranchByCriteria_EmptyFilter() {
    final WssBranchSearchRequestCriteria criteria = new WssBranchSearchRequestCriteria();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 5L);
  }

  @Test
  public void testSearchBranchByCriteria_BranchCode() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().branchCode("W").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 2L);
  }

  @Test
  public void testSearchBranchByCriteria_FullBranchCode() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().branchCode("BAM").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_BranchNr() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().branchNr("01").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_FullBranchNr() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().branchNr("1001").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_FullOpeningHour() {
    final WssBranchSearchRequestCriteria criteria = WssBranchSearchRequestCriteria.builder()
        .openingTime("08:00").orgId(WHOLE_SALER_ORG_ID_137).build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageUtils.MAX_PAGE);
    assertSearchResult(wssBranchPage,2L);
  }

  @Test
  public void testSearchBranchByCriteria_FullClosingTime() {
    final WssBranchSearchRequestCriteria criteria = WssBranchSearchRequestCriteria.builder()
        .closingTime("17:22").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_FullLunchStartTime() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().lunchStartTime("11:00").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_FullLunchEndTime() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().lunchEndTime("13:00").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_EmptyResult() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().branchCode("ABC").openingTime("22").build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 0L);
  }

  @Test
  public void testSearchBranchByCriteria_Space() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().branchCode(StringUtils.SPACE).build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 5L);
  }

  @Test
  public void testSearchBranchByCriteria_BranchNrDescSort() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().orderDescByBranchNr(true).build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 5L);
    List<WssBranchDto> branches = wssBranchPage.getContent();
    Assert.assertThat(branches.get(0).getBranchNr(), Is.is(1029));
  }

  @Test
  public void testSearchBranchByCriteria_BranchNrSort() {
    final WssBranchSearchRequestCriteria criteria =
        WssBranchSearchRequestCriteria.builder().orderDescByBranchNr(false).build();
    final Page<WssBranchDto> wssBranchPage =
        wssBranchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(wssBranchPage, 5L);
    List<WssBranchDto> branches = wssBranchPage.getContent();
    Assert.assertThat(branches.get(0).getBranchNr(), Is.is(1001));
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
    Optional<WssBranchDto> branchDetail = wssBranchService.getBranchDetail(1234, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(branchDetail.isPresent(), Is.is(false));
  }

  @Test
  public void testGetBranchDetail() throws WssBranchValidationException {
    final int branchNr = 1001;
    final Optional<WssBranchDto> branchDetail = wssBranchService.getBranchDetail(branchNr, WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(branchDetail.isPresent(), Is.is(true));
    Assert.assertThat(branchDetail.get().getBranchNr(), Is.is(branchNr));
  }

  @Test
  public void testGetBranchList() {
    final List<WssBranchDto> branches = wssBranchService.getBranchesByOrganisation(WHOLE_SALER_ORG_ID_137);
    Assert.assertThat(CollectionUtils.isEmpty(branches), Is.is(false));
  }

  private void assertSearchResult(final Page<WssBranchDto> wssBranchPage, final Long expected) {
    Assert.assertThat(wssBranchPage, Matchers.notNullValue());
    Assert.assertThat(wssBranchPage.getContent(), Matchers.notNullValue());
    if (wssBranchPage.hasContent()) {
      Assert.assertThat(wssBranchPage.getTotalElements(), Is.is(expected));
    }
  }

  private WssBranchRequestBody initBranchData() {
    return WssBranchRequestBody.builder()
        .branchNr(12345678)
        .branchCode("LEOTEST")
        .openingTime("07:30")
        .closingTime("17:00")
        .lunchStartTime("10:00")
        .lunchEndTime("13:00")
        .wssBranchOpeningTimes(Collections.emptyList())
        .build();
  }
}
