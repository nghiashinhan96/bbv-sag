package com.sagag.services.service.cart;

import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class CartItemUtils {

  public static ArticleDocDto createPfandArticleDto(final String parentIdSagSys,
      final int salesQuantity, final int amountNumber) {
    final ArticleDocDto pfandArticle = new ArticleDocDto();
    Article pfandErpArticle = new Article();
    final String pfandArticleId = buildPfandArticleId(parentIdSagSys);
    pfandErpArticle.setSalesQuantity(salesQuantity);
    pfandErpArticle.setId(pfandArticleId);
    pfandArticle.setArticle(pfandErpArticle);
    pfandArticle.setArtid(pfandArticleId);
    pfandArticle.setIdSagsys(pfandArticleId);
    pfandArticle.setAmountNumber(amountNumber);
    pfandArticle.setSalesQuantity(salesQuantity);
    return pfandArticle;
  }

  private static String buildPfandArticleId(final String parentIdSagSys) {
    return StringUtils.defaultString(parentIdSagSys) + AxArticleUtils.PFAND_ART_ID;
  }

  public static boolean showPfandArticleCase(final Customer customer, final ArticleDocDto article) {
    if (Objects.isNull(customer) || !customer.allowShowPfandArticle()) {
      return false;
    }
    return AxArticleUtils.equalsPfandArticle(article);
  }

  public static boolean hasNormalAttachedArticle(final ArticleDocDto article) {
    if (Objects.isNull(article)) {
      return false;
    }
    return !AxArticleUtils.OIL_FILTER_GAID.equalsIgnoreCase(article.getGaId())
        && article.hasAttachedArticle();
  }

   public static void bindAttachedArticleItems(ShoppingCart cart, double defaulVatRate,
      Customer customer) {
    Assert.notNull(cart, "The given shopping cart must not be null");
    cart.getItems().forEach(item -> {
      final ArticleDocDto articleDoc = item.getArticle();
      Double vatRate = !Objects.isNull(item.getPriceItem().getVatInPercent())
          ? item.getPriceItem().getVatInPercent()
          : defaulVatRate;
      final List<ArticleDocDto> attachedArticles = item.getAttachedArticles();
      if (!isValidItemWithAttachedArticle(articleDoc, attachedArticles)) {
        return;
      }
      final List<ShoppingCartItem> attachedCartItems = new ArrayList<>();
      attachedArticles.forEach(attachedArt -> attachedCartItems
          .add(buildAttachedArticleItem(item, attachedArt, customer, vatRate)));
      item.setAttachedCartItems(attachedCartItems);
    });
  }

  private static ShoppingCartItem buildAttachedArticleItem(ShoppingCartItem item,
      ArticleDocDto attachedArt, Customer customer, double vatRate) {
    ArticleDocDto articleDoc = item.getArticle();
    final boolean isPfand = showPfandArticleCase(customer, articleDoc);
    final ShoppingCartItem attachedCartItem =
        new ShoppingCartItem(attachedArt, item.getQuantity(), vatRate);
    attachedCartItem.setPriceItem(attachedArt, vatRate);
    if (isPfandOrDepotItem(attachedArt, articleDoc, isPfand)) {
      attachedCartItem.setDepot(true);
      attachedCartItem.setPfand(isPfand);
    }
    if (isRecycleItem(attachedArt, articleDoc)) {
      attachedCartItem.setRecycle(true);
    }
    if (isVocItem(attachedArt, articleDoc)) {
      attachedCartItem.setVoc(true);
    }
    if (isVrgItem(attachedArt, articleDoc)) {
      attachedCartItem.setVrg(true);
    }
    return attachedCartItem;
  }

  private static boolean isPfandOrDepotItem(ArticleDocDto attachedArt, ArticleDocDto articleDoc,
      boolean isPfand) {
    String articleId = attachedArt.getArtid();
    if (StringUtils.isBlank(articleId)) {
      return false;
    }
    String depotArticleId = articleDoc.getArticle().getDepotArticleId();
    return (isPfand && buildPfandArticleId(articleDoc.getArtid()).equals(articleId))
        || (!StringUtils.isBlank(depotArticleId) && StringUtils.equals(depotArticleId, articleId));
  }

  private static boolean isRecycleItem(ArticleDocDto attachedArt, ArticleDocDto articleDoc) {
    String articleId = attachedArt.getArtid();
    if (StringUtils.isBlank(articleId)) {
      return false;
    }
    String recycleArticleId = articleDoc.getArticle().getRecycleArticleId();
    return !StringUtils.isBlank(recycleArticleId)
        && StringUtils.equals(recycleArticleId, articleId);
  }

  private static boolean isVocItem(ArticleDocDto attachedArt, ArticleDocDto articleDoc) {
    String articleId = attachedArt.getArtid();
    if (StringUtils.isBlank(articleId)) {
      return false;
    }
    String vocArticleId = articleDoc.getArticle().getVocArticleId();
    return !StringUtils.isBlank(vocArticleId) && StringUtils.equals(vocArticleId, articleId);
  }

  private static boolean isVrgItem(ArticleDocDto attachedArt, ArticleDocDto articleDoc) {
    String articleId = attachedArt.getArtid();
    if (StringUtils.isBlank(articleId)) {
      return false;
    }
    String vrgArticleId = articleDoc.getArticle().getVrgArticleId();
    return !StringUtils.isBlank(vrgArticleId) && StringUtils.equals(vrgArticleId, articleId);
  }

  private static boolean isValidItemWithAttachedArticle(ArticleDocDto articleDoc,
      List<ArticleDocDto> attachedArticles) {
    return !Objects.isNull(articleDoc) && !Objects.isNull(articleDoc.getArticle())
        && !CollectionUtils.isEmpty(attachedArticles);
  }
}
