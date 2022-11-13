package com.sagag.services.hazelcast.domain.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomShoppingCart implements Serializable {

  private static final long serialVersionUID = -3561685922256439808L;

  private ShoppingCart shoppingCart;

  private List<String> notValidArticleNumbers;

}
