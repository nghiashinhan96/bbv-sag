package com.sagag.services.ivds.response;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.autonet.erp.AutonetArticleInfos;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ArticleInformationResponseItem implements Serializable {

  private static final long serialVersionUID = -246637817196488878L;

  private List<Availability> availabilities;

  private PriceWithArticle price;

  private ArticleStock stock;

  private Double totalAxStock;
  
  private Double deliverableStock;

  private boolean allowedAddToShoppingCart;
  // TODO @hiendo move FC net prices to object price
  private Double totalFinalCustomerNetPrice;

  private Double finalCustomerNetPrice;

  private Double finalCustomerNetPriceWithVat;

  private Double totalFinalCustomerNetPriceWithVat;

  private AutonetArticleInfos autonetInfos;

  private ArticleDocDto depositArticle;

  private ArticleDocDto vocArticle;

  private ArticleDocDto vrgArticle;

  private ArticleDocDto pfandArticle;
  
  private List<ErpArticleMemo> memos;

}
