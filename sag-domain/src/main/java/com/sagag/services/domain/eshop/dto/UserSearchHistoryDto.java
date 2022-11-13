package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * User search history Dto class.
 */
@Data
@Builder
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@AllArgsConstructor
public class UserSearchHistoryDto implements Serializable {

  private static final long serialVersionUID = 5539761315923330513L;

  private List<VehicleHistoryDto> vehHistories;

  private List<ArticleHistoryDto> artHistories;

  private List<EshopFavoriteDto> unipartFavotite;

}
