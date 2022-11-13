package com.sagag.services.domain.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleQuantityDto implements Serializable {

  private static final long serialVersionUID = -7552342678666999494L;

  private Integer qtyStandard;

  private Integer qtyLowest;

  private Integer qtyMultiple;

}
