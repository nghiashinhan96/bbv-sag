package com.sagag.eshop.service.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.service.api.BranchService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.BranchDetailDto;
import com.sagag.eshop.service.dto.BranchDto;
import com.sagag.eshop.service.exception.BranchValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.WeekDay;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.BranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * Integration tests for branch service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class BranchServiceImplIT {

  @Autowired
  private BranchService branchService;

  @Autowired
  private BranchRepository branchRepository;

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewBranch_requestNull() throws BranchValidationException {
    branchService.create(null);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_BranchNrNull() throws BranchValidationException {
    branchService.create(new BranchRequestBody());
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_OpeningTimeNull() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    BranchOpeningTimeDto openingTime =
        BranchOpeningTimeDto.builder().openingTime(null).weekDay(WeekDay.MONDAY).build();
    branchRequest.setBranchOpeningTimes(Lists.newArrayList(openingTime));
    branchService.create(branchRequest);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_InvalidBranchCodeLength() throws BranchValidationException {
    BranchRequestBody request = BranchRequestBody.builder().branchCode("ABCDEFGHIKLM").build();
    branchService.create(request);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_ClosingTimeInvalid() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    BranchOpeningTimeDto openingTime =
        BranchOpeningTimeDto.builder().closingTime("77:00").weekDay(WeekDay.MONDAY).build();
    branchRequest.setBranchOpeningTimes(Lists.newArrayList(openingTime));
    branchService.create(branchRequest);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_TimeInvalid() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    BranchOpeningTimeDto openingTime =
        BranchOpeningTimeDto.builder().openingTime("abc").weekDay(WeekDay.MONDAY).build();
    branchRequest.setBranchOpeningTimes(Lists.newArrayList(openingTime));
    branchService.create(branchRequest);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_DuplicatedBranchNr() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    branchRequest.setBranchNr(1001);
    branchService.create(branchRequest);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_StartAfterEndTime() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    BranchOpeningTimeDto openingTime = BranchOpeningTimeDto.builder().openingTime("17:30:00")
        .closingTime("07:30:00").weekDay(WeekDay.MONDAY).build();
    branchRequest.setBranchOpeningTimes(Lists.newArrayList(openingTime));
    branchService.create(branchRequest);
  }

  @Test(expected = BranchValidationException.class)
  public void testCreateNewBranch_StartAfterEndLunchTime() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    BranchOpeningTimeDto openingTime = BranchOpeningTimeDto.builder().lunchEndTime("10:00:00")
        .lunchStartTime("13:30:00").weekDay(WeekDay.MONDAY).build();
    branchRequest.setBranchOpeningTimes(Lists.newArrayList(openingTime));
    branchService.create(branchRequest);
  }

  @Test
  public void testCreateNewBranch() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    final BranchDetailDto branchDto = branchService.create(branchRequest);
    Assert.assertNotNull(branchDto);
    Assert.assertNotNull(branchDto.getId());
    Assert.assertThat(branchDto.getBranchNr(), Is.is(branchRequest.getBranchNr()));
    Assert.assertThat(branchDto.getBranchCode(), Is.is(branchRequest.getBranchCode()));
    Assert.assertThat(branchDto.getCountryId(), Is.is(branchRequest.getCountryId()));
  }

  @Test(expected = BranchValidationException.class)
  public void testUpdateBranch_NotExist() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    Assert.assertThat(branchRequest.getValidForKSL(), Is.is(true));
    branchRequest.setBranchNr(8888);
    branchService.update(branchRequest);
  }

  @Test
  public void testUpdateExistingBranch() throws BranchValidationException {
    final BranchRequestBody branchRequest = initBranchData();
    Assert.assertThat(branchRequest.getValidForKSL(), Is.is(true));
    branchRequest.setBranchNr(1001);
    branchRequest.setValidForKSL(false);
    final BranchDetailDto branchDto = branchService.update(branchRequest);
    Assert.assertNotNull(branchDto);
    Assert.assertThat(branchDto.getBranchNr(), Is.is(branchRequest.getBranchNr()));
    Assert.assertThat(branchDto.getValidForKSL(), Is.is(false));
  }

  @Test(expected = BranchValidationException.class)
  public void testRemoveBranch_NotExist() throws BranchValidationException {
    branchService.remove(8888);
  }

  @Test
  public void testRemoveBranch() throws BranchValidationException {
    final Integer branchNr = 1001;

    final Optional<Branch> existingBranch = branchRepository.findOneByBranchNr(branchNr);
    Assert.assertThat(existingBranch.isPresent(), Is.is(true));

    branchService.remove(branchNr);
    final Optional<Branch> deletedBranch = branchRepository.findOneByBranchNr(branchNr);

    Assert.assertThat(deletedBranch.isPresent(), Is.is(false));
  }

  @Test
  public void testSearchBranchByCriteria_EmptyFilter() {
    final BranchSearchRequestCriteria criteria = new BranchSearchRequestCriteria();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 33L);
  }

  @Test
  public void testSearchBranchByCriteria_BranchCode() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().branchCode("W").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 9L);
  }

  @Test
  public void testSearchBranchByCriteria_FullBranchCode() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().branchCode("OTT").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_BranchNr() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().branchNr("01").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 11L);
  }

  @Test
  public void testSearchBranchByCriteria_FullBranchNr() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().branchNr("1001").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_FullLunchStartTime() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().lunchStartTime("11:30").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_FullLunchEndTime() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().lunchEndTime("13:00").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 1L);
  }

  @Test
  public void testSearchBranchByCriteria_EmptyResult() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().branchCode("ABC").openingTime("22").build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 0L);
  }

  @Test
  public void testSearchBranchByCriteria_Space() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().branchCode(StringUtils.SPACE).build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 33L);
  }

  @Test
  public void testSearchBranchByCriteria_BranchNrDescSort() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().orderDescByBranchNr(true).build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 33L);
    List<BranchDto> branches = articles.getContent();
    Assert.assertThat(branches.get(0).getBranchNr(), Is.is(1032));
  }

  @Test
  public void testSearchBranchByCriteria_BranchNrSort() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().orderDescByBranchNr(false).build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 33L);
    List<BranchDto> branches = articles.getContent();
    Assert.assertThat(branches.get(0).getBranchNr(), Is.is(123));
  }

  @Test
  public void testSearchBranchByCriteria_OpeningDescSort() {
    final BranchSearchRequestCriteria criteria =
        BranchSearchRequestCriteria.builder().orderDescByOpeningTime(true).build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 33L);
  }

  @Test
  public void testSearchBranchByCriteria_OpeningDescClosingAscSort() {
    final BranchSearchRequestCriteria criteria = BranchSearchRequestCriteria.builder()
        .orderDescByOpeningTime(true).orderDescByClosingTime(false).build();
    final Page<BranchDto> articles =
        branchService.searchBranchByCriteria(criteria, PageRequest.of(0, 10));
    assertSearchResult(articles, 33L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchBranchByCriteria_NullCriteria() {
    branchService.searchBranchByCriteria(null, PageRequest.of(0, 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBranchDetail_NullBranchNr() throws BranchValidationException {
    branchService.getBranchDetail(null);
  }

  @Test
  public void testGetBranchDetail_NotFound() throws BranchValidationException {
    Optional<BranchDetailDto> branchDetail = branchService.getBranchDetail(1234);
    Assert.assertThat(branchDetail.isPresent(), Is.is(false));
  }

  @Test
  public void testGetBranchDetail() throws BranchValidationException {
    final int branchNr = 1001;
    final Optional<BranchDetailDto> branchDetail = branchService.getBranchDetail(branchNr);
    Assert.assertThat(branchDetail.isPresent(), Is.is(true));
    Assert.assertThat(branchDetail.get().getBranchNr(), Is.is(branchNr));
  }

  @Test
  public void testGetBranchList() {
    final List<BranchDto> branches = branchService.getBranches();
    Assert.assertThat(CollectionUtils.isEmpty(branches), Is.is(false));
  }

  @Test(expected = BranchValidationException.class)
  public void testGetBranchListByCountry_NotFound() throws BranchValidationException {
    branchService.getBranchesByCountry("abc");
  }

  @Test
  public void testGetBranchListByCountry() throws BranchValidationException {
    final List<BranchDto> branches = branchService.getBranchesByCountry("at");
    Assert.assertThat(CollectionUtils.isEmpty(branches), Is.is(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBranchListByCountry_Null() throws BranchValidationException {
    branchService.getBranchesByCountry(null);
  }

  private void assertSearchResult(final Page<BranchDto> articles, final Long expected) {
    Assert.assertThat(articles, Matchers.notNullValue());
    Assert.assertThat(articles.getContent(), Matchers.notNullValue());
    Assert.assertThat(articles.getTotalElements(), Is.is(expected));
  }

  private BranchRequestBody initBranchData() {
    return BranchRequestBody.builder().branchNr(12345678).branchCode("LEOTEST").orgId(10003)
        .zip("4060").countryId(17).addressStreet("Edtstrasse 13").addressCity("Leonding")
        .addressDesc("Filiale Leonding").addressCountry("Österreich").regionId("AUT")
        .primaryFax("+43 732 678 181 200").primaryEmail("filleo@derendinger.at")
        .primaryPhone("+43 732 678 181").primaryUrl("www.derendinger.at").validForKSL(true)
        .openingTime("07:30").closingTime("17:00").lunchStartTime("10:00").lunchEndTime("13:00")
        .build();
  }
}
