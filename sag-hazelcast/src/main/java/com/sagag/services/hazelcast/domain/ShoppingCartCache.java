package com.sagag.services.hazelcast.domain;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartCache implements Serializable, DataSerializable{

  private static final long serialVersionUID = -2771625586375557781L;

  private List<CartItemDto> items;

  @Override
  public void writeData(ObjectDataOutput out) throws IOException {
    out.writeObject(items);

  }

  @Override
  public void readData(ObjectDataInput in) throws IOException {
    items = in.readObject();

  }
}
