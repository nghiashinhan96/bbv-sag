package com.sagag.services.domain.eshop.uniparts_favorite.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class EshopFavoriteRequestDto implements Serializable {

  private static final long serialVersionUID = 1958082400211378546L;

  private String keySearch;

  private String type;

  private int page = 0;

  private int size = 10;

}
