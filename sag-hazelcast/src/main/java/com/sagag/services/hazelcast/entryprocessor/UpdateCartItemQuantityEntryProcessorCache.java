package com.sagag.services.hazelcast.entryprocessor;

import com.hazelcast.map.AbstractEntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.ShoppingCartCache;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Optional;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemQuantityEntryProcessorCache
    extends AbstractEntryProcessor<String, ShoppingCartCache> implements DataSerializable {

  private static final long serialVersionUID = -6026802315750692563L;

  @NotNull(message = "CartKey must not be null")
  private String cartKey;

  @NotNull(message = "Quantity must not be null")
  private int quantity;

  @Override
  public Object process(Entry<String, ShoppingCartCache> entry) {
    ShoppingCartCache shoppingCart = entry.getValue();

    Optional<CartItemDto> existing = CollectionUtils.emptyIfNull(shoppingCart.getItems()).stream()
        .filter(sc -> StringUtils.equalsIgnoreCase(cartKey, sc.getCartKey())).findFirst();
    if (existing.isPresent()) {
      int indexOfExisting = shoppingCart.getItems().indexOf(existing.get());
      shoppingCart.getItems().get(indexOfExisting).setQuantity(quantity);
    }
    entry.setValue(shoppingCart);
    return null;
  }

  @Override
  public void writeData(ObjectDataOutput out) throws IOException {
    out.writeObject(cartKey);
    out.writeObject(quantity);
  }

  @Override
  public void readData(ObjectDataInput in) throws IOException {
    cartKey = in.readObject();
    quantity = in.readObject();
  }
}
