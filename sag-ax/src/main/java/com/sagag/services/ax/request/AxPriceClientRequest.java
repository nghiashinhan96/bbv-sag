package com.sagag.services.ax.request;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.sagag.services.article.api.request.PriceRequest;

import lombok.Data;

/**
 * The class to build AX price request send to AX API.
 *
 */
@Data
public class AxPriceClientRequest implements Serializable {

  private static final long serialVersionUID = 6545875670570705554L;

  private String customerNr;

  private List<AxBasketItem> items;

  public static AxPriceClientRequest copy(PriceRequest request) {
    AxPriceRequest req = (AxPriceRequest) request;
    AxPriceClientRequest clientRequest = new AxPriceClientRequest();
    clientRequest.setCustomerNr(req.getCustomerNr());
    List<AxBasketItem> items = req.getBasketPositions().stream()
        .map(AxBasketItem::copyWithGetPrices).collect(Collectors.toList());
    clientRequest.setItems(items);
    return clientRequest;
  }

}
