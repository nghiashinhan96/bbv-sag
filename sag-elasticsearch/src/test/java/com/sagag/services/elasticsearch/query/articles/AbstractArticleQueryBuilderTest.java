package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.elasticsearch.query.filter.AggregationFilterBuilder;

import org.mockito.Mock;

import java.util.List;

public abstract class AbstractArticleQueryBuilderTest {

  protected static final String INDEX = "ax_articles_de";

  protected static final String[] LOCKS = new String[] { "dch" };

  @Mock
  protected List<AggregationFilterBuilder> aggFilterBuilders;

  @Mock
  private CountryConfiguration countryConfig;

}
