package com.sagag.services.ivds.response.wsp;

import com.sagag.services.domain.eshop.dto.externalvendor.SupportBrandDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversalPartGenArtDto {

  private String gaId;

  private String gaText;

  private boolean isFavorite;

  private String favoriteComment;

  private List<SupportBrandDto> brands;

  public UniversalPartGenArtDto(String gaId, String gaText, List<SupportBrandDto> brands) {
    this.gaId = gaId;
    this.gaText = gaText;
    this.brands = brands;
  }
}
