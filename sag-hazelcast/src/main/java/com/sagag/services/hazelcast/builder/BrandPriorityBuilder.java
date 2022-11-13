package com.sagag.services.hazelcast.builder;

import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.domain.eshop.category.SortDto;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPriorityDoc;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPrioritySortDoc;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPrioritySortsDoc;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BrandPriorityBuilder implements Function<BrandPriorityDoc, CachedBrandPriorityDto> {

  @Override
  public CachedBrandPriorityDto apply(BrandPriorityDoc doc) {
    final CachedBrandPriorityDto priority = new CachedBrandPriorityDto();
    priority.setGaid(doc.getGaid());
    priority.setSortType(doc.getSortType());
    priority.setBrands(convertBrandDtos(ListUtils.emptyIfNull(doc.getBrands()), doc.getGaid()));
    priority.setSorts(convertSortDtos(ListUtils.emptyIfNull(doc.getSorts()), doc.getGaid()));
    return priority;
  }

  private static List<BrandDto> convertBrandDtos(List<BrandPrioritySortsDoc> brandPrioritySortDocs,
      String gaid) {
    final List<BrandDto> brands = new ArrayList<>();
    for (BrandPrioritySortsDoc item : brandPrioritySortDocs) {
      if (CollectionUtils.isEmpty(item.getPriorities())) {
        continue;
      }

      final List<BrandPrioritySortDoc> brandPriorities = item.getPriorities();
      for (BrandPrioritySortDoc brandItem : brandPriorities) {
        brands.add(convertBrandDto(brandItem, gaid, item.getBrandId()));
      }
    }
    return brands;
  }

  private static BrandDto convertBrandDto(BrandPrioritySortDoc doc, String gaid, String brandId) {
    final BrandDto brand = new BrandDto();
    brand.setBrand(brandId);
    brand.setGaId(gaid);
    brand.setCollection(doc.getCollection());
    brand.setAffiliate(doc.getAffiliate());
    brand.setSorts(MapUtils.emptyIfNull(doc.getSort()));
    return brand;
  }

  private static List<SortDto> convertSortDtos(List<BrandPrioritySortsDoc> brandPrioritySortDocs,
      String gaid) {
    final List<SortDto> sorts = new ArrayList<>();
    for (BrandPrioritySortsDoc item : brandPrioritySortDocs) {
      if (CollectionUtils.isEmpty(item.getPriorities())) {
        continue;
      }

      final List<BrandPrioritySortDoc> brandPriorities = item.getPriorities();
      final List<String> affs = brandPriorities.stream()
          .map(BrandPrioritySortDoc::getAffiliate).distinct().collect(Collectors.toList());
      brandPriorities.stream()
      .map(sortItem -> convertSortDto(sortItem, gaid, item.getBrandId(), affs)).forEach(sorts::add);

    }
    return sorts;
  }

  private static SortDto convertSortDto(BrandPrioritySortDoc doc, String gaid, String brandId,
      List<String> affs) {
    final SortDto sort = new SortDto();
    sort.setBrands(Arrays.asList(convertBrandDto(doc, gaid, brandId)));
    sort.setAffiliates(affs);
    return sort;
  }
}
