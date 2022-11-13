package com.sagag.services.ivds.response.wsp;

import com.sagag.services.domain.eshop.category.GenArtDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversalPartArticleSearchResponse {

  private List<UniversalPartGenArtDto> genArts;
  
  private List<GenArtDto> brandPrios;

}
