package com.sagag.services.ivds.converter.wss;

import com.sagag.services.domain.article.WssArtGrpTreeDto;
import com.sagag.services.domain.article.WssArticleGroupDocDto;
import com.sagag.services.domain.article.WssArticleGroupDto;
import com.sagag.services.domain.article.WssArticleGroupDto.WssArticleGroupDtoBuilder;
import com.sagag.services.domain.article.WssDesignationsDto;
import com.sagag.services.elasticsearch.domain.wss.WssArtGrpTree;
import com.sagag.services.elasticsearch.domain.wss.WssArticleGroupDoc;
import com.sagag.services.elasticsearch.domain.wss.WssDesignations;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility provide some converters of WSS Article group.
 */
@UtilityClass
public final class WssArticleGroupConverters {

  public static Function<WssArticleGroupDoc, WssArticleGroupDocDto> simpleWssArticleGroupConverter() {
    return wssArticleGroup -> WssArticleGroupDocDto.builder().id(wssArticleGroup.getId())
        .wssArtGrpTree(convertArticleGroupTreeList(wssArticleGroup.getWssArtGrpTree()))
        .wssDesignations(convertDesignationList(wssArticleGroup.getWssDesignations())).build();
  }

  public static Function<WssArticleGroupDoc, WssArticleGroupDto> wssArticleGroupDtoConverter() {
    return wssArticleGroup -> {
      WssArticleGroupDtoBuilder builder = WssArticleGroupDto.builder();
      Optional<WssArtGrpTreeDto> artGroupTreeOpt =
          convertArticleGroupTree(wssArticleGroup.getWssArtGrpTree());
      if (artGroupTreeOpt.isPresent()) {
        builder.wssArtGrpTree(artGroupTreeOpt.get());
      }
      builder.wssDesignations(convertDesignationList(wssArticleGroup.getWssDesignations()));
      return builder.build();
    };
  }

  private static List<WssDesignationsDto> convertDesignationList(
      List<WssDesignations> wssDesignations) {
    if (CollectionUtils.isEmpty(wssDesignations)) {
      return Lists.emptyList();
    }
    List<WssDesignationsDto> result = new ArrayList<>();
    wssDesignations.stream().forEach(convertWssDesignationItem(result));
    return result;
  }

  private static Consumer<? super WssDesignations> convertWssDesignationItem(
      List<WssDesignationsDto> result) {
    return wssDesignation -> result.add(WssDesignationsDto.builder()
        .language(wssDesignation.getLanguage()).text(wssDesignation.getText()).build());
  }

  private static List<WssArtGrpTreeDto> convertArticleGroupTreeList(
      List<WssArtGrpTree> articleGroupTree) {
    if (CollectionUtils.isEmpty(articleGroupTree)) {
      return Lists.emptyList();
    }
    List<WssArtGrpTreeDto> result = new ArrayList<>();
    articleGroupTree.stream().forEach(convertWssArticleGroupTreeItem(result));
    return result;
  }

  private static Optional<WssArtGrpTreeDto> convertArticleGroupTree(
      List<WssArtGrpTree> articleGroupTree) {
    if (CollectionUtils.isEmpty(articleGroupTree)) {
      return Optional.empty();
    }
    return Optional.ofNullable(buildWssArtGrpTree(articleGroupTree.get(0)));
  }

  private static Consumer<? super WssArtGrpTree> convertWssArticleGroupTreeItem(
      List<WssArtGrpTreeDto> result) {
    return wssArticleGroupTree -> result.add(buildWssArtGrpTree(wssArticleGroupTree));
  }

  private static WssArtGrpTreeDto buildWssArtGrpTree(WssArtGrpTree wssArticleGroupTree) {
    return WssArtGrpTreeDto.builder()
        .artgrp(wssArticleGroupTree.getArtgrp()).leafid(wssArticleGroupTree.getLeafid())
        .parentid(wssArticleGroupTree.getParentid()).sort(wssArticleGroupTree.getSort()).build();
  }

}
