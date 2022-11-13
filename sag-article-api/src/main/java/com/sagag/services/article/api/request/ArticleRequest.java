package com.sagag.services.article.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Class to contain article info to send to ERP.
 *
 */
@Data
public class ArticleRequest implements Serializable {

  private static final long serialVersionUID = 1663542058608182629L;

  private final String id;
  private final int quantity;
  private String additionalTextDoc;
  private String additionalTextDocPrinters;
  private String registrationDocNr;
  private String sourcingType;
  private String vendorId;
  private String arrivalTime;
  private Long brandId;
  private String priceDiscTypeId;
  private String brand;

}
