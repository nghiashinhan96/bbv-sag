package com.sagag.services.elasticsearch.utils;

import com.sagag.services.common.contants.SagConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ElasticsearchConstants {

  public static final char WILDCARD = '*';

  public static final float DEFAULT_BOOST = 1.0f;

  public static final float MAX_BOOST = 100.0f;

  public static final int MAX_SIZE_AGGS = SagConstants.MAX_PAGE_SIZE;

  public static final String VEH_ID_ATTR = "vehid";

  public static final String REF_ARTICLE_ID_IN_FITMENT = "articles.artid";

}
