package com.sagag.services.article.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class to contain depot or recycle article id to send to ERP.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachedArticleRequest implements Serializable {

  private static final long serialVersionUID = -6777805357208516651L;

  private String attachedArticleId;

  private Integer quantity;

  private Integer salesQuantity;

  private String cartKey;

  private Double inheritVatRate;

  public static AttachedArticleRequest of(final String cartKey, final String attachedArticleId,
      final int quantity, final int salesQuantity, Double vatRate) {
    final AttachedArticleRequest request = new AttachedArticleRequest();
    request.setCartKey(cartKey);
    request.setAttachedArticleId(attachedArticleId);
    request.setQuantity(quantity);
    request.setSalesQuantity(salesQuantity);
    request.setInheritVatRate(vatRate);
    return request;
  }

  public static AttachedArticleRequest of(final String attachedArticleId, final int quantity,
      final int salesQuantity, Double vatRate) {
    final AttachedArticleRequest request = new AttachedArticleRequest();
    request.setAttachedArticleId(attachedArticleId);
    request.setQuantity(quantity);
    request.setSalesQuantity(salesQuantity);
    request.setInheritVatRate(vatRate);
    return request;
  }
}
