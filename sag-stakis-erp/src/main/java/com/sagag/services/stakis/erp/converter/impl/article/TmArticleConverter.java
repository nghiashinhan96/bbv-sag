package com.sagag.services.stakis.erp.converter.impl.article;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.stakis.erp.config.StakisErpProperties;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfKeyValueItem;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfLinkedItemsCollection;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfMemo;
import com.sagag.services.stakis.erp.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.KeyValueItem;
import com.sagag.services.stakis.erp.wsdl.tmconnect.LinkedItemsCollection;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Memo;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Price;

@Component
@CzProfile
public class TmArticleConverter implements InitializingBean {

  @Autowired
  private TmPriceConverterV2 priceConverterV2;

  @Autowired
  private TmAvailabilityConverter availabilityConverter;

  @Autowired
  private TmStatusInformationConverter statusInformationConverter;

  @Autowired
  private TmMemoTextConverter memoTextConverter;

  @Autowired
  private TmDepositItemConverter depositItemConverter;

  @Autowired
  private StakisErpProperties erpProps;

  @Autowired
  private TmQuantityMultipleConverter tmQuantityMultipleConverter;

  private Map<String, String> fGasItemTextMap;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fGasItemTextMap = MapUtils.emptyIfNull(erpProps.getConfig().getFgasItemText());
  }

  public ArticleDocDto convert(final ArticleDocDto article,
      final Map<String, String> articleIdUuidMap,
      final List<ErpInformation> erpInfos,
      final Map<String, AvailabilityState> availabilityStateTypes,
      final String language,
      final double vatRate) {
    Assert.notNull(article, "The given article must not be null");
    final String uuid = articleIdUuidMap.get(article.getIdSagsys());

    // Find ERP information of article
    final List<ErpInformation> filteredErpInfo = erpInfos.stream().filter(uuidPredicate(uuid))
        .collect(Collectors.toList());
    final Optional<ErpInformation> availabilitiesErpInfo = filteredErpInfo.stream().findFirst();

    availabilitiesErpInfo.map(tmQuantityMultipleConverter)
        .ifPresent(opt -> opt.ifPresent(article::setQtyMultiple));

    // ERP Article
    final Article erpArt = new Article();
    erpArt.setId(article.getIdSagsys());
    article.setArticle(erpArt);

    final List<KeyValueItem> keyValueItems = new ArrayList<>();

    // Memo status list
    keyValueItems.addAll(filteredErpInfo.stream()
        .flatMap(keyValueItemMapper()).collect(Collectors.toList()));

    article.setMemos(statusInformationConverter.apply(keyValueItems));

    // Memo text list
    final List<Memo> memoList = filteredErpInfo.stream()
        .flatMap(memoItemMapper()).collect(Collectors.toList());
    article.getMemos().addAll(memoTextConverter.apply(memoList));

    final Integer quantity = article.getAmountNumber();

    // Deposit item
    final Optional<KeyValueItem> depositKeyValueItemOpt = keyValueItems.stream()
        .filter(depositItemKeyValuePredicate()).findFirst();
    if (depositKeyValueItemOpt.isPresent()) {
      final List<LinkedItemsCollection> linkedItems = filteredErpInfo.stream()
          .flatMap(linkedItemsMapper()).collect(Collectors.toList());
      depositItemConverter.apply(linkedItems, articleIdUuidMap, availabilityStateTypes, language,
          vatRate, quantity)
      .ifPresent(depotArt -> {
        article.setDepositArticle(depotArt);
        article.getArticle().setDepotArticleId(depotArt.getIdSagsys());
      });
    }

    // Price list
    final List<Price> prices = availabilitiesErpInfo.map(priceTypeConverter())
        .orElse(Collections.emptyList());
    if (!CollectionUtils.isEmpty(prices)) {
      article.setPrice(priceConverterV2.apply(prices, quantity, vatRate));
      article.getPrice().setArticleId(article.getIdSagsys());
    }

    // Availabilities
    article.setAvailabilities(
        availabilityConverter.apply(availabilitiesErpInfo, availabilityStateTypes, language));

    article.setAllowedAddToShoppingCart(isAllowedAddToCart(article.getMemos(),
        this.fGasItemTextMap.get(language)));

    return article;
  }

  private static Predicate<ErpInformation> uuidPredicate(String uuid) {
    return erpInfo -> getValueOpt(erpInfo.getItem())
        .filter(eLink -> StringUtils.equalsIgnoreCase(uuid, eLink.getGuid())).isPresent();
  }

  private static Function<ErpInformation, List<Price>> priceTypeConverter() {
    return erpInfo -> getValueOpt(erpInfo.getPrices()).map(aOPrice -> aOPrice.getPrice())
        .orElse(Collections.emptyList());
  }

  private static Function<ErpInformation, Stream<KeyValueItem>> keyValueItemMapper() {
    return i -> getValueOpt(i.getStatusInformation()).map(ArrayOfKeyValueItem::getKeyValueItem)
        .orElse(Lists.newArrayList()).stream();
  }

  private static Function<ErpInformation, Stream<Memo>> memoItemMapper() {
    return i -> getValueOpt(i.getMemos()).map(ArrayOfMemo::getMemo)
        .orElse(Lists.newArrayList()).stream();
  }

  private static Function<ErpInformation, Stream<LinkedItemsCollection>> linkedItemsMapper() {
    return i -> getValueOpt(i.getLinkedItemsCollections())
        .map(ArrayOfLinkedItemsCollection::getLinkedItemsCollection)
        .orElse(Lists.newArrayList()).stream();
  }

  private static Predicate<KeyValueItem> depositItemKeyValuePredicate() {
    return i -> StringUtils.equalsIgnoreCase("DepositItem", i.getKey().getValue())
        && StringUtils.equalsIgnoreCase("Yes", i.getValue().getValue());
  }

  private static boolean isAllowedAddToCart(List<ErpArticleMemo> memos, String fGasTxt) {
    return memos.stream().allMatch(isContainsFGasWithCertification(fGasTxt));
  }

  private static Predicate<ErpArticleMemo> isContainsFGasWithCertification(String fGasTxt) {
    return memo -> !StringUtils.containsIgnoreCase(fGasTxt, memo.getText());
  }

}
