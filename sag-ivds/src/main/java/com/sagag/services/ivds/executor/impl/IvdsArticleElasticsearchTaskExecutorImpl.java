package com.sagag.services.ivds.executor.impl;

import com.sagag.services.article.api.utils.DefaultAmountHelper;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.CertifiedPartType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleCriteriaDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticlePartDto;
import com.sagag.services.domain.article.GenArtTxtDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.VehicleGenArtArtDto;
import com.sagag.services.elasticsearch.api.VehicleGenArtArtSearchService;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.hazelcast.api.FormatGaCacheService;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.hazelcast.api.MakeCacheService;
import com.sagag.services.hazelcast.api.SupplierCacheService;
import com.sagag.services.ivds.executor.IvdsArticleElasticsearchTaskExecutor;
import com.sagag.services.ivds.utils.ArticlesGenArtUtils;
import com.sagag.services.ivds.utils.OEAndIamNumberBuilder;
import com.sagag.services.ivds.utils.PartDescriptionBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IvdsArticleElasticsearchTaskExecutorImpl
    implements IvdsArticleElasticsearchTaskExecutor {

  private static final String CERTIFIED_PART_CID = "90001";

  @Autowired
  private GenArtCacheService genArtCacheService;
  @Autowired
  private FormatGaCacheService formatGaCacheService;
  @Autowired
  private VehicleGenArtArtSearchService vehicleGenArtArtSearchService;
  @Autowired
  private MakeCacheService makeCacheService;
  @Autowired
  private SupplierCacheService supplierCacheService;
  @Autowired
  private OEAndIamNumberBuilder oeAndIamNumberBuilder;
  @Autowired
  private PartDescriptionBuilder partDescBuilder;

  @Override
  @LogExecutionTime
  public void executeTask(Collection<ArticleDocDto> articles, Optional<VehicleDto> vehOpt,
      SupportedAffiliate affiliate) {

    final List<String> gaids = getGaIdsFromArticles(articles);
    final Map<String, GenArtTxt> genArts = genArtCacheService.searchGenArtByIds(gaids);

    final Map<String, GenArtTxt> genArtsEng = genArtCacheService.searchGenArtByIdsAndLanguage(gaids,
        Optional.of(Locale.ENGLISH));

    // Always return the empty map if not found any format_ga items
    final Map<String, FormatGaDoc> formatGaMap = formatGaCacheService.searchFormatGaByGaIds(gaids);

    Map<String, VehicleGenArtArtDto> fitmentMap = new HashMap<>();
    if (vehOpt.filter(validVehicleInfoPredicate()).isPresent()) {
      final List<VehicleGenArtArtDto> fitmentDocs = vehicleGenArtArtSearchService
          .searchFitments(vehOpt.get().getVehId(), gaids).stream()
          .map(fitment -> SagBeanUtils.map(fitment, VehicleGenArtArtDto.class))
          .collect(Collectors.toList());

      fitmentMap.putAll(fitmentDocs.stream().collect(Collectors.toMap(
          fm -> fm.getGenart().toString(), Function.identity(), (fm1, fm2) -> fm2)));
    }

    GenArtTxtDto genArtTxt;
    GenArtTxtDto genArtTxtEng;
    for (ArticleDocDto article : articles) {
      genArtTxt = ArticlesGenArtUtils.defaultGenArtTxtIfNull(genArts.get(article.getGaId()));
      article.setGenArtTxts(Collections.singletonList(genArtTxt));

      genArtTxtEng = ArticlesGenArtUtils.defaultGenArtTxtIfNull(genArtsEng.get(article.getGaId()));
      article.setGenArtTxtEng(genArtTxtEng);

      DefaultAmountHelper.updateArticleQuantities(article, vehOpt,
          Optional.ofNullable(fitmentMap.get(genArtTxt.getGaid())), affiliate);
      if (Objects.isNull(article.getAmountNumber())) {
        article.setAmountNumber(article.getSalesQuantity());
      }
      article.setCriteria(removeDuplicateCriterias(article.getCriteria()));
      getCertifiedPartType(article.getCriteria()).ifPresent(article::setCertifiedPartType);

      // Update part description
      article.setPartDesc(partDescBuilder.build(article, formatGaMap.get(article.getGaId())));
      setAdditionalPartNumbers(article, oeAndIamNumberBuilder);

      Collections.sort(article.getCriteria(),
          (c1, c2) -> NumberUtils.compare(c1.getCidLong(), c2.getCidLong()));
    }
  }

  private void setAdditionalPartNumbers(ArticleDocDto article,
      OEAndIamNumberBuilder oeAndIamNumberBuilder) {

    // #3309: Update OE-IAM cross references
    if (!article.hasOeNumbers()) {
      List<ArticlePartDto> oeArticleParts = article.getPartInfosByTypes(ArticlePartType.OEM.name());
      Map<String, List<String>> oeNumbers =
          oeAndIamNumberBuilder.build(oeArticleParts, searchMakesByArticleParts(oeArticleParts));
      oeNumbers.forEach((key, values) -> values.sort((a, b) -> b.compareToIgnoreCase(a)));
      article.setOeNumbers(oeNumbers);
    }
    if (!article.hasIamNumbers()) {
      List<ArticlePartDto> iamArticleParts = article.getPartInfosByTypes(ArticlePartType.IAM.name());
      Map<String, List<String>> iamNumbers = oeAndIamNumberBuilder.build(iamArticleParts,
          searchSuppliersByArticleParts(iamArticleParts));
      iamNumbers.forEach((key, values) -> values.sort((a, b) -> a.compareToIgnoreCase(b)));
      article.setIamNumbers(iamNumbers);
    }

    // #4027 #4048: Set pprnEANs
    if (!article.hasPnrnEANs()) {
      List<String> pnrnEANs = article.getPartInfosByTypes(ArticlePartType.EAN.name()).stream()
          .map(ArticlePartDto::getPnrn).collect(Collectors.toList());
      article.setPnrnEANs(pnrnEANs);
    }


    // TFS #2946
    if (!article.hasPnrnPccs()) {
      List<String> pnrnPccs = article.getPartInfosByTypes(ArticlePartType.PCC.name()).stream()
          .map(ArticlePartDto::getPnrn)
          .distinct() // TFS#3221: Filter out duplicate PCC per article
          .collect(Collectors.toList());
      article.setPnrnPccs(pnrnPccs);
    }

  }

  private Map<String, String> searchMakesByArticleParts(List<ArticlePartDto> articleParts) {
    if (CollectionUtils.isEmpty(articleParts)) {
      return Collections.emptyMap();
    }
    final List<String> brandIds =
        articleParts.stream().map(ArticlePartDto::getBrandid).collect(Collectors.toList());
    return makeCacheService.findMakesByIds(brandIds);
  }

  private Map<String, String> searchSuppliersByArticleParts(List<ArticlePartDto> articleParts) {
    if (CollectionUtils.isEmpty(articleParts)) {
      return Collections.emptyMap();
    }
    final List<String> brandIds =
        articleParts.stream().map(ArticlePartDto::getBrandid).collect(Collectors.toList());
    return supplierCacheService.searchSupplierNameByIds(brandIds);
  }

  /**
   * Return a list of Criteria without duplicated items and all elements are sorted.
   *
   * @param criterias criteria list
   * @return result sorted criteria list
   */
  private static List<ArticleCriteriaDto> removeDuplicateCriterias(
      List<ArticleCriteriaDto> criterias) {
    if (CollectionUtils.isEmpty(criterias)) {
      return Collections.emptyList();
    }
    HashMap<String, ArticleCriteriaDto> criteriaMap = new HashMap<>();
    criterias.stream().filter(Objects::nonNull)
        .forEach(item -> criteriaMap.put(getCriteriaKey(item), item));
    ArrayList<ArticleCriteriaDto> result = new ArrayList<>(criteriaMap.values());
    Collections.sort(result, (c1, c2) -> NumberUtils.compare(c1.getCsort(), c2.getCsort()));
    return result;
  }

  private static String getCriteriaKey(ArticleCriteriaDto criteria) {
    return criteria.getCid() + criteria.getCvp() + criteria.getCndech();
  }

  /**
   * Get gaid list from the list of part result.
   *
   */
  private static List<String> getGaIdsFromArticles(Collection<ArticleDocDto> articles) {
    if (CollectionUtils.isEmpty(articles)) {
      return Collections.emptyList();
    }
    final Set<String> gaIds =
        articles.stream().filter(part -> StringUtils.isNotBlank(part.getGaId()))
            .map(ArticleDocDto::getGaId).collect(Collectors.toSet());
    return new ArrayList<>(gaIds);
  }

  /**
   * Returns the certified part type of article if exist.
   *
   * @param criterias the list criteria of article.
   * @return the optional of {@link CertifiedPartType}
   */
  private static Optional<CertifiedPartType> getCertifiedPartType(
      final List<ArticleCriteriaDto> criterias) {
    if (CollectionUtils.isEmpty(criterias)) {
      return Optional.empty();
    }
    return criterias.stream().filter(filterByCertifiedArticle())
        .map(criteria -> CertifiedPartType.valueOf(StringUtils.upperCase(criteria.getCvp())))
        .findFirst();
  }

  private static Predicate<ArticleCriteriaDto> filterByCertifiedArticle() {
    return criteria -> Objects.nonNull(criteria) && CERTIFIED_PART_CID.equals(criteria.getCid());
  }

  private static Predicate<VehicleDto> validVehicleInfoPredicate() {
    return veh -> !StringUtils.isBlank(veh.getVehId())
        && !StringUtils.equalsIgnoreCase(SagConstants.KEY_NO_VEHICLE, veh.getVehId());
  }
}
