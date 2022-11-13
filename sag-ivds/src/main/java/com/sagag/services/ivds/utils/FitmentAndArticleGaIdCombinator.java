package com.sagag.services.ivds.utils;

import com.google.common.collect.Lists;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class FitmentAndArticleGaIdCombinator {

  public void combineGaIds(List<ArticleDocDto> articleDtos,
    List<VehicleGenArtArtDoc> vehicleGenArtArticles) {

    Map<String, Integer> articleIdGaidMap = getGaidArticleIdMap(vehicleGenArtArticles);
    articleDtos.stream().forEach(articleDto -> {
      // Update combined gen_art_id
      articleDto.setCombinedGenArtIds(
        combineGenArtIdListBetweenVehicleGenArtArtAndArticleIndex(
          vehicleGenArtArticles, articleDto.getGaId(), articleDto.getId()));
      // Bind gaid from vehicle genArtArt
      if (Objects.nonNull(articleIdGaidMap.get(articleDto.getArtid()))) {
        articleDto.setGaId(articleIdGaidMap.get(articleDto.getArtid()).toString());
      }
    });
  }

  private static List<String> combineGenArtIdListBetweenVehicleGenArtArtAndArticleIndex(
      final List<VehicleGenArtArtDoc> vehicleGenArtArtList, final String genArtId,
      final String esIdArticle) {
      if (CollectionUtils.isEmpty(vehicleGenArtArtList) && StringUtils.isBlank(genArtId)
        && StringUtils.isBlank(esIdArticle)) {
        return Collections.emptyList();
      }
      if (CollectionUtils.isEmpty(vehicleGenArtArtList)) {
        return Lists.newArrayList(genArtId);
      }
      final List<String> combinedGenArtIds = Lists.newArrayList(vehicleGenArtArtList.stream()
          .filter(filterVehicleGenArtArtByArtId(esIdArticle))
          .map(vehGenArtArtDoc -> String.valueOf(vehGenArtArtDoc.getGenart()))
          .collect(Collectors.toList()));
      if (StringUtils.isBlank(genArtId)) {
        return combinedGenArtIds;
      }
      combinedGenArtIds.add(genArtId);
      return combinedGenArtIds;
    }

    private static Predicate<VehicleGenArtArtDoc> filterVehicleGenArtArtByArtId(
      final String esIdArticle) {
      return vehicleGenArtArtDoc -> CollectionUtils.isNotEmpty(vehicleGenArtArtDoc.getArticles())
        && vehicleGenArtArtDoc.getArticles().stream()
        .anyMatch(article -> StringUtils.equalsIgnoreCase(article.getArtId(), esIdArticle));
    }

    private static Map<String, Integer> getGaidArticleIdMap(List<VehicleGenArtArtDoc> vehicleGenArtArtDocs) {
      Map<String, Integer> artIdGaidMap = new HashMap<>();
      vehicleGenArtArtDocs.forEach(vehicleGenArt -> {
        if (!CollectionUtils.isEmpty(vehicleGenArt.getArticles())) {
          vehicleGenArt.getArticles().forEach(
              article -> artIdGaidMap.putIfAbsent(article.getArtId(), vehicleGenArt.getGenart()));
        }
      });
      return artIdGaidMap;
    }

}
