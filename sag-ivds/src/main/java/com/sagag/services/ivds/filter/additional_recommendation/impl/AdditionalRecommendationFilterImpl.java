package com.sagag.services.ivds.filter.additional_recommendation.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleInfoDto;
import com.sagag.services.hazelcast.api.OatesAdditionalRecommendationsCacheService;
import com.sagag.services.ivds.filter.additional_recommendation.AdditionalRecommendationFilter;
import com.sagag.services.oates.dto.OatesApplicationDto;
import com.sagag.services.oates.dto.ProductResourceDto;
import com.sagag.services.oates.dto.RecommendProductDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AdditionalRecommendationFilterImpl implements AdditionalRecommendationFilter {

  private static final String ADDITIONAL_RECOMMENDATION_NOTE_CLASS = "lube";
  private static final String ADDITIONAL_RECOMMENDATION_INFO_TYPE = "ADD_REC";

  @Autowired
  private OatesAdditionalRecommendationsCacheService additionalRecommendationsCacheService;

  @Autowired
  protected MessageSource messageSource;

  @Override
  public Optional<ArticleInfoDto> filterAdditionalRecommendation(UserInfo user) {
    final String additionalRecommendationText = getAdditionalRecommendationText(user);
    final ArticleInfoDto articleInfo =
        ArticleInfoDto.builder().infoType(ADDITIONAL_RECOMMENDATION_INFO_TYPE)
            .infoTxtDe(additionalRecommendationText).build();
    return Optional.of(articleInfo);
  }

  @Override
  public List<String> filterAdditionalRecommendationArticles(UserInfo user,
      List<OatesApplicationDto> filteredApplications) {
    final String additionalRecommendationText = getAdditionalRecommendationText(user);
    List<String> separatedArticleNames =
        extractAdditionalRecommendArticleNames(filteredApplications, additionalRecommendationText);

    if (CollectionUtils.isEmpty(separatedArticleNames)) {
      return Lists.emptyList();
    }
    Set<String> axIds = extractAddtionalRecommendAxIds(separatedArticleNames);
    return Lists.newArrayList(axIds);
  }

  private Set<String> extractAddtionalRecommendAxIds(List<String> separatedArticleNames) {
    separatedArticleNames.replaceAll(String::trim);
    final Map<String, RecommendProductDto> searchByNames =
        additionalRecommendationsCacheService.searchByNames(separatedArticleNames);
    List<ProductResourceDto> productResources = Lists.newArrayList();
    Set<String> axIds = new HashSet<>();
    searchByNames.values().forEach(product -> productResources.addAll(product.getResources()));
    String regex = ":\\\"(.*?)\\\"";
    Pattern p = Pattern.compile(regex);
    productResources.stream().forEach(resource -> {
      Matcher m = p.matcher(resource.getUri());
      while (m.find()) {
        axIds.add(m.group(1));
      }
    });
    return axIds;
  }

  private List<String> extractAdditionalRecommendArticleNames(
      List<OatesApplicationDto> filteredApplications, final String additionalRecommendationText) {
    final Set<String> appNoteTexts =
        filteredApplications.stream()
            .flatMap(application -> CollectionUtils.emptyIfNull(application.getOriginAppNotes())
                .stream()
                .filter(appNote -> ADDITIONAL_RECOMMENDATION_NOTE_CLASS
                    .equalsIgnoreCase(appNote.getNoteClass()))
                .map(appNote -> appNote.getText()))
            .collect(Collectors.toSet());
    appNoteTexts.removeIf(text -> !StringUtils.contains(text, additionalRecommendationText));
    final Set<String> additionalArticleNames = appNoteTexts.stream()
        .map(noteText -> StringUtils.substringAfter(noteText, SagConstants.COLON))
        .collect(Collectors.toSet());
    List<String> separatedArticleNames = Lists.newArrayList();
    additionalArticleNames.forEach(articleNames -> separatedArticleNames
        .addAll(Lists.newArrayList(StringUtils.split(articleNames, SagConstants.SLASH))));
    return separatedArticleNames;
  }

  private String getAdditionalRecommendationText(UserInfo user) {
    final Locale locale = user.getUserLocale();
    return messageSource.getMessage("oates.additional_recommendation", null, locale);
  }

}
