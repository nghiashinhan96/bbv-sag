package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.CustomerSearchService;
import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.elasticsearch.criteria.Telephone;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.enums.Index.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * Elasticsearch Customer service implementation class.
 */
@Service
public class CustomerSearchServiceImpl extends AbstractElasticsearchService
  implements CustomerSearchService {

  private static final String[] FREETEXT_CUSTOMER_FIELDS = new String[] {
    // customers_nn_customer
    Index.Customer.NAME.fullQField(),
    Index.Customer.ALIAS.fullQField(),
    Index.Customer.PEMAIL.fullQField(),
    Index.Customer.PFAX.fullQField(),
    Index.Customer.TAX_NR.fullQField(),
    Index.Customer.TAX_EX_CODE.fullQField(),
    Index.Customer.OFFICAL_REG_CODE.fullQField(),
    Index.Customer.MANDANT_CODE.fullQField(),
  };

  private static final String[] FREETEXT_CONTACT_FIELDS = new String[] {
     // customers_nn_customer.contacts
     Index.Customer.CONTACT_FNAME.fullQField(),
     Index.Customer.CONTACT_LNAME.fullQField(),
   };

   private static final String[] FREETEXT_ADDRESSES_FIELDS = new String[] {
     // customers_nn_customer.addresses
     Index.Customer.ADDR_ID_LOCATION.fullQField(),
     Index.Customer.ADDR_BUILDING_COMP.fullQField(),
     Index.Customer.ADDR_DESC.fullQField(),
     Index.Customer.ADDR_STREET.fullQField(),
     Index.Customer.ADDR_CITY.fullQField(),
     Index.Customer.ADDR_ZIP.fullQField()
   };

  @Override
  public String keyAlias() {
    return "customers";
  }

  @Override
  public Optional<CustomerDoc> searchCustomerByTelephone(CustomerSearchCriteria criteria) {
    Optional<Telephone> telephone = criteria.getTelephone();
    if (!telephone.isPresent()) {
      throw new IllegalArgumentException("The telephone number must not be empty");
    }
    if (CollectionUtils.isEmpty(criteria.getAffiliates())) {
      return Optional.empty();
    }

    final BoolQueryBuilder boolQuery = buildMadantQueryBoolBuilder(criteria.getAffiliates());

    // #1484, use default country code if not exist, "mandant" = 43
    final Telephone tel = telephone.get();
    boolQuery.must(
        QueryBuilders.termQuery(Index.Customer.PRIMARY_PHONE_CC.fullQField(),
        tel.getCountryCode()));
    boolQuery.must(
        QueryBuilders.termQuery(Index.Customer.PRIMARY_PHONE.fullQField(),
        tel.getNumber()));

    final NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
        .withIndices(index())
        .withQuery(boolQuery).build();

    return searchList(searchQuery, CustomerDoc.class).stream().findFirst();
  }

  @Override
  public Page<CustomerDoc> searchCustomerByFreetext(CustomerSearchCriteria criteria,
      Pageable pageable) {
    if (CollectionUtils.isEmpty(criteria.getAffiliates())) {
      return Page.empty();
    }

    final BoolQueryBuilder boolQuery = buildMadantQueryBoolBuilder(criteria.getAffiliates());

    // #1485 - AT-AX: Sales Agent: Customer Search - Free Text
    final String text = criteria.getText();

    final NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
        .withIndices(index())
        .withQuery(mustQuery(text, boolQuery)).withPageable(pageable).build();

    return searchPage(searchQuery, CustomerDoc.class);
  }

  private BoolQueryBuilder mustQuery(String keyword, BoolQueryBuilder boolQuery) {
    if (StringUtils.isEmpty(keyword)) {
      return boolQuery;
    }
    final List<String> words = Arrays.asList(keyword.split(StringUtils.SPACE));
    List<BoolQueryBuilder> shouldQueries =
        words.stream().map(this::shouldQuery).collect(Collectors.toList());
    shouldQueries.stream().forEach(boolQuery::must);
    return boolQuery;
  }

  private BoolQueryBuilder shouldQuery(final String text) {
    final BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
    orQuery.should(
        QueryBuilders.multiMatchQuery(text, FREETEXT_CUSTOMER_FIELDS).type(Type.PHRASE_PREFIX));

    orQuery.should(QueryBuilders.nestedQuery(Path.CUST_CONTACTS.getValue(),
        QueryBuilders.multiMatchQuery(text, FREETEXT_CONTACT_FIELDS).type(Type.PHRASE_PREFIX),
        ScoreMode.None));

    final BoolQueryBuilder addressBoolQuery = buildCustomerAddressQuery(text);

    orQuery.should(QueryBuilders.nestedQuery(Path.CUST_ADDRESSES.getValue(), addressBoolQuery,
        ScoreMode.None));
    return orQuery;
  }

  private BoolQueryBuilder buildCustomerAddressQuery(final String text) {
    final BoolQueryBuilder addressBoolQuery = QueryBuilders.boolQuery();
    MultiMatchQueryBuilder addressMultiMatchQuery =
        QueryBuilders.multiMatchQuery(text, FREETEXT_ADDRESSES_FIELDS).type(Type.PHRASE_PREFIX);
    addressBoolQuery.must(addressMultiMatchQuery);
    addressBoolQuery
        .must(QueryBuilders.termQuery(Index.Customer.ADDR_IS_PRIMARY.fullQField(), true));
    return addressBoolQuery;
  }

  private BoolQueryBuilder buildMadantQueryBoolBuilder(List<String> affiliates) {
    final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
    // since the mandant field is a not_analyzed type, thus search with lower case as default
    boolQuery.must(QueryBuilders.termsQuery(Index.Customer.MANDANT.fullQField(), affiliates));
    return boolQuery;
  }

  @Override
  public Optional<CustomerDoc> searchCustomerByCustomerNumber(CustomerSearchCriteria criteria) {
    final BoolQueryBuilder boolQuery = buildMadantQueryBoolBuilder(criteria.getAffiliates());
    final String customerNr = criteria.getCustomerNumber();
    boolQuery.must(
        QueryBuilders.termQuery(Index.Customer.CUSTOMER_ACCOUNT_NR.fullQField(), customerNr));

    final NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
        .withIndices(index())
        .withQuery(boolQuery).build();
    return searchList(searchQuery, CustomerDoc.class).stream().findFirst();
  }
}
