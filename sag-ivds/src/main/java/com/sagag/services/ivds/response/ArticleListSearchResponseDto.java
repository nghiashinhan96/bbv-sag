package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;

import lombok.Data;

import org.springframework.data.domain.Page;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "articles", "olyslagerTypeIds", "olyslagerPopup" })
public class ArticleListSearchResponseDto {

  private Page<ArticleDocDto> articles;

  private List<OilTypeIdsDto> oilTypeIds;

}
