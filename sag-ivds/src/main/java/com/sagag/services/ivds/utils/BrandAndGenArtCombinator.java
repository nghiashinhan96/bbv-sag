package com.sagag.services.ivds.utils;

import com.sagag.services.common.contants.UniversalPartConstants;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.domain.eshop.category.GenArtDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportBrandDto;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.UniversalPartArticleResponse;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.ivds.response.wsp.UniversalPartArticleSearchResponse;
import com.sagag.services.ivds.response.wsp.UniversalPartGenArtDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class BrandAndGenArtCombinator {

  @Autowired
  private GenArtCacheService genArtCacheService;

  @Autowired
  private CountryConfiguration countryConfiguration;

  @Autowired
  private BrandPriorityCacheService brandPriorityCacheService;

  public UniversalPartArticleSearchResponse buildUniversalPartArticleSearchResponse(
      UniversalPartArticleResponse articlesResponse, String language, SupportedAffiliate affiliate) {
    List<String> gaIds =
        articlesResponse.getAggregations().get(UniversalPartConstants.GENART_MAP_NAME).stream()
            .map(Object::toString).collect(Collectors.toList());
    List<String> brandIds =
        articlesResponse.getAggregations().get(UniversalPartConstants.SUPPLIER_MAP_NAME).stream()
            .map(Object::toString).collect(Collectors.toList());
    
    Map<String, CachedBrandPriorityDto> cachedBrandPrio =
        brandPriorityCacheService.findCachedBrandPriority(gaIds);
    List<GenArtDto> brandPrios = cachedBrandPrio.values().stream().map(genArtConverter(affiliate))
        .collect(Collectors.toList());
    
    if (CollectionUtils.isEmpty(gaIds) || gaIds.size() < UniversalPartConstants.MIN_GENART_TILES) {
      return new UniversalPartArticleSearchResponse(Lists.emptyList(), brandPrios);
    }
    if (StringUtils.isBlank(language)) {
      language = countryConfiguration.getDefaultUserLanguageByCountry();
    }
    final List<ArticleDoc> articles = articlesResponse.getArticles().getContent();
    List<UniversalPartGenArtDto> universalPartGenArts = new ArrayList<>();

    Map<String, GenArtTxt> cachedGenArtTxts =
        genArtCacheService.searchGenArtByIdsAndLanguageCode(gaIds, Optional.of(language));
    gaIds.forEach(genArt -> {
      List<SupportBrandDto> brands = findBrandsOfGenArt(articles, genArt, brandIds);

      GenArtTxt genArtTxt = cachedGenArtTxts.get(genArt);
      String genArtName = StringUtils.EMPTY;
      if (genArtTxt != null) {
        genArtName = genArtTxt.getGatxtdech();
      }
      universalPartGenArts.add(new UniversalPartGenArtDto(genArt, genArtName, brands));
    });
    return UniversalPartArticleSearchResponse.builder().genArts(universalPartGenArts).brandPrios(brandPrios).build();
  }

  private List<SupportBrandDto> findBrandsOfGenArt(List<ArticleDoc> articles, String genArt,
      List<String> brandIds) {
    if (CollectionUtils.isEmpty(articles)) {
      return Lists.emptyList();
    }
    List<SupportBrandDto> result = Lists.newArrayList();
    Map<String, String> genArtBrands = new HashedMap<>();
    articles.stream().filter(brandByGenArtPredicate(genArt, brandIds))
    .forEach(art -> genArtBrands.putIfAbsent(art.getIdDlnr(), art.getProductBrand()));
    genArtBrands.forEach((id, name) -> result.add(new SupportBrandDto(id, name)));
    return result;
  }
  

  private Function<CachedBrandPriorityDto, GenArtDto> genArtConverter(
      SupportedAffiliate affiliate) {
    return item -> convertGenArtDto(item, affiliate);
  }
 
  private GenArtDto convertGenArtDto(CachedBrandPriorityDto brandPrio,
      SupportedAffiliate affiliate) {
    GenArtDto genArtDto = new GenArtDto();
    SagBeanUtils.copyProperties(brandPrio, genArtDto);
    final List<BrandDto> brandByAfffiliates = new LinkedList<>();
    brandPrio.getSorts().stream().forEach(sort -> {
      List<BrandDto> brands = sort.getBrands().stream()
          .filter(brandByAffiliate(affiliate.getEsShortName())).collect(Collectors.toList());
      brandByAfffiliates.addAll(brands);
    });
        
    genArtDto.setBrands(brandByAfffiliates);
    genArtDto.setSorts(Collections.emptyList());
    return genArtDto;
  }
  
  private Predicate<BrandDto> brandByAffiliate(String affiliate) {
    return brand -> !StringUtils.isBlank(brand.getAffiliate()) &&
        StringUtils.equalsIgnoreCase(brand.getAffiliate(), affiliate);
  }
  
  private Predicate<? super ArticleDoc> brandByGenArtPredicate(String genArt,
      List<String> brandIds) {
    return art -> StringUtils.equalsIgnoreCase(art.getGaId(), genArt)
        && brandIds.contains(art.getIdDlnr());
  }

}
