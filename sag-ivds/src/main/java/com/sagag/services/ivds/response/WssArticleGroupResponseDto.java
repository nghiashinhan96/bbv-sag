package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.domain.article.WssArticleGroupDocDto;

import lombok.Data;

import org.springframework.data.domain.Page;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WssArticleGroupResponseDto implements Serializable {

  private static final long serialVersionUID = -8006952702118126774L;

  private Page<WssArticleGroupDocDto> articleGroups;

}
