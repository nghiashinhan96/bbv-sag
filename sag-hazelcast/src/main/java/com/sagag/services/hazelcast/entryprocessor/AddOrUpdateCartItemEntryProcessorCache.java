package com.sagag.services.hazelcast.entryprocessor;

import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.hazelcast.util.StringUtil;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class AddOrUpdateCartItemEntryProcessorCache
    extends AbstractEntryProcessor<String, ShoppingCartCache> implements DataSerializable {

  private static final long serialVersionUID = 7930749910796736863L;

  @NotNull(message = "CartKey must not be null")
  private String cartKey;

  @NotNull(message = "Input CartItem must not be null")
  private CartItemDto cartItem;

  @Override
  public Object process(Entry<String, ShoppingCartCache> entry) {
    ShoppingCartCache shoppingCart = entry.getValue();
    List<CartItemDto> items = ListUtils.defaultIfNull(shoppingCart.getItems(), new ArrayList<>());
    Optional<CartItemDto> existingItem = items.stream()
        .filter(item -> StringUtil.equalsIgnoreCase(item.getCartKey(), cartKey)).findFirst();
    if (existingItem.isPresent()) {
      int indexOfExistedItem = items.indexOf(existingItem.get());
      shoppingCart.getItems().set(indexOfExistedItem, cartItem);
    } else {
      shoppingCart.getItems().add(cartItem);
    }
    entry.setValue(shoppingCart);
    return null;
  }

  @Override
  public void writeData(ObjectDataOutput out) throws IOException {
    out.writeObject(cartItem);
    out.writeObject(cartKey);
  }

  @Override
  public void readData(ObjectDataInput in) throws IOException {
    cartItem = in.readObject();
    cartKey = in.readObject();
  }

}
