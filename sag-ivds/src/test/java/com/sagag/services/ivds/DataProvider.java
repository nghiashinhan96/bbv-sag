package com.sagag.services.ivds;

import com.google.common.collect.Maps;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleAccessoryCriteriaDto;
import com.sagag.services.domain.article.ArticleAccessoryDto;
import com.sagag.services.domain.article.ArticleAccessoryItemDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.ModelItem;
import com.sagag.services.domain.sag.external.CustomLink;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.freetext.SearchOptions;
import com.sagag.services.ivds.request.AccessorySearchRequest;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationItem;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class DataProvider {

  public static final String VEHICLE_ID = "V124808M32594";

  public static final String KTYPE = "124808";

  public static final String[] OIL_GAIDS = {"1862", "3224"};

  public static final String PLATE_SZ104289 = "SZ104289";

  public GtmotiveOperationRequest gtmotiveOperationRequest() {
    GtmotiveOperationRequest request = new GtmotiveOperationRequest();
    request.setMakeCode("SE1");

    final List<GtmotiveOperationItem> operations = new ArrayList<>();
    operations.add(GtmotiveOperationItem.builder().reference("07119963151")
        .description("Unterlegscheibe Ölablassschraube").build());
    operations.add(GtmotiveOperationItem.builder().reference("11427807177")
        .description("Ölfilter").build());
    operations.add(GtmotiveOperationItem.builder().reference("64319313517")
        .description("Pollenfilter").build());
    request.setOperations(operations);
    return request;
  }
  
  public GtmotiveOperationRequest gtmotiveOperationRequestDirectMatchesCase() {
    GtmotiveOperationRequest request = new GtmotiveOperationRequest();
    request.setMakeCode("VW1");

    final List<GtmotiveOperationItem> operations = new ArrayList<>();
    operations.add(GtmotiveOperationItem.builder().reference("1K0199867Q")
        .description("Unterlegscheibe Ölablassschraube").build());
    operations.add(GtmotiveOperationItem.builder().reference("1K0199868Q")
        .description("Ölfilter").build());
    request.setOperations(operations);
    return request;
  }

  public UserInfo buildUserInfo() {
    UserInfo user = new UserInfo();
    user.setId(1L);
    Customer customer = new Customer();
    customer.setNr(1100005L);
    customer.setDefaultBranchId("1001");

    final Map<String, CustomLink> map = new HashMap<>();
    map.put(Customer.PRICE_KEY, new CustomLink("/webshop-service/articles/Matik-Austria/prices"));
    map.put(Customer.AVAILABILITY_KEY,
        new CustomLink("/webshop-service/articles/Derendinger-Austria/availabilities"));
    customer.setLinks(map);

    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setAffiliateShortName("derendinger-at");
    user.setCompanyName("Derendinger-Austria");
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    return user;
  }

  public FreetextSearchRequest buildArticleSearchFreetext(String text) {
    return FreetextSearchRequest.builder()
        .searchOptions(Arrays.asList(SearchOptions.ARTICLES.lowerCase()))
        .user(buildUserInfo())
        .text(text)
        .pageRequest(PageUtils.DEF_PAGE)
        .build();
  }

  public FreetextSearchRequest buildVehicleSearchFreetext(String text) {
    return FreetextSearchRequest.builder()
        .searchOptions(Arrays.asList(SearchOptions.VEHICLES.lowerCase()))
        .user(buildUserInfo())
        .text(text)
        .pageRequest(PageUtils.DEF_PAGE)
        .build();
  }

  public ArticleFilterRequest buildArticleFilterRequest(String text, FilterMode mode) {
    return ArticleFilterRequest.builder()
        .keyword(text).filterMode(mode.name())
        .build();
  }

  public ArticleFilterRequest buildAccessoryListFilterRequest() {
    ArticleAccessoryDto access1 = ArticleAccessoryDto.builder().linkType(1).linkVal("123")
        .accesoryListsText("Access 1")
        .accesoryListItems(Arrays.asList(ArticleAccessoryItemDto.builder()
            .accessoryArticleIdArt("123").gaid("ga1").quantity(123)
            .criteria(
                Arrays.asList(ArticleAccessoryCriteriaDto.builder().cid("c1").cvp("vp1").build()))
            .build()))
        .seqNo(1)
        .build();

    ArticleAccessoryDto access2 = ArticleAccessoryDto.builder().linkType(2).linkVal("234")
        .accesoryListsText("Access 2")
        .accesoryListItems(Arrays.asList(ArticleAccessoryItemDto
            .builder().accessoryArticleIdArt("345").gaid("ga2").quantity(345).build()))
        .seqNo(2)
        .build();

    ArticleAccessoryDto access3 =
        ArticleAccessoryDto.builder().linkType(3).linkVal("345").accesoryListsText("Access 3")
            .accesoryListItems(Arrays.asList(
                ArticleAccessoryItemDto.builder().accessoryArticleIdArt("456").gaid("ga3").build()))
            .seqNo(3)
            .build();

    return ArticleFilterRequest.builder().accessorySearchRequest(AccessorySearchRequest.builder()
            .accessoryList(Arrays.asList(access1, access2, access3)).build())
        .filterMode(FilterMode.ACCESSORY_LIST.name())
        .build();
  }

  public List<String> buildGaidListAccessory() {
    final List<String> gaIds = new LinkedList<>();
    gaIds.add("ga1");
    gaIds.add("ga2");
    gaIds.add("ga3");
    return gaIds;
  }

  public List<String> buildMakeIdsAccessory() {
    final List<String> makeIds = new LinkedList<>();
    makeIds.add("123");
    return makeIds;
  }

  public List<VehicleDoc> buildVehicleAccessory() {
    final List<VehicleDoc> vehicles = new LinkedList<>();
    VehicleDoc vehicle = new VehicleDoc();
    vehicle.setKtType(345);
    vehicle.setVehicleModel("I8");
    vehicle.setVehicleBrand("BMW");
    vehicle.setVehicleName("1.4");
    vehicles.add(vehicle);
    return vehicles;
  }

  public List<Integer> buildKtypesAccessory() {
    final List<Integer> kTypes = new LinkedList<>();
    kTypes.add(345);
    return kTypes;
  }

  public Map<String, String> buildMakeMapAccessory() {
    Map<String, String> makeMap = new HashMap<>();
    makeMap.put("123", "Mercedes");
    makeMap.put("345", "BMW");
    return makeMap;
  }

  public Optional<ModelItem> buildModelAccessory() {
    return Optional.of(ModelItem.builder().idMake(345).model("2021").build());
  }

  public Map<String, GenArtTxt> buildGenartTxtMapAccessory() {
    Map<String, GenArtTxt> mapGA = new HashMap<>();

    GenArtTxt txt1 = new GenArtTxt();
    txt1.setGaid("ga1");
    txt1.setGatxtdech("ga txt 1");

    GenArtTxt txt2 = new GenArtTxt();
    txt2.setGaid("ga2");
    txt2.setGatxtdech("ga txt 2");

    GenArtTxt txt3 = new GenArtTxt();
    txt3.setGaid("ga3");
    txt3.setGatxtdech("ga txt 3");

    mapGA.put("ga1", txt1);
    mapGA.put("ga2", txt2);
    mapGA.put("ga3", txt3);
    return mapGA;
  }

  public Page<ArticleDocDto> buildArticlesAccessoryList() {
    final List<ArticleDocDto> articles = new ArrayList<>();

    final ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("123");
    article1.setIdSagsys(article1.getArtid());
    articles.add(article1);

    final ArticleDocDto article2 = new ArticleDocDto();
    article2.setArtid("345");
    article2.setIdSagsys(article2.getArtid());
    articles.add(article2);

    final ArticleDocDto article3 = new ArticleDocDto();
    article3.setArtid("456");
    article3.setIdSagsys(article3.getArtid());
    articles.add(article3);

    return new PageImpl<>(articles);
  }

  public Page<ArticleDocDto> buildEmptyArticle(Pageable pageable) {
    return new PageImpl<>(Collections.emptyList(), pageable, 0);
  }

  public Page<ArticleDocDto> buildArticles(Pageable pageable) {
    final List<ArticleDocDto> articles = new ArrayList<>();

    final ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid(UUID.randomUUID().toString());
    article1.setIdSagsys(article1.getArtid());
    articles.add(article1);

    final ArticleDocDto article2 = new ArticleDocDto();
    article2.setArtid(UUID.randomUUID().toString());
    article2.setIdSagsys(article2.getArtid());
    articles.add(article2);

    return new PageImpl<>(articles, pageable, articles.size());
  }

  public Page<ArticleDoc> buildArticleDocs(Pageable pageable) {
    final List<ArticleDoc> articles = new ArrayList<>();

    final ArticleDoc article1 = new ArticleDoc();
    article1.setArtid(UUID.randomUUID().toString());
    article1.setIdSagsys(article1.getArtid());
    articles.add(article1);

    final ArticleDoc article2 = new ArticleDoc();
    article2.setArtid(UUID.randomUUID().toString());
    article2.setIdSagsys(article2.getArtid());
    articles.add(article2);

    return new PageImpl<>(articles, pageable, articles.size());
  }

  public ArticleFilteringResponseDto buildMockedCachedResponse(Page<ArticleDocDto> articles) {
    return ArticleFilteringResponseDto.builder()
        .articles(articles).filters(Maps.newHashMap()).build();
  }
}
