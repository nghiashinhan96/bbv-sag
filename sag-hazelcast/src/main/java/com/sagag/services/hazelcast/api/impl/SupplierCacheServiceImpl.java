package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.aggregation.Aggregators;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.api.SupplierSearchService;
import com.sagag.services.elasticsearch.domain.SupplierDoc;
import com.sagag.services.elasticsearch.domain.SupplierTxt;
import com.sagag.services.hazelcast.api.SupplierCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Cache service for supplier document.
 */
@Service
@Slf4j
public class SupplierCacheServiceImpl extends CacheDataProcessor implements SupplierCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private SupplierSearchService supplierService;

  @Override
  public Map<String, SupplierTxt> searchSupplierByIds(List<String> supplierIds) {
    log.debug("Seaching the suppliers from a list of ids");
    final IMap<String, SupplierDoc> suppliersMap = hazelcastInstance.getMap(defName());
    final Map<String, SupplierTxt> supplierTxts = new HashMap<>();
    suppliersMap
        .values(Predicates.in("suppTxts[any].dlnrid",
            supplierIds.toArray(new String[supplierIds.size()])))
        .stream().flatMap(doc -> doc.getSuppTxts().stream())
        .forEach(txt -> supplierTxts.put(txt.getDlnrid(), txt));
    return supplierTxts;
  }

  @Override
  public Map<String, String> searchSupplierNameByIds(List<String> supplierIds) {
    log.debug("Seaching the supplier names from a list of ids");
    final IMap<String, SupplierDoc> suppliersMap = hazelcastInstance.getMap(defName());
    final Map<String, String> supplierNames = new HashMap<>();
    suppliersMap
        .values(Predicates.in("suppTxts[any].dlnrid",
            supplierIds.toArray(new String[supplierIds.size()])))
        .stream().flatMap(doc -> doc.getSuppTxts().stream())
        .forEach(txt -> supplierNames.put(txt.getDlnrid(), txt.getSuppname()));
    return supplierNames;
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available suppliers from ES to Hazelcast instance");
    refreshCacheSuppliers(supplierService.getAll());
  }

  @Override
  public IMap<String, SupplierDoc> refreshCacheSuppliers(List<SupplierDoc> suppliers) {
    hazelcastInstance.getMap(defName()).evictAll();
    final IMap<String, SupplierDoc> suppliersMap = hazelcastInstance.getMap(defName());
    suppliers.parallelStream().forEach(doc -> suppliersMap.put(doc.getId(), doc));
    return suppliersMap;
  }

  @Override
  public List<SupplierTxt> findAllBrand() {
    return hazelcastInstance.getMap(defName()).values().stream()
        .map(SupplierDoc.class::cast)
        .map(SupplierDoc::getSuppTxts)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Page<SupplierTxt> findLikeByName(String searchSuppName, Pageable pageable) {
    log.debug("Finding the suppliers by mapping with suppname");
    List<SupplierTxt> resultInPage = new ArrayList<SupplierTxt>();
    int totalElements = 0;
    if (StringUtils.isBlank(searchSuppName)) {
      resultInPage = findAllBrand();
      resultInPage = filterInvalidSupplier(resultInPage);
      totalElements = resultInPage.size();
      resultInPage.sort(supplierComparator());
    } else {
      Predicate predicate = Predicates.ilike("suppTxts[any].suppname", StringUtils
          .replace(searchSuppName, SagConstants.WILDCARD, String.valueOf(SagConstants.LIKE_CHAR)));
      Comparator<Entry<String, SupplierDoc>> alphabetSorting = new AlphabetSorting();
      PagingPredicate pagingPredicate =
          new PagingPredicate<>(predicate, alphabetSorting, pageable.getPageSize());
      pagingPredicate.setPage(pageable.getPageNumber());
      final IMap<String, SupplierDoc> suppliersMap = hazelcastInstance.getMap(defName());
      totalElements =
          Math.toIntExact((long) suppliersMap.aggregate(Aggregators.count(), predicate));
      resultInPage = suppliersMap.values(pagingPredicate).stream()
          .flatMap(t -> t.getSuppTxts().stream()).collect(Collectors.toList());
    }

    return new PageImpl<SupplierTxt>(resultInPage, pageable, totalElements);

  }

  private Comparator<SupplierTxt> supplierComparator() {
    return Comparator.comparing(SupplierTxt::getSuppname, String.CASE_INSENSITIVE_ORDER);
  }

  private List<SupplierTxt> filterInvalidSupplier(List<SupplierTxt> resultInPage) {
    resultInPage = resultInPage.parallelStream()
        .filter(x -> StringUtils.isNotBlank(x.getSuppname())).collect(Collectors.toList());
    return resultInPage;
  }

  @SuppressWarnings("serial")
  public static class AlphabetSorting
      implements Serializable, Comparator<Map.Entry<String, SupplierDoc>> {

    @Override
    public int compare(Map.Entry<String, SupplierDoc> s1, Map.Entry<String, SupplierDoc> s2) {
      SupplierTxt doc1 = CollectionUtils.emptyIfNull(s1.getValue().getSuppTxts()).stream()
          .findFirst().orElse(null);
      SupplierTxt doc2 = CollectionUtils.emptyIfNull(s2.getValue().getSuppTxts()).stream()
          .findFirst().orElse(null);
      if (Objects.isNull(doc1) || Objects.isNull(doc2)) {
        return -1;
      }
      if (Objects.isNull(doc1.getSuppname())) {
        return Objects.isNull(doc2.getSuppname()) ? 0 : 1;
      } else if (Objects.isNull(doc2.getSuppname())) {
        return -1;
      }
      return doc1.getSuppname().compareToIgnoreCase(doc2.getSuppname());
    }
  }

}
