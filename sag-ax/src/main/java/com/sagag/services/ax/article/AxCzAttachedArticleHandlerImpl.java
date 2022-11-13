package com.sagag.services.ax.article;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.common.profiles.AxCzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.ErpArticleMemoKey;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AxCzProfile
public class AxCzAttachedArticleHandlerImpl extends AbstractAttachedArticleHandler {

  @Override
  public void buildMemoList(List<ArticleDocDto> filteredArticles, ArticleSearchCriteria criteria) {
    super.buildMemoList(filteredArticles, criteria);

    Map<String, ArticleDocDto> mapDepositArticle = getMapDepositArticle(filteredArticles, criteria);
    filteredArticles.stream()
        .filter(art -> Objects.nonNull(art.getArticle()))
        .forEach(art -> {
          Article articleErp = art.getArticle();
          art.setMemos(buildArtMemos());
          if (mapDepositArticle.containsKey(articleErp.getDepotArticleId())) {
            activeMemo(ErpArticleMemoKey.DEPOSIT_ITEM.getKey(), art.getMemos());
            art.setDepositArticle(mapDepositArticle.get(articleErp.getDepotArticleId()));
          }

          if (existingValidItemApprovalType(articleErp)) {
            activeMemo(ErpArticleMemoKey.FGAS.getKey(), art.getMemos());
            art.setAllowedAddToShoppingCart(allowedAddToShoppingCart(
                articleErp.getItemApprovalTypeNames(), criteria.getCustApprovalTypes()));
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
        });
  }

}
