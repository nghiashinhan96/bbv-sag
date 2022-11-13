package com.sagag.services.domain.eshop.criteria;

import com.sagag.services.common.utils.PageUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRequest implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8038924418713117916L;

  private int page = 0;

  private int size = 10;
  
  public Pageable buildPageable() {
    return PageUtils.defaultPageable(page, size);
  }
}
