package com.sagag.services.ivds.utils;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagStringUtils;
import com.sagag.services.domain.article.ArticleCriteriaDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaElement;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The text builder to generate part description for each article.
 *
 */
@Component
public class PartDescriptionBuilder {

  private static final String SHOW_CRITERIA = "TRUE";

  private static final String PREFIX_NEW_LINE = "\\n";

  private static final String HYPHEN_HAS_SPACE_DELIMITER = " - ";

  private static final String CVP_DELIMITER = SagConstants.SLASH;

  private static final String SPACE = StringUtils.SPACE;

  private static final String[] CONSTRUCTION_YEAR_CIDS = new String[] { "20", "21", "22", "9253" };

  @LogExecutionTime
  public String build(ArticleDocDto article, FormatGaDoc formatGaDoc) {
    Assert.notNull(article, "The given article to build part description must not be null");
    return buildPartDescription(article, formatGaDoc);
  }

  /**
   * Build part description base on criteria list.
   *
   */
  private static String buildPartDescription(ArticleDocDto articleDoc, FormatGaDoc formatGaDoc) {
    StringBuilder partDescBuilder = new StringBuilder();
    if (!isValidFormatGaDoc(formatGaDoc)) {
      buildConstructionYearsTxt(partDescBuilder, articleDoc.getCriteria());
      return beautifyPartDesc(partDescBuilder, StringUtils.EMPTY, HYPHEN_HAS_SPACE_DELIMITER);
    }

    final List<FormatGaElement> elements = formatGaDoc.getElements();
    final List<ArticleCriteriaDto> filteredCriterias =
        filterCriteriaByFormatGaElements(articleDoc.getCriteria(), elements);
    final Map<String, List<ArticleCriteriaDto>> criteriaMap =
        initCriteriaMap(filteredCriterias);

    String currentDelimiter = null;
    Integer prevSort = null;
    Integer prevOrder = null;
    boolean hasSort = false;
    List<ArticleCriteriaDto> criterias;
    for (FormatGaElement element : elements) {
      criterias = criteriaMap.get(String.valueOf(element.getCid()));

      SortedFormatGaResult result = null;
      hasSort = StringUtils.isNotBlank(element.getSort());
      if (hasSort) {
        SortedFormatGaCriteria sortCriteria =
            new SortedFormatGaCriteria(prevSort, prevOrder, element);
        result = getSortedFormatGaResult(sortCriteria);
      }
      if (CollectionUtils.isEmpty(criterias) || (hasSort && result == null)) {
        continue;
      }
      if (result != null) {
        prevSort = result.getCurrentSort();
        prevOrder = result.getCurrentOrder();
      }

      if (hasDelimiter(currentDelimiter, element)) {
        currentDelimiter = element.getDelimiter().trim();
      }

      // Update part description
      updatePartDescStr(partDescBuilder, criterias, element);
      if (partDescBuilder.length() > 0) {
        partDescBuilder.append(HYPHEN_HAS_SPACE_DELIMITER);
      }
    }

    partDescBuilder = new StringBuilder(
        beautifyPartDesc(partDescBuilder, currentDelimiter, HYPHEN_HAS_SPACE_DELIMITER));
    buildConstructionYearsTxt(partDescBuilder, articleDoc.getCriteria());

    return beautifyPartDesc(partDescBuilder, currentDelimiter, CVP_DELIMITER,
        HYPHEN_HAS_SPACE_DELIMITER);
  }

  private static SortedFormatGaResult getSortedFormatGaResult(SortedFormatGaCriteria sortCriteria) {
    // If element has same sort, check order and display the highest order.
    Integer prevSort = sortCriteria.getPrevSort();
    Integer prevOrder = sortCriteria.getPrevOrder();
    Integer currentSort = sortCriteria.getFormatGaSort();
    if (prevSort == null) {
      prevSort = currentSort;
    } else {
      prevSort = currentSort;
      if (prevSort.equals(sortCriteria.getFormatGaSort())
          && sortCriteria.getFormatGaOrder() != null) {
        Integer currentOrder = sortCriteria.getFormatGaOrder();
        if (prevOrder == null) {
          prevOrder = currentOrder;
        } else {
          if (currentOrder.compareTo(prevOrder) > 0) {
            return null;
          } else {
            prevOrder = currentOrder;
          }
        }
      }
    }
    return new SortedFormatGaResult(prevSort, prevOrder);
  }

