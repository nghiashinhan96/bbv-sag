package com.sagag.services.oates.api;

import com.sagag.services.oates.dto.RecommendProductDto;
import java.util.List;

public interface OatesAdditionalRecommendationsService {

  /**
   * Returns all recommendation products.
   *
   * @return the list of recommendation product
   */
  List<RecommendProductDto> getAllRecommendProducts();

}
