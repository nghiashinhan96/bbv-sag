package com.sagag.services.service.exporter.wss;

import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.WssDesignationsDto;
import com.sagag.services.elasticsearch.domain.wss.WssArtGrpTree;
import com.sagag.services.elasticsearch.domain.wss.WssArticleGroupDoc;
import com.sagag.services.elasticsearch.domain.wss.WssDesignations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WssMarginArticleGroupExportItemDto {
  private String sagArticleGroup;
  private String sagArticleGroupDesc;
  private String customArticleGroup;
  private String customArticleGroupDesc;
  private Double margin1;
  private Double margin2;
  private Double margin3;
  private Double margin4;
  private Double margin5;
  private Double margin6;
  private Double margin7;
  private String parentid;
  private String leafid;
  private boolean isMapped;

  public WssMarginArticleGroupExportItemDto(WssMarginsArticleGroup marginsArticleGroup,
      String languageCode) {
    this.sagArticleGroup = marginsArticleGroup.getSagArtGroup();
    this.sagArticleGroupDesc = StringUtils.EMPTY;
    List<WssDesignationsDto> sagArticleGroupDescList = SagJSONUtil.convertArrayJsonToList(
        marginsArticleGroup.getSagArticleGroupDesc(), WssDesignationsDto.class);
    Optional<WssDesignationsDto> sagArticleGroupDescByLangOpt =
        sagArticleGroupDescList.stream().filter(articleGroupDesc -> StringUtils
            .equalsIgnoreCase(articleGroupDesc.getLanguage(), languageCode)).findFirst();
    if (sagArticleGroupDescByLangOpt.isPresent()) {
      this.sagArticleGroupDesc = sagArticleGroupDescByLangOpt.get().getText();
    }
    this.customArticleGroup = marginsArticleGroup.getCustomArticleGroup();
    this.customArticleGroupDesc = marginsArticleGroup.getCustomArticleGroupDesc();
    this.margin1 = marginsArticleGroup.getMargin1();
    this.margin2 = marginsArticleGroup.getMargin2();
    this.margin3 = marginsArticleGroup.getMargin3();
    this.margin4 = marginsArticleGroup.getMargin4();
    this.margin5 = marginsArticleGroup.getMargin5();
    this.margin6 = marginsArticleGroup.getMargin6();
    this.margin7 = marginsArticleGroup.getMargin7();
    this.parentid = marginsArticleGroup.getParentLeafId();
    this.leafid = marginsArticleGroup.getLeafId();
    this.isMapped = marginsArticleGroup.isMapped();
  }

  public WssMarginArticleGroupExportItemDto(WssArticleGroupDoc wssrticleGroup,
      String languageCode) {
    if (CollectionUtils.isNotEmpty(wssrticleGroup.getWssArtGrpTree())) {
      WssArtGrpTree wssArtGrpTree = wssrticleGroup.getWssArtGrpTree().get(0);
      this.sagArticleGroup = wssArtGrpTree.getArtgrp();
      this.parentid = wssArtGrpTree.getParentid();
      this.leafid = wssArtGrpTree.getLeafid();
    }
    this.sagArticleGroupDesc = wssrticleGroup
        .getWssDesignations().stream().filter(articleGroupDesc -> StringUtils
            .equalsIgnoreCase(articleGroupDesc.getLanguage(), languageCode))
        .findFirst().map(WssDesignations::getText).orElse(StringUtils.EMPTY);
  }

}
