package com.sagag.services.ivds.utils;

import com.sagag.services.domain.article.ArticleCriteriaDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.CriteriaProperty;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class ArticlesLocationUtils {

  public static List<ArticleDocDto> filterArticlesByLocation(Collection<CategoryDoc> cates,
      List<ArticleDocDto> articles) {
    List<CriteriaProperty> cateCriterias =
        CollectionUtils.emptyIfNull(cates).stream().flatMap(cate -> cate.getGenarts().stream())
            .flatMap(genart -> genart.getCriteria().stream()).collect(Collectors.toList());
    return CollectionUtils.emptyIfNull(articles).stream()
        .filter(filterArticleByLocation(cateCriterias)).collect(Collectors.toList());
  }

  private static Predicate<ArticleDocDto> filterArticleByLocation(
      List<CriteriaProperty> cateCriterias) {
    return article -> {
      if (CollectionUtils.isEmpty(cateCriterias)) {
        return true;
      }
      return article.getCriteria().stream().anyMatch(matchCriterias(cateCriterias));
    };
  }

  private static Predicate<ArticleCriteriaDto> matchCriterias(
      List<CriteriaProperty> cateCriterias) {
    return articleCriteria -> cateCriterias.stream().anyMatch(matchCriteria(articleCriteria));

  }

  private static Predicate<CriteriaProperty> matchCriteria(ArticleCriteriaDto articleCriteria) {
    return cateCriteria -> cateCriteria.getCid().equals(articleCriteria.getCid())
        && cateCriteria.getCvp().equalsIgnoreCase(articleCriteria.getCvp());
  }
}
