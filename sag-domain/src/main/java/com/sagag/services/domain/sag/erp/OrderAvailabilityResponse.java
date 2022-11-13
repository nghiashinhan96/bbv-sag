package com.sagag.services.domain.sag.erp;

import com.sagag.services.domain.article.ArticleDocDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAvailabilityResponse {

  private ArticleDocDto article;

  private Availability availabilityResponse;

  private boolean recycle;
  private boolean depot;
  private boolean pfand;
  private boolean voc;
  private boolean vrg;

  private List<ArticleDocDto> attachArticles;

  private String itemType;

}
