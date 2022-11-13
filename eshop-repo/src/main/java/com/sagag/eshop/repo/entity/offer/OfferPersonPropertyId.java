package com.sagag.eshop.repo.entity.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferPersonPropertyId implements Serializable {

  private static final long serialVersionUID = -4674418525559655514L;

  private long personId;

  private String type;
}
