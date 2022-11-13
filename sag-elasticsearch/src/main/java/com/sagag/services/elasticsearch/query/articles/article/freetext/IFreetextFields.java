package com.sagag.services.elasticsearch.query.articles.article.freetext;

import com.sagag.services.elasticsearch.enums.Index;

import java.util.HashMap;
import java.util.Map;

public interface IFreetextFields {

  default String[] refsFields() {
    return new String[] {
      Index.Article.ARTID.field(),
      Index.Article.NAME.field(),
      Index.Article.ID_PIM.field(),
      Index.Article.ARTNR.field(),
      Index.Article.SUPPLIER.field(),
      Index.Article.PRODUCT_ADDON.field(),
      Index.Article.PRODUCT_BRAND.field(),
      Index.Article.ARTICLES_NN.field(),
      Index.Article.PRODUCT_VALUES.field()
    };
  }

  default String[] criteriaFields() {
    return new String[] {
        Index.Article.CN.fullQField()
    };
  }

  default String[] partFields() {
    return new String[] {
        Index.Article.PNRN.fullQField()
    };
  }

  default String[] partsExtFields() {
    return new String[]{
        Index.Article.PARTS_EX_PNRN.fullQField()
    };
  }

  default String[] productInfoTextFields() {
    return new String[] {
        Index.Article.PRODUCT_INFO_TEXT.field()
    };
  }

  default String[] refFieldsFullText() {
    return new String[] {
      Index.Article.ID_PIM.field(),
      Index.Article.ARTNR.field(),
      Index.Article.ARTICLES_NN.field(),
      Index.Article.GOLDEN_RECORD_ID.field()
    };
  }


  default Map<String, Float> attributesBoost() {
    final Map<String, Float> attributesBoost = new HashMap<>();
    attributesBoost.put(Index.Article.NAME.field(), 5.0f);
    attributesBoost.put(Index.Article.ARTNR.field(), 5.0f);
    attributesBoost.put(Index.Article.SUPPLIER.field(), 3.0f);
    attributesBoost.put(Index.Article.PRODUCT_ADDON.field(), 4.0f);
    attributesBoost.put(Index.Article.PRODUCT_BRAND.field(), 5.0f);
    attributesBoost.put(Index.Article.CVP.fullQField(), 1.2f);
    attributesBoost.put(Index.Article.INFO_TXT.fullQField(), 4.0f);
    attributesBoost.put(Index.Article.PNRN.fullQField(), 4.0f);
    attributesBoost.put(Index.Article.PARTS_EX_PNRN.fullQField(), 4.0f);
    return attributesBoost;
  }

}
