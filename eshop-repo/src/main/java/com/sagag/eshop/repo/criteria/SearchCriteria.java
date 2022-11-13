package com.sagag.eshop.repo.criteria;

import com.sagag.services.common.contants.SagConstants;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCriteria implements Serializable {

  private static final long serialVersionUID = 8652743023832221225L;

  private int offset;

  private int pageSize;

  public int getOffset() {
    if (offset <= 0) {
      return 0;
    }
    return offset;
  }

  public int getPageSize() {
    if (pageSize <= 0) {
      return SagConstants.MIN_PAGE_SIZE;
    }
    if (pageSize > SagConstants.MAX_PAGE_SIZE) {
      return SagConstants.MAX_PAGE_SIZE;
    }

    return pageSize;
  }

}
