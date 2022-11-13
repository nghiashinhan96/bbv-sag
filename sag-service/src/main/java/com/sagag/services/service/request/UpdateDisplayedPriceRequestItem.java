package com.sagag.services.service.request;

import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class UpdateDisplayedPriceRequestItem implements Serializable{

  private static final long serialVersionUID = 766499409167299316L;

  private String cartKey;

  private DisplayedPriceDto displayedPrice;

}
