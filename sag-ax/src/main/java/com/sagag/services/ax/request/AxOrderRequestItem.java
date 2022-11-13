package com.sagag.services.ax.request;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.article.api.request.BasketPosition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The class to build ax basket item.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AxOrderRequestItem extends AxBasketItem implements Serializable {

  private static final long serialVersionUID = 1L;

  private String additionalTextDoc;

  private String additionalTextDocPrinters;

  private String priceDiscTypeId;

  public static AxOrderRequestItem copyWithCreateOrder(BasketPosition position) {
    AxOrderRequestItem basketItem = new AxOrderRequestItem();
    basketItem.setArticleId(position.getArticleId());
    basketItem.setQuantity(position.getQuantity());
    if (Objects.nonNull(position.getBrandId())) {
      basketItem.setBrandId(position.getBrandId().intValue());
    }
    basketItem.setRegistrationDocNr(position.getRegistrationDocNr());
    basketItem.setAdditionalTextDoc(position.getAdditionalTextDoc());
    basketItem.setAdditionalTextDocPrinters(position.getAdditionalTextDocPrinters());
    basketItem.setSourcingType(position.getSourcingType());
    basketItem.setVendorId(position.getVendorId());
    basketItem.setArrivalTime(position.getArrivalTime());

    if (!StringUtils.isBlank(position.getBrand())) {
      basketItem.setBrand(position.getBrand());
    }
    if (!StringUtils.isBlank(position.getModel())) {
      basketItem.setModel(position.getModel());
    }
    if (!StringUtils.isBlank(position.getType())) {
      basketItem.setType(position.getType());
    }
    if (!StringUtils.isBlank(position.getPriceDiscTypeId())) {
      basketItem.setPriceDiscTypeId(position.getPriceDiscTypeId());
    }
    return basketItem;
  }
}
