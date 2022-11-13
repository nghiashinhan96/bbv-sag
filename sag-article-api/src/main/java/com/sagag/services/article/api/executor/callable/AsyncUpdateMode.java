package com.sagag.services.article.api.executor.callable;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import static com.sagag.services.common.enums.ErpSendMethodEnum.PICKUP;
import org.apache.commons.lang3.StringUtils;

public enum AsyncUpdateMode {

  PRICE {
    @Override
    public boolean isValid(ArticleSearchCriteria criteria) {
      return criteria.isUpdatePrice();
    }
  },
  STOCK {
    @Override
    public boolean isValid(ArticleSearchCriteria criteria) {
      return criteria.isUpdateStock();
    }
  },
  AVAILABILITY {
    @Override
    public boolean isValid(ArticleSearchCriteria criteria) {
      return criteria.isUpdateAvailability();
    }
  },
  STOCK_PICKUP_DIFFERENT_BRANCH {
    @Override
    public boolean isValid(ArticleSearchCriteria criteria) {
      final String defaultBrandId = criteria.getDefaultBrandId();
      return AVAILABILITY.isValid(criteria)
        && PICKUP == ErpSendMethodEnum.valueOf(criteria.getDeliveryType())
        && StringUtils.isNotBlank(defaultBrandId)
        && !defaultBrandId.equals(criteria.getPickupBranchId());
    }
  };

  public abstract boolean isValid(ArticleSearchCriteria criteria);
}