  private static boolean isValidFormatGaDoc(final FormatGaDoc formatGaDoc) {
    return Objects.nonNull(formatGaDoc) && CollectionUtils.isNotEmpty(formatGaDoc.getElements());
  }

  private static boolean hasDelimiter(final String currentDelimiter,
      final FormatGaElement element) {
    return currentDelimiter == null && element.getDelimiter() != null;
  }

  private static String beautifyPartDesc(final StringBuilder partDescBuilder,
      final String exDelimiter, final String... removedStrs) {

    if (!StringUtils.isEmpty(exDelimiter)
        && partDescBuilder.lastIndexOf(exDelimiter) == partDescBuilder.length() - 1) {

      return partDescBuilder.deleteCharAt(partDescBuilder
          .lastIndexOf(exDelimiter)).toString();
    }

    final StringBuilder beautifyPartDescBuilder =
        new StringBuilder(StringUtils.trim(partDescBuilder.toString()));
    for (String str : removedStrs) {
      final int lastCommaCharIndex =
          beautifyPartDescBuilder.lastIndexOf(StringUtils.trim(str));
      if (beautifyPartDescBuilder.length() > 0
          && lastCommaCharIndex == beautifyPartDescBuilder.length() - 1) {
        beautifyPartDescBuilder.deleteCharAt(beautifyPartDescBuilder
            .lastIndexOf(StringUtils.trim(str)));
      }
    }
    return beautifyPartDescBuilder.toString().trim();
  }

  private static void updatePartDescStr(final StringBuilder partDescBuilder,
      final List<ArticleCriteriaDto> criterias, final FormatGaElement ele) {
    int index = 0;
    int size = CollectionUtils.size(criterias);
    for (ArticleCriteriaDto criteria : criterias) {
      if (criteria == null) {
        continue;
      }
      if (index == 0) {
        updatePartDescStrPerCriteria(partDescBuilder, criteria, ele);
      } else {
        partDescBuilder.append(criteria.getCvp());
      }
      if (!StringUtils.isBlank(criteria.getCvp()) && isValidToAddCvpDelimiter(index, size)) {
        partDescBuilder.append(SPACE).append(CVP_DELIMITER).append(SPACE);
      }
      index++;
    }
  }

  private static boolean isValidToAddCvpDelimiter(int curIdx, int criteriaSize) {
    return criteriaSize > NumberUtils.INTEGER_ONE
        && curIdx < criteriaSize - NumberUtils.INTEGER_ONE;
  }

  private static void updatePartDescStrPerCriteria(final StringBuilder partDescBuilder,
      final ArticleCriteriaDto criteria, final FormatGaElement ele) {
    if (StringUtils.isBlank(ele.getPostfix()) && StringUtils.isBlank(ele.getPrefix())) {
      partDescBuilder.append(SPACE);
    }

    if (SHOW_CRITERIA.equalsIgnoreCase(ele.getShowCriteria())) {
      partDescBuilder.append(addBoldTag(criteria.getCndech()));
      partDescBuilder.append(SPACE);
      partDescBuilder.append(criteria.getCvp());
    } else if (!StringUtils.isEmpty(ele.getAlternate())) {
      partDescBuilder.append(ele.getAlternate());
      partDescBuilder.append(criteria.getCvp());
    } else {
      partDescBuilder.append(criteria.getCvp());
    }
    // Append postfix
    partDescBuilder.append(defaultPostfix(ele.getPostfix()));

  }

  private static String addBoldTag(final String str) {
    return "<strong>" + str + "</strong>:";
  }

  private static String defaultPostfix(final String postfix) {
    return PREFIX_NEW_LINE.equals(postfix) ? HYPHEN_HAS_SPACE_DELIMITER : StringUtils.defaultString(postfix);
  }

  /**
   * Initialize criteria map base on the list of criteria input.
   *
   */
  private static Map<String, List<ArticleCriteriaDto>> initCriteriaMap(
      final List<ArticleCriteriaDto> criterias) {
    Map<String, List<ArticleCriteriaDto>> criteriaMap = new HashMap<>();
    for (ArticleCriteriaDto criteria : criterias) {
      if (!criteriaMap.containsKey(criteria.getCid())) {
        criteriaMap.put(criteria.getCid(), getArticleCriteriasByCid(criterias, criteria.getCid()));
      }
    }
    return criteriaMap;
  }

