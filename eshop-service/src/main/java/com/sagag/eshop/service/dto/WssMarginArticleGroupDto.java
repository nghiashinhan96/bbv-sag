package com.sagag.eshop.service.dto;

import com.sagag.services.domain.article.WssDesignationsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WssMarginArticleGroupDto implements Serializable {

  private static final long serialVersionUID = -8335857384631256957L;

  private Integer id;

  private Integer orgId;

  @NotBlank(message = "Article group must not be blank")
  private String sagArticleGroup;

  @NotBlank(message = "Custom article group must not be blank")
  private String customArticleGroup;

  @NotBlank(message = "Custom article group description must not be blank")
  private String customArticleGroupDesc;

  @NotNull(message = "First margin must not be null")
  private Double margin1;

  @NotNull(message = "Second margin must not be null")
  private Double margin2;

  @NotNull(message = "Third margin must not be null")
  private Double margin3;

  @NotNull(message = "Fourth margin must not be null")
  private Double margin4;

  @NotNull(message = "Fifth margin must not be null")
  private Double margin5;

  @NotNull(message = "Sixth margin must not be null")
  private Double margin6;

  @NotNull(message = "Seventh margin must not be null")
  private Double margin7;

  private List<WssMarginArticleGroupDto> childMarginArticleGroups;

  private String leafId;

  private String parentLeafId;

  @NotNull(message = "sagArticleGroupDesc must not be null")
  private List<WssDesignationsDto> sagArticleGroupDesc;

  private boolean isMapped;

  private boolean isDefault;

  private boolean hasChild;

  private Integer level;

  private boolean isRoot;
}
