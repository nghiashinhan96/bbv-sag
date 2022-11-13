package com.sagag.services.ivds.converter.fitment;

import com.sagag.services.domain.article.FitmentArticleDto;
import com.sagag.services.elasticsearch.domain.article.FitmentArticle;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class FitmentArticleConverter
  implements Function<List<VehicleGenArtArtDoc>, List<FitmentArticleDto>> {

  @Autowired
  private FitmentArticleMapper fitmentArticleMapper;

  protected List<FitmentArticleDto> getArticlesFromVehicleGenArtArt(
      final List<VehicleGenArtArtDoc> vehicleGenArtArtDocs, final boolean distinct) {
    if (!distinct) {
      return vehicleGenArtArtDocs.stream()
          .flatMap(doc -> doc.getArticles().stream())
          .map(fitmentArticleMapper.fitmentConverter())
          .collect(Collectors.toList());
    }
    return vehicleGenArtArtDocs.stream()
        .flatMap(doc -> doc.getArticles().stream())
        .filter(distinctByKey(fitmentArticleMapper.keyExtractor()))
        .map(fitmentArticleMapper.fitmentConverter())
        .collect(Collectors.toList());
  }

  private static Predicate<FitmentArticle> distinctByKey(
      Function<FitmentArticle, String> keyExtractor) {
    Map<String, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }
}
