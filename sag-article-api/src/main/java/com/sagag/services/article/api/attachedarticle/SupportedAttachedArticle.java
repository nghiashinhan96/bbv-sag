package com.sagag.services.article.api.attachedarticle;

import com.sagag.services.domain.sag.erp.Article;

public interface SupportedAttachedArticle {

  /**
   * Checks support depot article.
   *
   */
  boolean supportDepot();

  default boolean supportDepot(Article erpArticle) {
    return supportDepot() && erpArticle.hasDepotArticleId();
  }

  /**
   * Checks support recycle article.
   *
   */
  boolean supportRecycle();

  default boolean supportRecycle(Article erpArticle) {
    return supportRecycle() && erpArticle.hasRecycleArticleId();
  }

  /**
   * Checks support VOC article.
   *
   */
  boolean supportVoc();

  default boolean supportVoc(Article erpArticle) {
    return supportVoc() && erpArticle.hasVocArticleId();
  }

  /**
   * Checks support VRG article.
   *
   */
  boolean supportVrg();

  default boolean supportVrg(Article erpArticle) {
    return supportVrg() && erpArticle.hasVrgArticleId();
  }

}
