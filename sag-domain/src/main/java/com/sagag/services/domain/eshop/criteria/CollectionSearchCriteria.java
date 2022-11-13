package com.sagag.services.domain.eshop.criteria;

import com.sagag.services.common.utils.PageUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CollectionSearchCriteria extends BaseRequest implements Serializable {

  private static final long serialVersionUID = 6736744141835071136L;
  private String affiliate;
  private String customerNr;
  private String collectionName;
  private CollectionSearchSortableColumn sorting;
  private boolean useWholePage;

  public Pageable buildPageable() {
    if (isUseWholePage()) {
      return Pageable.unpaged();
    }
    return PageUtils.defaultPageable(getPage(), getSize());
  }
}
