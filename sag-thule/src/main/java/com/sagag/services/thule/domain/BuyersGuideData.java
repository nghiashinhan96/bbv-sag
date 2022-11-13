package com.sagag.services.thule.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BuyersGuideData implements Serializable {

  private static final long serialVersionUID = 7909811929994852687L;

  @NonNull
  private String dealerId;

  private List<BuyersGuideOrder> orders;

}
