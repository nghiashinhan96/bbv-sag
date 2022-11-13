package com.sagag.services.article.api.attachedarticle;

import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface AttachedArticleRequestBuilder {

  /**
   * Builds attached article request list.
   *
   * @param objects
   * @return the list of <code>AttachedArticleRequest</code>
   */
  List<AttachedArticleRequest> buildAttachedAticleRequestList(Object... objects);

  /**
   * Builds attached article deposit request list.
   *
   * @param articles
   * @return the list of <code>AttachedArticleRequest</code>
   */
  List<AttachedArticleRequest> buildAttachedArticleDepositRequestList(List<ArticleDocDto> articles);

  static String extractVehicleId(VehicleDto vehicle) {
    return vehicle != null ? vehicle.getId() : StringUtils.EMPTY;
  }
}
