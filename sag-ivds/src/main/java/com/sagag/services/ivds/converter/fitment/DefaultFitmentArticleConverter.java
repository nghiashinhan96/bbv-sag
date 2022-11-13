package com.sagag.services.ivds.converter.fitment;

import com.sagag.services.domain.article.FitmentArticleDto;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultFitmentArticleConverter extends FitmentArticleConverter {

  @Override
  public List<FitmentArticleDto> apply(List<VehicleGenArtArtDoc> vehicleGenArtArticles) {
    return getArticlesFromVehicleGenArtArt(vehicleGenArtArticles, false);
  }

}
