package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.criteria.VehicleFilteringTerms;
import com.sagag.services.elasticsearch.criteria.vehicle.GtmotiveVehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.dto.VehicleSearchResponse;
import com.sagag.services.elasticsearch.enums.FreetextSearchOption;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.vehicles.GtmotiveVehicleSearchQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.VehicleQueryUtils;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.VehiclesResultExtractors;
import com.sagag.services.elasticsearch.query.vehicles.builder.VehicleAdvanceSearchQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class VehicleSearchServiceImpl extends AbstractElasticsearchService
  implements VehicleSearchService {

  private static final String LOG_GTMOTIVE_VEHICLE_SEARCH =
      "Searching vehicle with umc = {}, model = {}, engine = {} and drive code = {}";

  @Autowired
  private VehicleSearchQueryBuilder vehicleDescriptionQueryBuilder;

  @Autowired
  private VehicleAdvanceSearchQueryBuilder vehicleAdvanceSearchQueryBuilder;

  @Autowired
  private GtmotiveVehicleSearchQueryBuilder gtmotiveVehicleSearchQueryBuilder;

  @Override
  public String keyAlias() {
    return "vehicles";
  }

  @Override
  public Optional<VehicleDoc> searchVehicleByVehId(final String vehId) {
    if (StringUtils.isBlank(vehId)) {
      return Optional.empty();
    }
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .must(QueryBuilders.matchQuery(Index.Vehicle.VEH_ID.fullQField(), vehId));
    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withQuery(queryBuilder).withIndices(index())
        .withPageable(PageUtils.FIRST_ITEM_PAGE);
    return searchList(searchQueryBuilder.build(), VehicleDoc.class).stream().findFirst();
  }

  @Override
  public List<VehicleDoc> searchTypesByMakeModel(Integer idMake, Integer idModel, String yearFrom,
    String fuelType) {
    if (idMake == null || idModel == null) {
      return Collections.emptyList();
    }
    return searchList(buildQuery(idMake, idModel, yearFrom, fuelType).build(),
            VehicleDoc.class);
  }

  @Override
  public VehicleSearchResponse searchAdvanceTypesByMakeModel(VehicleSearchCriteria criteria, Pageable pageable) {
    return search(vehicleAdvanceSearchQueryBuilder.buildQuery(criteria, pageable, index()),
            VehiclesResultExtractors.extractByFiltering(pageable));
  }

  private NativeSearchQueryBuilder buildQuery(Integer idMake, Integer idModel, String yearFrom, String fuelType) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.ID_MAKE.fullQField(), idMake));
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.ID_MODEL.fullQField(), idModel));
    if (!StringUtils.isBlank(yearFrom)) {
      Integer yearMonthFrom = VehicleQueryUtils.getFormattedBuiltYearAndDecMonth(yearFrom);
      queryBuilder.must(QueryBuilders.rangeQuery(Index.Vehicle.SEARCH_BUILT_YEAR_FROM.field())
          .lte(yearMonthFrom));
      Integer yearMonthTill = VehicleQueryUtils.getFormattedBuiltYearAndJanMonth(yearFrom);
      queryBuilder.must(QueryBuilders.rangeQuery(Index.Vehicle.SEARCH_BUILT_YEAR_TILL.field())
          .gte(yearMonthTill));
    }
    if (!StringUtils.isBlank(fuelType)) {
      queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.VEHICLE_FUEL_TYPE_RAW.field(), fuelType));
    }

    log.debug("Query = {}", queryBuilder);

    return new NativeSearchQueryBuilder()
            .withQuery(queryBuilder).withIndices(index())
            .withPageable(PageUtils.MAX_PAGE);
  }

  @Override
  public Optional<VehicleDoc> searchGtmotiveVehicle(final String umc, final List<String> modelCodes,
      final List<String> engineCodes, final List<String> transmisionCodes) {
    log.debug(LOG_GTMOTIVE_VEHICLE_SEARCH, umc, modelCodes, engineCodes, transmisionCodes);

    final GtmotiveVehicleSearchCriteria criteria = new GtmotiveVehicleSearchCriteria();
    criteria.setUmc(umc);
    criteria.setModelCodes(modelCodes);
    criteria.setEngineCodes(engineCodes);
    criteria.setTransmisionCodes(transmisionCodes);

    final SearchQuery searchQuery = gtmotiveVehicleSearchQueryBuilder.buildQuery(criteria,
        PageUtils.MAX_PAGE, index());
    List<VehicleDoc> searchList = searchStream(searchQuery, VehicleDoc.class);
    return searchList.stream().sorted((v1, v2) -> {
      //Put the null to lowest priority
      if (v1.getGtDrv() == null) {
        return -1;
      }
      return v1.getGtDrv().compareTo(v2.getGtDrv());
    }).findFirst();
  }

  @Override
  public Optional<FreetextVehicleResponse> searchVehiclesByFreetext(
      final FreetextSearchOption option, final String text,
      final Optional<VehicleFilteringTerms> filterTerms, final Pageable pageable) {
    if (StringUtils.isBlank(text)) {
      return Optional.empty();
    }
    final VehicleSearchTermCriteria.VehicleSearchTermCriteriaBuilder termBuilder =
        VehicleSearchTermCriteria.builder();
    if (option.isVehiclesMotor()) {
      termBuilder.motorCodeDesc(text);
    } else {
      termBuilder.freeText(text);
    }

    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(termBuilder.build()).build();
    final SearchQuery searchQuery = vehicleDescriptionQueryBuilder.buildQuery(criteria, pageable,
      index());

    return searchOrDefault(searchQuery, VehiclesResultExtractors.extractByFreetext(pageable),
        Optional.empty());
  }
  

  @Override
  public List<VehicleDoc> getTopResultVehicleByKtypes(List<Integer> kTypeNrs) {
    log.debug("searching vehicle by kTypeNr = {}", kTypeNrs);
    if (CollectionUtils.isEmpty(kTypeNrs)) {
      return Collections.emptyList();
    }
    final List<VehicleDoc> result = new LinkedList<>();

    MultiSearchRequest request = new MultiSearchRequest();
    Client client = getEsClient();
    
    kTypeNrs.stream().forEach(ktype -> {
      final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
          .must(QueryBuilders.matchQuery(Index.Vehicle.KTYPE.fullQField(), ktype));

      log.debug("Query: {}", queryBuilder);
      final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
          .withQuery(queryBuilder).withIndices(index()).withPageable(PageUtils.FIRST_ITEM_PAGE);

      SearchQuery query = searchQueryBuilder.build();

      SearchRequestBuilder searchRequest = client
          .prepareSearch(query.getIndices().toArray(new String[0]))
          .setSearchType(query.getSearchType()).setTypes(query.getTypes().toArray(new String[0]))
          .setQuery(query.getQuery()).setFrom(0).setSize(1);
      
      request.add(searchRequest.request());
    });

    final MultiSearchResponse response = client.multiSearch(request).actionGet();

    SearchResponse[] responses = Arrays.asList(response.getResponses()).stream()
        .map(MultiSearchResponse.Item::getResponse).toArray(SearchResponse[]::new);
    final DefaultResultMapper resultMapper = new DefaultResultMapper();
   
    IntStream.range(0, responses.length).mapToObj(index -> responses[index])
        .collect(Collectors.toList()).forEach(res -> {
          resultMapper.mapResults(res, VehicleDoc.class, PageUtils.FIRST_ITEM_PAGE).stream()
              .findFirst().ifPresent(vehicle -> result.add(vehicle));
        });

    return result;    
  }
  
  private List<VehicleDoc> searchVehiclesByKTypeNr(final Integer kTypeNr, PageRequest pageRequest){
    log.debug("searching vehicle by kTypeNr = {}", kTypeNr);
    if (Objects.isNull(kTypeNr)) {
      return Collections.emptyList();
    }

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .must(QueryBuilders.matchQuery(Index.Vehicle.KTYPE.fullQField(), kTypeNr));
    
    log.debug("Query = {}", queryBuilder);

    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withQuery(queryBuilder).withIndices(index())
        .withPageable(pageRequest);
    
    log.debug("Query = {}", searchQueryBuilder);
    return searchList(searchQueryBuilder.build(), VehicleDoc.class);
  }

  @Override
  public List<VehicleDoc> searchVehiclesByKTypeNr(final Integer kTypeNr) {
    return searchVehiclesByKTypeNr(kTypeNr, PageUtils.MAX_PAGE);
  }

  @Override
  public VehicleSearchResponse searchVehiclesByCriteria(VehicleSearchCriteria criteria,
    Pageable pageable) {
    log.debug("Searching vehicle description in ES by criteria = {}", criteria);
    Assert.notNull(criteria, "The given search criteria must not be null");
    final SearchQuery query = vehicleDescriptionQueryBuilder.buildQuery(criteria, pageable, index());
    return search(query, VehiclesResultExtractors.extractByFiltering(pageable));
  }

  @Override
  public List<VehicleDoc> searchVehiclesByVehIds(String... vehIds) {
    return searchVehiclesByVehIds(vehIds, PageUtils.defaultPageable(ArrayUtils.getLength(vehIds)));
  }

  @Override
  public List<VehicleDoc> searchVehiclesByMakeModelForSpecificVehicleType(String vehicleType,
      List<String> vehicleSubClass, Integer idMake, Integer idModel, String cupicCapacity,
      String year) {
    if (idMake == null) {
      return Collections.emptyList();
    }
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(QueryBuilders.termsQuery(Index.Vehicle.VEHICLE_SUB_CLASS.fullQField(), vehicleSubClass));
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.VEHICLE_CLASS.fullQField(), vehicleType));
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.ID_MAKE.fullQField(), idMake));
    if(idModel != null) {
      queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.ID_MODEL.fullQField(), idModel));
    }

    if (!StringUtils.isBlank(year)) {
      queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.VEHICLE_MODEL_YEAR.fullQField(), year));
    }
    if (!StringUtils.isBlank(cupicCapacity)) {
      queryBuilder.must(
          QueryBuilders.matchQuery(Index.Vehicle.VEHICLE_CAPACITY_CC_TECH.field(), cupicCapacity));
    }

    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withQuery(queryBuilder).withIndices(index())
        .withPageable(PageUtils.MAX_PAGE);
    return searchList(searchQueryBuilder.build(), VehicleDoc.class);
  }

  private List<VehicleDoc> searchVehiclesByVehIds(String[] vehIds, Pageable pageable) {
    if (StringUtils.isAnyBlank(vehIds)) {
      return Collections.emptyList();
    }

    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        .must(QueryBuilders.termsQuery(Index.Vehicle.VEH_ID.fullQField(), vehIds));
    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
        .withQuery(queryBuilder).withIndices(index())
        .withPageable(pageable);
    return searchList(searchQueryBuilder.build(), VehicleDoc.class);
  }
}
