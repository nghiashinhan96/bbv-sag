package com.sagag.services.ivds.response;

import com.sagag.services.domain.article.ArticleDocDto;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class GtmotiveCupisResponse implements Serializable {

  private static final long serialVersionUID = -504803819196374653L;
  private List<String> cateIds;
  private Page<ArticleDocDto> articles;
}
