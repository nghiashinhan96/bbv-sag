package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;


/**
 * Test class for RepositoryUtils.
 */
public class PageUtilsTest {

  private static final int CUR_PAGE = 3;
  private static final int VALID_PAGESIZE = 500;
  private static final int ZERO_PAGESIZE = 0;
  private static final int NEGATIVE_PAGESIZE = -2;
  private static final int PAGESIZE_GREATER_THAN_MAX_SIZE = 12000;

  @Test
  public void givenInputGreaterThanMaxShouldGetDefaultPageable() {
    final PageRequest pageRequest = PageUtils.defaultPageable(PAGESIZE_GREATER_THAN_MAX_SIZE);
    // 12000 > SagConstants.MAX_PAGE_SIZE -> limit SagConstants.MAX_PAGE_SIZE
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.MAX_PAGE_SIZE));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(0));
  }

  @Test
  public void givenInputMaxShouldGetDefaultPageable() {
    final PageRequest pageRequest = PageUtils.defaultPageable(SagConstants.MAX_PAGE_SIZE);
    // input SagConstants.MAX_PAGE_SIZE = SagConstants.MAX_PAGE_SIZE -> SagConstants.MAX_PAGE_SIZE
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.MAX_PAGE_SIZE));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(0));
  }

  @Test
  public void givenNegativeSizeShouldGetDefaultPageable() {
    final PageRequest pageRequest = PageUtils.defaultPageable(NEGATIVE_PAGESIZE);
    // -2 < 0 -> limit SagConstants.DEFAULT_PAGE_NUMBER
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.DEFAULT_PAGE_NUMBER));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(0));
  }

  @Test
  public void givenZeroSizeShouldGetDefaultPageable() {
    final PageRequest pageRequest = PageUtils.defaultPageable(ZERO_PAGESIZE);
    // 0 <= 0 -> limit SagConstants.DEFAULT_PAGE_NUMBER
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.DEFAULT_PAGE_NUMBER));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(0));
  }

  @Test
  public void givenValidSizeShouldGetDefaultPageable() {
    final int pageSize = VALID_PAGESIZE;
    final PageRequest pageRequest = PageUtils.defaultPageable(pageSize);
    // 0 <= pageSize < SagConstants.MAX_PAGE_SIZE, get page request with input size
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(pageSize));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(0));
  }

  @Test
  public void givenInputGreaterThanMaxShouldGetDefaultPageableWithCurrentPage() {
    final PageRequest pageRequest =
        PageUtils.defaultPageable(CUR_PAGE, PAGESIZE_GREATER_THAN_MAX_SIZE);
    // 12000 > SagConstants.MAX_PAGE_SIZE -> limit SagConstants.MAX_PAGE_SIZE
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.MAX_PAGE_SIZE));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(CUR_PAGE));
  }

  @Test
  public void givenInputMaxShouldGetDefaultPageableWithCurrentPage() {
    final PageRequest pageRequest = PageUtils.defaultPageable(CUR_PAGE, SagConstants.MAX_PAGE_SIZE);
    // input SagConstants.MAX_PAGE_SIZE = SagConstants.MAX_PAGE_SIZE -> SagConstants.MAX_PAGE_SIZE
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.MAX_PAGE_SIZE));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(CUR_PAGE));
  }

  @Test
  public void givenNegativeSizeShouldGetDefaultPageableWithCurrentPage() {
    final PageRequest pageRequest = PageUtils.defaultPageable(CUR_PAGE, NEGATIVE_PAGESIZE);
    // -2 < 0 -> limit SagConstants.DEFAULT_PAGE_NUMBER
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.DEFAULT_PAGE_NUMBER));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(CUR_PAGE));
  }

  @Test
  public void givenZeroSizeShouldGetDefaultPageableWithCurrentPage() {
    final PageRequest pageRequest = PageUtils.defaultPageable(CUR_PAGE, ZERO_PAGESIZE);
    // 0 <= 0 -> limit SagConstants.DEFAULT_PAGE_NUMBER
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(SagConstants.DEFAULT_PAGE_NUMBER));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(CUR_PAGE));
  }

  @Test
  public void givenValidSizeShouldGetDefaultPageableWithCurrentPage() {
    final int pageSize = VALID_PAGESIZE;
    final PageRequest pageRequest = PageUtils.defaultPageable(CUR_PAGE, pageSize);
    // 0 <= pageSize < SagConstants.MAX_PAGE_SIZE, get page request with input size
    Assert.assertThat(pageRequest.getPageSize(), Matchers.is(pageSize));
    Assert.assertThat(pageRequest.getPageNumber(), Matchers.is(CUR_PAGE));
  }
}
