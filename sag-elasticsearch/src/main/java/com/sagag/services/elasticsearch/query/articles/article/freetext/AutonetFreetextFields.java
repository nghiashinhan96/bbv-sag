package com.sagag.services.elasticsearch.query.articles.article.freetext;

import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.elasticsearch.enums.Index;
import org.springframework.stereotype.Component;

@Component
@AutonetProfile
public class AutonetFreetextFields implements IFreetextFields {

  @Override
  public String[] refsFields() {
    return new String[] {
        Index.Article.ARTID.field(),
        Index.Article.NAME.field(),
        Index.Article.ID_AUTONET.field(),
        Index.Article.ARTNR.field(),
        Index.Article.SUPPLIER.field(),
        Index.Article.PRODUCT_ADDON.field(),
        Index.Article.PRODUCT_BRAND.field(),
        Index.Article.ARTICLES_NN.field(),
        Index.Article.PRODUCT_VALUES.field()
    };
  }

  @Override
  public String[] refFieldsFullText() {
    return new String[] {
      Index.Article.ID_AUTONET.field(),
      Index.Article.ARTNR.field(),
      Index.Article.ARTICLES_NN.field()
    };
  }


}
