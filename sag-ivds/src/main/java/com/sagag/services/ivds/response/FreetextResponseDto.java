package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.unitree.FreetextUnitreeResponse;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;

import lombok.Data;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "vehData", "unitreeData", "articles" })
public class FreetextResponseDto implements Serializable {

  private static final long serialVersionUID = -1736636083907341398L;

  private FreetextVehicleResponse vehData;

  private FreetextUnitreeResponse unitreeData;

  private Page<ArticleDocDto> articles;

  private String contextKey;

  public boolean isVehiclePresent() {
    return !Objects.isNull(vehData) && vehData.getVehicles().hasContent();
  }

  public boolean isUnitreePresent() {
    return !Objects.isNull(unitreeData) && unitreeData.getUnitrees().hasContent();
  }

  public boolean isArticlePresent() {
    return !Objects.isNull(articles) && articles.hasContent();
  }
}
