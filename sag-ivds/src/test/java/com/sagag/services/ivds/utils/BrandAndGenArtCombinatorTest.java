package com.sagag.services.ivds.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.UniversalPartArticleResponse;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.ivds.response.wsp.UniversalPartArticleSearchResponse;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UT for {@link com.sagag.services.ivds.utils.BrandAndGenArtCombinator}.
 *
 */
@RunWith(SpringRunner.class)
public class BrandAndGenArtCombinatorTest {

  @InjectMocks
  private BrandAndGenArtCombinator combinator;

  @Mock
  private GenArtCacheService genArtCacheService;

  @Mock
  private BrandPriorityCacheService brandPriorityCacheService;

  @Test
  public void testBuildUniversalPartArticleSearchResponse() {

    // makes
    final ArticleDoc art1 = new ArticleDoc();
    art1.setGaId("g1");
    final ArticleDoc art2 = new ArticleDoc();
    art2.setGaId("g2");
    final String language = "de";

    final Map<String, List<Object>> aggregations = new HashMap<>();
    final List<ArticleDoc> arts = Arrays.asList(art1, art2);
    final List<Object> gaIds = Arrays.asList("g1","g2");
    final List<Object> suppliers = Arrays.asList("s1","s2");
    final Page<ArticleDoc> pageArt = new PageImpl<>(arts);
    final Map<String, GenArtTxt> cachedGenArtTxts = buildGenArtTxt();
    final Map<String, CachedBrandPriorityDto> cacheBrandPrio = buildBrandPrio();

    aggregations.put("genarts", gaIds);
    aggregations.put("suppliers", suppliers);
    when(genArtCacheService.searchGenArtByIdsAndLanguageCode(
        gaIds.stream().map(Object::toString).collect(Collectors.toList()), Optional.of(language)))
            .thenReturn(cachedGenArtTxts);
    
    when(brandPriorityCacheService.findCachedBrandPriority(
        gaIds.stream().map(Object::toString).collect(Collectors.toList())))
            .thenReturn(cacheBrandPrio);
    
    UniversalPartArticleSearchResponse result = combinator.buildUniversalPartArticleSearchResponse(
        UniversalPartArticleResponse.builder().aggregations(aggregations).articles(pageArt).build(),
        "de", SupportedAffiliate.DERENDINGER_CH);

    assertEquals(2, result.getGenArts().size());
    assertEquals(2, result.getBrandPrios().size());
  }
  
  private Map<String, GenArtTxt> buildGenArtTxt(){
    final Map<String, GenArtTxt> cachedGenArtTxts = new HashMap<>();
    GenArtTxt g1Txt = new GenArtTxt();
    g1Txt.setGaid("g1");
    g1Txt.setGatxtdech("g1txt");
    GenArtTxt g2Txt = new GenArtTxt();
    g2Txt.setGaid("g2");
    g2Txt.setGatxtdech("g2txt");
    cachedGenArtTxts.put("g1", g1Txt);
    cachedGenArtTxts.put("g2", g2Txt);
    return cachedGenArtTxts;
  }
  private Map<String, CachedBrandPriorityDto> buildBrandPrio(){
    final Map<String, CachedBrandPriorityDto> cacheBrand = new HashMap<>();
    CachedBrandPriorityDto brand1 = new CachedBrandPriorityDto();
    brand1.setGaid("g1");
    brand1.setSorts(Collections.emptyList());
    brand1.setBrands(Collections.emptyList());
    CachedBrandPriorityDto brand2 = new CachedBrandPriorityDto();
    brand2.setGaid("g2");
    brand2.setSorts(Collections.emptyList());
    brand2.setBrands(Collections.emptyList());
    cacheBrand.put("g1", brand1);
    cacheBrand.put("g2", brand2);
    return cacheBrand;
  }

  
  @Test
  public void testBuildUniversalPartArticleSearchResponseReturnEmpty() {
    final Map<String, List<Object>> aggregations = new HashMap<>();
    final List<Object> gaIds = Arrays.asList("g1");
    final List<Object> suppliers = Arrays.asList("s1","s2");
    aggregations.put("genarts", gaIds);
    aggregations.put("suppliers", suppliers);
    UniversalPartArticleSearchResponse result = combinator.buildUniversalPartArticleSearchResponse(
        UniversalPartArticleResponse.builder().aggregations(aggregations).build(),
        StringUtils.EMPTY, SupportedAffiliate.DERENDINGER_CH);
    assertTrue(result.getGenArts().isEmpty());
  }
}
