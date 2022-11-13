package com.sagag.services.domain.autonet.erp;

import com.sagag.services.domain.sag.erp.ErpArticleAvailability;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.domain.sag.erp.ErpArticlePrice;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AutonetArticleInfos implements Serializable {

  private static final long serialVersionUID = 830371070265282704L;

  private List<ErpArticlePrice> prices;

  private List<ErpArticleMemo> memos;

  private ErpArticleAvailability availability;
}
