package com.sagag.services.article.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Request basket position definition class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasketPosition implements Serializable {

  private static final long serialVersionUID = -3659351866490252455L;

  /**
   * The umarId of SAG-sys identifies a set of identically constructed spare parts. The attribute
   * will not be supported in upcoming AX ERP system. Therefore, it is not recommended to use it at
   * all. <br>
   * Applications relying on umarId will most likely break after introduction of new ERP system.
   */
  private Long umarId;

  /**
   * The id of the article to be ordered, checked for availability, or get a price for. Besides the
   * articleId there still exists deprecated umarId, which will not be supported by upcoming
   * AX-based ERP system. It is therefore recommended to provide this articleId. Note: Setting of
   * article or UMAR identifier must be consistent with flags in request object that hosts basket
   * positions, eg. orderSpecificArticleIds of OrderRequest.
   */
  private String articleId;

  /** Requested quantity of article, which need to be greater than zero. */
  private Integer quantity;

  private Long brandId;

  /**
   * Additional text which will be included in the position and will be printed in documents, e.g.
   * offer, invoice etc., selected by additionalTextDocPrinters
   */
  private String additionalTextDoc;

  /**
   * Codes indicating in which documents the additional text will be printed. If a text exists and
   * no codes for printers are set, then it will be set to ALL documents by default.
   */
  private String additionalTextDocPrinters;

  /**
   * The number of the official document that comes along with a (new) car from the producer
   * containing all the relevant technical details, serial numbers etc.
   */
  private String registrationDocNr;

  private String brand;

  private String model;

  private String type;

  private String externalArticleId;

  private String sourcingType;

  private String vendorId;

  private String arrivalTime;

  private String priceDiscTypeId;

  public void addQuantity(final int quantity) {
    setQuantity(getQuantity() + quantity);
  }

}
