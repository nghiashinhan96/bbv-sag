package com.sagag.services.domain.sag.erp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErpArticleMemo implements Serializable {

  private static final long serialVersionUID = 3197338017920287755L;

  private int type;

  private String text;

  private String label;

  private String statusKey;

  private String statusValue;

  private String icon;

  private String link;
}