  private static List<ArticleCriteriaDto> getArticleCriteriasByCid(
      final List<ArticleCriteriaDto> criterias, String cid) {
    return criterias.stream()
        .filter(c -> StringUtils.equals(cid, c.getCid()))
        .sorted((c1, c2) -> {
          int sortOrder = NumberUtils.compare(c1.getCsort(), c2.getCsort());
          return sortOrder != 0 ? sortOrder :
            StringUtils.defaultString(c1.getCvp()).compareToIgnoreCase(
                StringUtils.defaultString(c2.getCvp()));
        }).collect(Collectors.toList());
  }

  private static List<ArticleCriteriaDto> filterCriteriaByFormatGaElements(
      List<ArticleCriteriaDto> criterias, List<FormatGaElement> elements) {
    Integer curSortGroup = null;
    List<ArticleCriteriaDto> result = new ArrayList<>();
    List<ArticleCriteriaDto> subCriterias;
    for (FormatGaElement element : elements) {
      if (NumberUtils.createInteger(element.getSort()).equals(curSortGroup)) {
        continue;
      }
      // #1683: List Parts: Attributes not showing when multiple
      // Allows multiple attributes in article criteria list
      subCriterias =
          criterias.stream().filter(matchFormatGaElement(element))
          .sorted(articleCriteriaComparatorByCvp())
          .collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(subCriterias)) {
        curSortGroup = NumberUtils.createInteger(element.getSort());
        result.addAll(subCriterias);
      }
    }
    return result;
  }

  private static Predicate<ArticleCriteriaDto> matchFormatGaElement(FormatGaElement element) {
    return criteria -> StringUtils.equals(element.getCid().toString(), criteria.getCid())
        && StringUtils.isNotBlank(criteria.getCvp());
  }

  private static Comparator<ArticleCriteriaDto> articleCriteriaComparatorByCvp() {
    return (criteria1, criteria2) -> StringUtils.defaultString(criteria1.getCvp())
        .compareToIgnoreCase(StringUtils.defaultString(criteria2.getCvp()));
  }

  private static void buildConstructionYearsTxt(StringBuilder partDescBuilder,
      final List<ArticleCriteriaDto> criterias) {
    final List<ArticleCriteriaDto> constructionCriterias = criterias.stream()
        .filter(c -> ArrayUtils.contains(CONSTRUCTION_YEAR_CIDS, c.getCid()))
        .sorted(articleCriteriaComparator()).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(constructionCriterias)) {
      return;
    }
    // Just add new line if have format element before
    if (!SagStringUtils.isEmptyStrBuilder(partDescBuilder)) {
      partDescBuilder.append(HYPHEN_HAS_SPACE_DELIMITER);
    }

    List<String> constructionTxts = constructionCriterias.stream()
        .map(criteria -> addBoldTag(criteria.getCndech()) + SPACE + criteria.getCvp())
        .collect(Collectors.toList());
    partDescBuilder.append(StringUtils.join(constructionTxts, HYPHEN_HAS_SPACE_DELIMITER));
  }

  private static Comparator<ArticleCriteriaDto> articleCriteriaComparator() {
    return (criteria1, criteria2) -> {
      if (!NumberUtils.isNumber(criteria1.getCid())
          || !NumberUtils.isNumber(criteria2.getCid())) {
        return -1;
      }
      return NumberUtils.compare(NumberUtils.createInteger(criteria1.getCid()),
          NumberUtils.createInteger(criteria2.getCid()));
    };
  }

  @Data
  static class SortedFormatGaCriteria {
    private Integer prevSort;
    private Integer prevOrder;
    private Integer formatGaSort;
    private Integer formatGaOrder;

    SortedFormatGaCriteria(Integer prevSort, Integer prevOrder, FormatGaElement element) {
      setPrevSort(prevSort);
      setPrevOrder(prevOrder);
      if (!StringUtils.isBlank(element.getOrder())) {
        setFormatGaOrder(Integer.valueOf(element.getOrder()));
      }
      if (!StringUtils.isBlank(element.getSort())) {
        setFormatGaSort(Integer.valueOf(element.getSort()));
      }
    }
  }

  @Data
  static class SortedFormatGaResult {
    private Integer currentSort;
    private Integer currentOrder;

    SortedFormatGaResult(Integer currentSort, Integer currentOrder) {
      setCurrentOrder(currentOrder);
      setCurrentSort(currentSort);
    }
  }

}
