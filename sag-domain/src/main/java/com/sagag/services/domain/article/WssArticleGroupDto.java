package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class WssArticleGroupDto implements Serializable {

  private static final long serialVersionUID = -4081514796684908103L;

  private WssArtGrpTreeDto wssArtGrpTree;

  private List<WssDesignationsDto> wssDesignations;
}
