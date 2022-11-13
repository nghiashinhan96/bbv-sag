package com.sagag.services.service.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {

  private static final long serialVersionUID = 1683683958421792826L;

  private String cartKey;

  private String additionalTextDoc;

}
