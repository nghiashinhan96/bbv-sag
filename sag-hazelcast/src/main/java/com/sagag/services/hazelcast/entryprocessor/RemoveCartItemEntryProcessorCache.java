package com.sagag.services.hazelcast.entryprocessor;

import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class RemoveCartItemEntryProcessorCache
    extends AbstractEntryProcessor<String, ShoppingCartCache> implements DataSerializable {

  private static final long serialVersionUID = -2920393836805047465L;

  @NotNull(message = "CartKey must not be null")
  private String cartKey;

  @Override
  public Object process(Entry<String, ShoppingCartCache> entry) {
    ShoppingCartCache shoppingCart = entry.getValue();
    List<CartItemDto> cartItems =
        ListUtils.defaultIfNull(shoppingCart.getItems(), new ArrayList<>());
    Optional<CartItemDto> existingCartItem =
        cartItems.stream().filter(ci -> StringUtils.equals(ci.getCartKey(), cartKey)).findFirst();
    if (existingCartItem.isPresent()) {
      shoppingCart.getItems().remove(existingCartItem.get());
    }
    entry.setValue(shoppingCart);
    return null;
  }

  @Override
  public void writeData(ObjectDataOutput out) throws IOException {
    out.writeObject(cartKey);
  }

  @Override
  public void readData(ObjectDataInput in) throws IOException {
    cartKey = in.readObject();
  }

}
