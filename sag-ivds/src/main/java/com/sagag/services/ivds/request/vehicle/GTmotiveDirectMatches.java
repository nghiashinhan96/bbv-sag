package com.sagag.services.ivds.request.vehicle;

import com.sagag.services.domain.article.ArticleDocDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GTmotiveDirectMatches {

  private List<ArticleDocDto> directMatchesArticles;
  
  private String pseudoCategoryName;
  
  private String reference;
  
}
