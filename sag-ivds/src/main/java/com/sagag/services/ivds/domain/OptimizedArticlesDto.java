package com.sagag.services.ivds.domain;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptimizedArticlesDto {

  private Page<ArticleDocDto> articles;

  private Map<String, List<SagBucket>> aggregations;

}
