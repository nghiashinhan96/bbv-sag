package com.sagag.services.domain.sag.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.sag.erp.Article;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class InvoicePositionDto implements Serializable {

  private static final long serialVersionUID = 2517049236676063196L;

  private String invoiceNr;

  private String articleId;

  private String articleNr;

  private String articleTitle;

  private Double amount;

  private String vehicleInfo;

  private Integer quantity;

  private String orderNr;

  private String deliveryNoteNr;

  @JsonIgnore
  private Integer sequence;

  @JsonIgnore
  private Article article;
}
