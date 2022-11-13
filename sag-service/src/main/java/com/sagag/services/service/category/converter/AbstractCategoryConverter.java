package com.sagag.services.service.category.converter;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.domain.eshop.category.GenArtDto;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.service.category.CategoryItemCriteria;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractCategoryConverter
    implements ICategoryConverter<CategoryDoc, CategoryItem> {

  private static final String SHOW_Q_CATEGORY = "1";

  private static final String FOLD_Q_CATEGORY = "1";

  private static final String SHOW_FOLD_Q_CATEGORY = "1";

  private static final String UNCHECK_GENART_VALUE = "0";

  private static final String CHECKED_GENART_VALUE = "1";

  protected static final String PRIOR_DELIMITER = SagConstants.COLON;

  protected abstract Function<GenArt, GenArtDto> genArtConverter(SupportedAffiliate aff,
      String collection, boolean addCUPICode,
      Map<String, CachedBrandPriorityDto> brandPrioritiesMap);

  private List<GenArtDto> convertGenArtDtos(final List<GenArt> genArts, final SupportedAffiliate aff,
      final String collection, final boolean addCUPICode,
      final Map<String, CachedBrandPriorityDto> brandPrioritiesMap) {
    return genArts.stream().map(genArtConverter(aff, collection, addCUPICode, brandPrioritiesMap))
        .collect(Collectors.toList());
  }

  protected static GenArtDto cloneGenArtDto(final GenArt document, final boolean addCUPICode) {
    final GenArtDto genArtDto = SagBeanUtils.map(document, GenArtDto.class);
    if (!addCUPICode) {
      genArtDto.setCupis(Collections.emptyList());
    }
    genArtDto.setBrands(ListUtils.emptyIfNull(genArtDto.getBrands()));
    return genArtDto;
  }

  @Override
  public CategoryItem convert(CategoryDoc catDoc, CategoryItemCriteria criteria) {
    final SupportedAffiliate aff = criteria.getAff();
    final String collection = criteria.getCollection();
    final boolean ignoreOpenVal = criteria.isIgnoreOpenVal();
    final boolean addCUPICode = criteria.isReturnCUPIs();
    final Map<String, String> externalUrls = MapUtils.emptyIfNull(criteria.getExternalUrls());
    final List<String> oilGaIds = ListUtils.emptyIfNull(criteria.getOilGaIds());
    final Map<String, CachedBrandPriorityDto> brandPrioritiesMap =
        MapUtils.emptyIfNull(criteria.getBrandSortingMap());

    final String leafId = catDoc.getLeafid();
    final CategoryItem catItem = new CategoryItem(leafId);
    catItem.setDescription(catDoc.getLeaftxt());
    catItem.setSagCode(catDoc.getSagCode());
    catItem.setSort(catDoc.getSort());
    catItem.setOpen(catDoc.getOpen());
    catItem.setParentId(catDoc.getParentid());
    catItem.setIdProductBrand(catDoc.getIdProductBrand());
    catItem.setGenArts(this.convertGenArtDtos(catDoc.getGenarts(), aff, collection, addCUPICode,
        brandPrioritiesMap));
    catItem.setClassicCol(catDoc.getClassicCol());
    catItem.setCheck(checkedGenartValuePredicate().test(catDoc));
    catItem.setOilCate(isOilCate(catItem, oilGaIds));
    catItem.setLink(externalUrls.get(catDoc.getLink()));

    // #3019: AT-AX: Minimise the leftside search menu on "Alle Artikel" as default for all users
    // We propose new attribute to control we should ignore open flag in front-end side or not.
    catItem.setIgnoredOpen(ignoreOpenVal);

    // Update map entity for quick click feature
    catItem.setQCol(
      !StringUtils.isBlank(catDoc.getQcol()) ? Integer.valueOf(catDoc.getQcol()) : null);
    catItem.setQRow(
      !StringUtils.isBlank(catDoc.getQrow()) ? Integer.valueOf(catDoc.getQrow()) : null);
    catItem.setQFlag(
      !StringUtils.isBlank(catDoc.getQflag()) ? Integer.valueOf(catDoc.getQflag()) : null);
    catItem.setQLevel(
      !StringUtils.isBlank(catDoc.getQlevel()) ? Integer.valueOf(catDoc.getQlevel()) : null);
    catItem.setQSort(
      !StringUtils.isBlank(catDoc.getQsort()) ? Integer.valueOf(catDoc.getQsort()) : null);
    catItem.setQShow(SHOW_Q_CATEGORY.equals(catDoc.getQshow()));
    catItem.setQFold(FOLD_Q_CATEGORY.equals(catDoc.getQfold()));
    catItem.setQFoldShow(SHOW_FOLD_Q_CATEGORY.equals(catDoc.getQfoldShow()));

    return catItem;
  }

  private static boolean isOilCate(final CategoryItem cateItem, final List<String> oilGaIds) {
    if (CollectionUtils.isEmpty(oilGaIds) || !cateItem.hasGaIds()) {
      return false;
    }
    return cateItem.getGenArts().stream().anyMatch(genArt -> oilGaIds.contains(genArt.getGaid()));
  }

  private static Predicate<CategoryDoc> checkedGenartValuePredicate() {
    return catTree -> CHECKED_GENART_VALUE.equals(StringUtils.defaultIfBlank(catTree.getIsCheck(),
      UNCHECK_GENART_VALUE));
  }

}
