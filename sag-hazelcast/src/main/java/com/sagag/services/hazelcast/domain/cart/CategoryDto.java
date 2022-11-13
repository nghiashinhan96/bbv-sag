package com.sagag.services.hazelcast.domain.cart;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {

  private static final long serialVersionUID = 6381156308196242445L;

  private String gaId;
  private String gaDesc;
  private String rootDesc;

}
