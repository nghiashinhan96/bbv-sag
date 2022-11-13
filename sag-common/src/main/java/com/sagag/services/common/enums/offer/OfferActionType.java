package com.sagag.services.common.enums.offer;

public enum OfferActionType {
  NONE, ADDITION_PERCENT, ADDITION_AMOUNT, DISCOUNT_PERCENT,
  ADDITION_AMOUNT_ARTICLES, ADDITION_PERCENT_ARTICLES, DISCOUNT_PERCENT_WORK,
  DISCOUNT_AMOUNT, ADDITION_PERCENT_WORK, DISCOUNT_AMOUNT_WORK, ADDITION_AMOUNT_WORK,
  OFFERACTION_TYPE, DISCOUNT_PERCENT_ARTICLES, DISCOUNT_AMOUNT_ARTICLES;

  public boolean isAdditionPercent() {
    return ADDITION_PERCENT == this;
  }

  public boolean isDiscountPercent() {
    return DISCOUNT_PERCENT == this;
  }

  public boolean isAdditionPercentWork() {
    return ADDITION_PERCENT_WORK == this;
  }

  public boolean isDiscountPercentWork() {
    return DISCOUNT_PERCENT_WORK == this;
  }

  public boolean isAdditionPercentArticles() {
    return ADDITION_PERCENT_ARTICLES == this;
  }

  public boolean isDiscountPercentArticles() {
    return DISCOUNT_PERCENT_ARTICLES == this;
  }

  public boolean isAdditionAmount() {
    return ADDITION_AMOUNT == this;
  }

  public boolean isDiscountAmount() {
    return DISCOUNT_AMOUNT == this;
  }

  public boolean isAdditionAmountWork() {
    return ADDITION_AMOUNT_WORK == this;
  }

  public boolean isDiscountAmountWork() {
    return DISCOUNT_AMOUNT_WORK == this;
  }

  public boolean isAdditionAmountArticles() {
    return ADDITION_AMOUNT_ARTICLES == this;
  }

  public boolean isDiscountAmountArticles() {
    return DISCOUNT_AMOUNT_ARTICLES == this;
  }

  public boolean isNone() {
    return NONE == this;
  }

  public boolean isDiscountMode() {
    return isDiscountAmount() || isDiscountAmountArticles() || isDiscountAmountWork()
        || isDiscountPercent() || isDiscountPercentArticles() || isDiscountPercentWork();
  }

  public boolean isPercentType() {
    return isAdditionPercent() || isAdditionPercentArticles() || isAdditionPercentWork()
        || isDiscountPercent() || isDiscountPercentArticles() || isDiscountPercentWork();
  }

  public boolean isAmountType() {
    return isAdditionAmount() || isAdditionAmountArticles() || isAdditionAmountWork()
        || isDiscountAmount() || isDiscountAmountArticles() || isDiscountAmountWork();
  }

  public boolean isOwnWorkType() {
    return isAdditionPercentWork() || isDiscountPercentWork()
        || isAdditionAmountWork() || isDiscountAmountWork();
  }

  public boolean isOwnArticleType() {
    return isAdditionPercentArticles() || isDiscountPercentArticles()
        || isAdditionAmountArticles() || isDiscountAmountArticles();
  }
}
