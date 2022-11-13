package com.sagag.services.haynespro.api;

import com.sagag.services.haynespro.dto.HaynesProOptionDto;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import java.io.BufferedReader;
import java.util.List;
import java.util.Optional;

public interface HaynesProService {

  /**
   * Returns the HaynesPro options for login.
   *
   * @return the list of options for login
   */
  List<HaynesProOptionDto> getHaynesProAccessOptions();

  /**
   * Returns the HaynesPro Access URL.
   *
   * @param request the HaynesPro request
   * @return the access URL
   */
  String getHaynesProAccessUrl(HaynesProAccessUrlRequest request);

  /**
   * Returns the HaynesPro Shopping Cart.
   *
   * @param uuid the user key of user login
   * @param reader the buffered reader from HaynesPro
   * @return the optional of {@link HaynesProShoppingCart}
   */
  Optional<HaynesProShoppingCart> getHaynesProShoppingCart(String key, BufferedReader reader);
}
