package com.sagag.services.ax.domain;

import com.sagag.services.article.api.price.DisplayedPriceRequestCriteria;
import com.sagag.services.ax.request.AxPriceRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AxDisplayedPriceRequestBody extends DisplayedPriceRequestCriteria {

  private AxPriceRequest axPriceRequest;

}
