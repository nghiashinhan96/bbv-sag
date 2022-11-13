package com.sagag.services.elasticsearch.query.articles.bulbs;

import com.sagag.services.elasticsearch.common.BulbConstants;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.IAggregationBuilder;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BulbArticleQueryBuilder extends AbstractArticleQueryBuilder<BulbArticleSearchCriteria> implements
    IAggregationBuilder {

  private static final String[] BULB_GEN_ART_IDS = new String[] { "104", "105", "106", "107", "108",
    "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121",
    "1092", "1172", "2126", "2502", "2769", "2770", "2771", "2773", "2774", "2775", "2776",
    "2777", "2778", "2779", "2780", "2781", "2782", "2783", "2784", "2785", "2786", "2787",
    "2788", "2789", "2797", "2798", "2991", "2992", "2993", "2994", "2997", "2998", "2999",
    "3000", "3001", "3002", "3003", "3004", "3005", "3193", "3211", "3212", "3484", "3680",
    "3683", "3853", "3854", "4304", "4305", "4306", "49022", "49024", "49026", "49027", "49413",
    "49856", "49978", "49979", "49980", "49982", "50038", "50041", "1457", "3326", "5857", "7084",
    "7624", "8226", "8262", "8271", "8311", "8788", "8789", "8790", "8791", "8792", "8793", "8880",
    "8959", "8969", "60090"};

  @Override
  public SearchQuery buildQuery(BulbArticleSearchCriteria criteria, Pageable pageable,
    String... indices) {
    final BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
      .apply(commonQueryBuilder(criteria));

    aggFilterBuilders.forEach(builder -> builder.addFilter(queryBuilder, criteria));

    final NativeSearchQueryBuilder searchQueryBuilder = ArticleQueryUtils.nativeQueryBuilder(
        defaultPageable(pageable), indices)
        .withQuery(queryBuilder);

    aggregated(searchQueryBuilder, ArticleField.SUPPLIER_RAW);

    return searchQueryBuilder.build();
  }

  private static BoolQueryBuilder commonQueryBuilder(final BulbArticleSearchCriteria criteria) {
    final BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

    // Filter by battery gen_art_ids
    searchQuery.must(QueryBuilders.termsQuery(ArticleField.GA_ID.value(), BULB_GEN_ART_IDS));

    // Filter by suppliers
    Optional.ofNullable(criteria.getSuppliers())
    .filter(CollectionUtils::isNotEmpty)
    .map(suppliers -> QueryBuilders.termsQuery(ArticleField.SUPPLIER_RAW.value(), suppliers))
    .ifPresent(searchQuery::must);

    // Filter by voltages
    Optional.ofNullable(criteria.getVoltages())
    .filter(CollectionUtils::isNotEmpty)
    .map(voltages -> buildCvpRawQuery(BulbConstants.VOLTAGE_CID, voltages))
    .ifPresent(searchQuery::must);

    // Filter by watts
    Optional.ofNullable(criteria.getWatts())
    .filter(CollectionUtils::isNotEmpty)
    .map(watts -> buildCvpRawQuery(BulbConstants.WATT_CID, watts))
    .ifPresent(searchQuery::must);

    // Filter by type of codes & bilds
    Optional.ofNullable(criteria.getCodes())
    .filter(CollectionUtils::isNotEmpty)
    .map(codes -> buildCvpRawQuery(BulbConstants.CODE_CID, codes))
    .ifPresent(searchQuery::must);

    return searchQuery;
  }

}
