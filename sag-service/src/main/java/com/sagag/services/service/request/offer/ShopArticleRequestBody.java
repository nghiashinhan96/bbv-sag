package com.sagag.services.service.request.offer;

import com.sagag.services.common.enums.offer.ShopArticleType;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopArticleRequestBody implements Serializable {

  private static final long serialVersionUID = 4542050380424392192L;

  private String articleNumber;

  private String name;

  private String description;

  private double amount;

  private double price;

  private ShopArticleType type;

}
