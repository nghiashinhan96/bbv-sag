package com.sagag.services.ivds.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetArticleSyncInformation implements Serializable {

  private static final long serialVersionUID = -5089150201731232626L;

  private static final int MAX_ARTICLES_PER_AVAILABILITY_REQUEST = 5;

  private List<ArticleInformationRequestItem> articleInformationRequestItems;

  private ErpInfoRequest erpInfoRequest;

  private int numberOfRequestedItems = MAX_ARTICLES_PER_AVAILABILITY_REQUEST;
}
