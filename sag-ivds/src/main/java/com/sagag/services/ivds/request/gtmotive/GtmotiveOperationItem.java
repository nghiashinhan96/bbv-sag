package com.sagag.services.ivds.request.gtmotive;

import com.sagag.services.gtmotive.domain.response.GtmotiveOperation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.function.Function;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GtmotiveOperationItem implements Serializable {

  private static final long serialVersionUID = -2765458142769097840L;

  private String cupi;

  private String reference;

  private String description;

  public static Function<GtmotiveOperation, GtmotiveOperationItem> converter() {
    return gtOperation -> {
      final GtmotiveOperationItem item = new GtmotiveOperationItem();
      item.setReference(gtOperation.getReference());
      item.setDescription(gtOperation.getDescription().getValue());
      return item;
    };
  }

}
