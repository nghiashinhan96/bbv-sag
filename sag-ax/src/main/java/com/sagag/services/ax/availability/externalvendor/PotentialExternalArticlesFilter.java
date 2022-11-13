package com.sagag.services.ax.availability.externalvendor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

@Component
@AxProfile
public class PotentialExternalArticlesFilter {

  public List<ArticleDocDto> filterArticlesCouldBeSuppliedByExternalVendor(
      List<ArticleDocDto> articles, List<ExternalVendorDto> extVendors) {
    return articles.stream().filter(art -> filterArticle(art, sortExternalVendor(extVendors)))
        .collect(Collectors.toList());
  }

  private boolean filterArticle(ArticleDocDto article, List<ExternalVendorDto> extVendors) {
    final List<String> sagProductGroups =
        Arrays.asList(article.getSagProductGroup(), article.getSagProductGroup2(),
            article.getSagProductGroup3(), article.getSagProductGroup4());
    final String idDlnr = article.getIdDlnr();

    return extVendors.stream()
        .filter(vendor -> matchFilterCondition(vendor, idDlnr, sagProductGroups)).findAny()
        .map(vendor -> Objects.nonNull(vendor.getVendorId())).orElse(false);
  }

  private boolean matchFilterCondition(ExternalVendorDto extVendor, String idDlnr,
      List<String> sagProductGroups) {
    return ExternalVendorUtils.matchSagProductGroups(extVendor.getSagArticleGroup(),
        sagProductGroups) && ExternalVendorUtils.matchBrandId(extVendor.getBrandId(), idDlnr);
  }

  private List<ExternalVendorDto> sortExternalVendor(List<ExternalVendorDto> extVendors) {
    extVendors.sort(Comparator.comparing(ExternalVendorDto::getBrandId, sortByBrandId())
        .thenComparing(ExternalVendorDto::getSagArticleGroup, sortByArticleGroup()));
    return extVendors;
  }

  private Comparator<Long> sortByBrandId() {
    return (a, b) -> Boolean.compare(Objects.nonNull(b), Objects.nonNull(a));
  }

  private Comparator<String> sortByArticleGroup() {
    return (a, b) -> Boolean.compare(StringUtils.isNoneBlank(b), StringUtils.isNoneBlank(a));
  }
}
