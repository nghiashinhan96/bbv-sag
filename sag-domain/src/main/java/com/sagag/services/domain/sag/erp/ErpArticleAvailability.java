package com.sagag.services.domain.sag.erp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErpArticleAvailability implements Serializable {

  private static final long serialVersionUID = -1625566447703559070L;

  private int availState;

  private String availStateColor;

  private String rawArrivalTime;

}
