package com.sagag.services.ax.article;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxPriceUtils;
import com.sagag.services.common.profiles.AtChProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.ErpArticleMemoKey;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static com.sagag.services.ax.utils.AxArticleUtils.OIL_FILTER_GAID;

@Service
@AtChProfile
public class AtChAttachedArticleHandlerImpl extends AbstractAttachedArticleHandler {

  @Override
  public void buildMemoList(List<ArticleDocDto> filteredArticles, ArticleSearchCriteria criteria) {
    super.buildMemoList(filteredArticles, criteria);

    Map<String, ArticleDocDto> mapDepositArticle = getMapDepositArticle(filteredArticles, criteria);
    boolean allowShowPfand =
        criteria.isAllowShowPfandArticle() && StringUtils.isBlank(criteria.getCustDisposalNumber());
    filteredArticles.stream()
        .filter(art -> Objects.nonNull(art.getArticle()))
        .forEach(art -> {
          Article articleErp = art.getArticle();
          art.setMemos(buildArtMemos());
          // Any articles except F-GAS article can be added to shopping cart,
          // F-GAS articles only allow add to shopping cart if customer has F-GAS approve type
          art.setAllowedAddToShoppingCart(true);
          if (mapDepositArticle.containsKey(articleErp.getDepotArticleId())) {
            activeMemo(ErpArticleMemoKey.DEPOSIT_ITEM.getKey(), art.getMemos());
            art.setDepositArticle(mapDepositArticle.get(articleErp.getDepotArticleId()));
          }

          if (articleErp.isNonReturnable()) {
            activeMemo(ErpArticleMemoKey.NON_RETURNABLE.getKey(), art.getMemos());
          }

          if (mapDepositArticle.containsKey(articleErp.getVocArticleId())) {
            setArticleVocArticle(mapDepositArticle, art, articleErp);
          }

          if (mapDepositArticle.containsKey(articleErp.getVrgArticleId())) {
            setArticleVrgArticle(mapDepositArticle, art, articleErp);
          }

          if (existingValidItemApprovalType(articleErp)) {
            activeMemo(ErpArticleMemoKey.FGAS.getKey(), art.getMemos());
            art.setAllowedAddToShoppingCart(allowedAddToShoppingCart(
                articleErp.getItemApprovalTypeNames(), criteria.getCustApprovalTypes()));
          }

          if (allowShowPfand && isPfandArticle().test(art)) {
            ArticleDocDto pfand = createPfandArticle(art.getIdSagsys(), art.getSalesQuantity());
            pfand.setPrice(AxArticleUtils.createPricePfandArticle(art.getSalesQuantity(),
                AxPriceUtils.defaultVatRate(art, criteria.getVatRate())));
            art.setPfandArticle(pfand);
            activeMemo(ErpArticleMemoKey.DEPOSIT_ITEM.getKey(), art.getMemos());
          }
        });
  }

  private Predicate<ArticleDocDto> isPfandArticle() {
    return art -> Objects.nonNull(art)
        && StringUtils.equalsIgnoreCase(OIL_FILTER_GAID, art.getGaId());
  }

  private ArticleDocDto createPfandArticle(final String parentIdSagSys, final int salesQuantity) {
    final ArticleDocDto pfandArticle = new ArticleDocDto();
    Article pfandErpArticle = new Article();
    final String pfandArticleId =
        StringUtils.defaultString(parentIdSagSys) + AxArticleUtils.PFAND_ART_ID;
    pfandErpArticle.setSalesQuantity(salesQuantity);
    pfandErpArticle.setId(pfandArticleId);
    pfandArticle.setArticle(pfandErpArticle);
    pfandArticle.setArtid(pfandArticleId);
    pfandArticle.setIdSagsys(pfandArticleId);
    pfandArticle.setSalesQuantity(salesQuantity);
    return pfandArticle;
  }

}
