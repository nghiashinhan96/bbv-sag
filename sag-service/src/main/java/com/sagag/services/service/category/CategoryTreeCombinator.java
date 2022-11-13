package com.sagag.services.service.category;

import com.google.common.collect.Lists;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.ivds.api.IvdsOilSearchService;
import com.sagag.services.service.category.converter.AbstractCategoryConverter;
import com.sagag.services.service.enums.CategoryType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CategoryTreeCombinator {

  @Autowired
  private AbstractCategoryConverter categoryConverter;

  @Autowired
  private IvdsOilSearchService ivdsOilSearchService;

  public Map<String, CategoryItem> combine(final Collection<CategoryDoc> catsByVehicle,
      final Map<String, CategoryDoc> allCatsMap, final CategoryItemCriteria criteria) {
    if (CollectionUtils.isEmpty(catsByVehicle) || MapUtils.isEmpty(allCatsMap)) {
      return Collections.emptyMap();
    }

    final Map<String, CategoryDoc> displayedCatsByVehicle = new HashMap<>();
    catsByVehicle.stream()
    .forEach(catByVehicle -> {
      displayedCatsByVehicle.put(catByVehicle.getId(), catByVehicle);
      displayedCatsByVehicle.putAll(findParentCats(allCatsMap, catByVehicle));
    });

    allCatsMap.values().stream()
    .filter(availableLinkOptionPredicate(SetUtils.emptyIfNull(criteria.getExternalUrls().keySet())))
    .forEach(catByRequired -> {
      displayedCatsByVehicle.putIfAbsent(catByRequired.getId(), catByRequired);
      displayedCatsByVehicle.putAll(findParentCats(allCatsMap, catByRequired));
    });

    return Optional.of(displayedCatsByVehicle).filter(MapUtils::isNotEmpty)
        .map(mapValues -> Lists.newArrayList(mapValues.values()))
        .map(realCategories -> this.buildCategoriesByLeafIds(realCategories, criteria))
        .orElseGet(() -> Collections.emptyMap());
  }

  private static Predicate<CategoryDoc> availableLinkOptionPredicate(Set<String> linkPartnerKeys) {
    return catDoc -> StringUtils.isNotBlank(catDoc.getLink())
            && linkPartnerKeys.contains(catDoc.getLink());
  }

  private static Map<String, CategoryDoc> findParentCats(Map<String, CategoryDoc> allCatsMap,
      CategoryDoc catByVehicle) {
    final Map<String, CategoryDoc> cats = new HashMap<>();
    cats.putIfAbsent(catByVehicle.getId(), catByVehicle);
    final String catParentId = catByVehicle.getParentid();
    if (containsParentIdPredicate().test(allCatsMap, catParentId)) {
      cats.putAll(findParentCats(allCatsMap, allCatsMap.get(catParentId)));
    }
    return cats;
  }

  private static BiPredicate<Map<String, CategoryDoc>, String> containsParentIdPredicate() {
    return (allCatsMap, parentId) -> StringUtils.isNotBlank(parentId)
        && allCatsMap.containsKey(parentId);
  }

  private Map<String, CategoryItem> buildCategoriesByLeafIds(
      final List<CategoryDoc> realDisplayedCats, final CategoryItemCriteria criteria) {
    if (CollectionUtils.isEmpty(realDisplayedCats)) {
      return Collections.emptyMap();
    }

    final CategoryType cateType = criteria.getCategoryType();
    final List<CategoryDoc> filteredRealDisplayedCats = realDisplayedCats.stream()
        .filter(filterServiceMode(cateType.isService())).collect(Collectors.toList());

    final List<String> filteredRealDisplayedGaIds = filteredRealDisplayedCats.stream()
        .flatMap(catDoc -> catDoc.getGenarts().stream())
        .map(GenArt::getGaid).distinct().collect(Collectors.toList());

    final List<String> oilGaIds =
        ivdsOilSearchService.extractOilGenericArticleIds(filteredRealDisplayedGaIds);
    criteria.setOilGaIds(oilGaIds);

    return filteredRealDisplayedCats.stream()
        .map(cateDoc -> categoryConverter.convert(cateDoc, criteria))
        .collect(Collectors.toMap(CategoryItem::getId, Function.identity()));
  }

  private Predicate<CategoryDoc> filterServiceMode(final boolean isService) {
    return cat -> !isService || !StringUtils.isEmpty(cat.getService());
  }
}
