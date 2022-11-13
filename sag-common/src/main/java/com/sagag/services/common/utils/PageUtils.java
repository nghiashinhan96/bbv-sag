package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;

/**
 * Utility class for JPA repository.
 */
@UtilityClass
@Slf4j
public class PageUtils {

  /** default paging from new JPA 2.0.6.RELEASE version. */
  public static final PageRequest DEF_PAGE = PageRequest.of(0, SagConstants.DEFAULT_PAGE_NUMBER);

  /** max pageable records allowed. */
  public static final PageRequest MAX_PAGE = PageRequest.of(0, SagConstants.MAX_PAGE_SIZE);

  /** Just make for some specific case need verify first item in page */
  public static final PageRequest FIRST_ITEM_PAGE = PageRequest.of(0, 1);

  /**
   * Returns safely the pageable with size for first page.
   *
   * @param size the size of the page, default to get to {@link SagConstants#DEFAULT_PAGE_NUMBER}
   *        records.
   * @return the {@link PageRequest} in the JPA query.
   */
  public static PageRequest defaultPageable(int size) {
    return limitPageRequest(0, size);
  }

  private static PageRequest limitPageRequest(int page, int size) {
    final int maxSize = SagConstants.MAX_PAGE_SIZE;
    if (size >= maxSize) {
      log.warn("Pageable has size > {}. The program will take {} as page size", maxSize, maxSize);
      return PageRequest.of(page, SagConstants.MAX_PAGE_SIZE);
    }
    if (size <= 0) {
      return PageRequest.of(page, SagConstants.DEFAULT_PAGE_NUMBER);
    }
    return PageRequest.of(page, size);
  }

  /**
   * Returns safely the pageable with <code>size</code> for current page <code>page</code>.
   *
   * @param page the index-th page to retrieve
   * @param size the size of the page, default to get to {@link SagConstants#DEFAULT_PAGE_NUMBER}
   *        records if size <= 0, and to get to {@link SagConstants#MAX_PAGE_SIZE} if size >=
   *        MAX_PAGE_SIZE.
   * @return the {@link PageRequest} in the JPA query.
   */
  public static PageRequest defaultPageable(int page, int size) {
    return limitPageRequest(page, size);
  }
}
