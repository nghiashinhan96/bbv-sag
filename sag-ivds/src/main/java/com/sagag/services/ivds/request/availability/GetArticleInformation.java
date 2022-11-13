package com.sagag.services.ivds.request.availability;

import com.sagag.services.ivds.request.ArticleInformationRequestItem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetArticleInformation implements Serializable {

  private static final long serialVersionUID = 1865707015345646642L;

  private static final int MAX_ARTICLES_PER_AVAILABILITY_REQUEST = 5;

  private List<ArticleInformationRequestItem> availabilityRequestItemList;

  private int numberOfRequestedItems = MAX_ARTICLES_PER_AVAILABILITY_REQUEST;
}
