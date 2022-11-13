package com.sagag.services.dvse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfGenericArticle;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfItem;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfString;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfTecDocType;
import com.sagag.services.dvse.wsdl.dvse.GenericArticle;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformation;
import com.sagag.services.dvse.wsdl.dvse.Item;
import com.sagag.services.dvse.wsdl.dvse.Order;
import com.sagag.services.dvse.wsdl.dvse.Quantity;
import com.sagag.services.dvse.wsdl.dvse.ResponseLimit;
import com.sagag.services.dvse.wsdl.dvse.SendOrder;
import com.sagag.services.dvse.wsdl.dvse.TecDocType;
import com.sagag.services.dvse.wsdl.dvse.User;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfArticleTmf;
import com.sagag.services.dvse.wsdl.tmconnect.ArrayOfErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.ArticleTmf;
import com.sagag.services.dvse.wsdl.tmconnect.Credentials;
import com.sagag.services.dvse.wsdl.tmconnect.EMasterDataType;
import com.sagag.services.dvse.wsdl.tmconnect.EntityLink;
import com.sagag.services.dvse.wsdl.tmconnect.ErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationRequest;
import com.sagag.services.dvse.wsdl.tmconnect.MasterData;

import lombok.experimental.UtilityClass;

/**
 * Utilities for DVSE Data Test.
 */
@UtilityClass
public final class DvseDataProvider {

  /**
   * Create get article information data for test GetArticleInfos service.
   *
   */
  public static GetArticleInformation createGetArticleInfomation() {

    GetArticleInformation getArticleInformation = new GetArticleInformation();
    getArticleInformation.setUser(createUser("1F255CF3F5337E5F", "31D7F6939DA6EC14",
            "9A339B241B", "26"));
    getArticleInformation.setItems(createArrayOfItem());
    getArticleInformation.setResponseLimit(createResponseLimit());
    return getArticleInformation;
  }

  public static GetArticleInformation createGetArticleInfomationWithLockedStatusOnES() {

    GetArticleInformation getArticleInformation = new GetArticleInformation();
    getArticleInformation.setUser(createUser("1F255CF3F5337E5F", "31D7F6939DA6EC14", "9A339B241B", "26"));
    getArticleInformation.setItems(createArrayOfItemWithLockedStatusOnES());
    getArticleInformation.setResponseLimit(createResponseLimit());
    return getArticleInformation;
  }

  public static GetErpInformation createArticleInformationRequestAxCz() {
    String uuid = UUID.randomUUID().toString();
    GetErpInformation requestErpInfo = new GetErpInformation();
    GetErpInformationRequest requestErp = new GetErpInformationRequest();
    ArrayOfErpInformation erpInfoArray = new ArrayOfErpInformation();
    List<ErpInformation> erpInfos = new ArrayList<>();
    ErpInformation erpInfo = new ErpInformation();
    erpInfo.setGuid(UUID.randomUUID().toString());
    EntityLink item = new EntityLink();
    item.setGuid(uuid);
    item.setType(EMasterDataType.ARTICLE_TMF);
    erpInfo.setRequestedQuantity(BigDecimal.ONE);
    erpInfo.setItem(item);
    erpInfos.add(erpInfo);
    erpInfoArray.getErpInformation().addAll(erpInfos);


    MasterData masterData = new MasterData();
    ArrayOfArticleTmf artTmfArray = new ArrayOfArticleTmf();
    List<ArticleTmf> artTmfs = new ArrayList<>();
    ArticleTmf artTmf = new ArticleTmf();
    artTmf.setArticleIdErp("1000082238");
    artTmf.setGuid(uuid);
    artTmfs.add(artTmf);
    artTmfArray.getArticleTmf().addAll(artTmfs);
    masterData.setArticleTmfs(artTmfArray);

    Credentials credential = new Credentials();
    com.sagag.services.dvse.wsdl.tmconnect.User user = new com.sagag.services.dvse.wsdl.tmconnect.User();

    user.setCustomerId("1100007");
    user.setPassword("Sbiils3PW");
    user.setUsername("000001");
    credential.setCatalogUserCredentials(user);
    ;
    requestErp.setMasterData(masterData);
    requestErp.setErpArticleInformation(erpInfoArray);
    requestErp.setCredentials(credential);
    requestErpInfo.setRequest(requestErp);
    return requestErpInfo;
  }

  /**
   * Create array of items for test addItemsToCart service.
   *
   */
  public static SendOrder createAddItemsToCart() {
    SendOrder sendOrder = new SendOrder();
    sendOrder.setUser(createUser("1F255CF3F5337E5F", "31D7F6939DA6EC14", "9A339B241B", "26"));
    sendOrder.setItems(createArrayOfItemForAddToCart());
    sendOrder.setOrder(createOrder("ORDER_ID_1234567"));
    return sendOrder;
  }

  /**
   * Create array of items for test addItemsToCart service with attached articles case.
   *
   */
  public static SendOrder createAddItemsToCartWithDepotAndRecycleCase() {
    SendOrder sendOrder = new SendOrder();
    sendOrder.setUser(createUser("1F255CF3F5337E5F", "31D7F6939DA6EC14", "9A339B241B", "26"));
    sendOrder.setItems(createArrayOfItemForAddToCartWithDepotAndRecycleCase());
    sendOrder.setOrder(createOrder("ORDER_ID_1234567"));
    return sendOrder;
  }

  public static Order createOrder(String orderId) {
    Order order = new Order();
    order.setOrderId(orderId);
    return order;
  }

  public static ArrayOfItem createArrayOfItemForAddToCart() {
    final ArrayOfItem arrayOfItem = new ArrayOfItem();
    arrayOfItem.getItem().add(createItem("1001250836", 2, 504, "MANN-FILTER", "W 712/22 (10)", 7));
    return arrayOfItem;
  }

  public static ArrayOfItem createArrayOfItemForAddToCartWithDepotAndRecycleCase() {
    final ArrayOfItem arrayOfItem = new ArrayOfItem();
    arrayOfItem.getItem().add(createItem("1000338890", 2, 504, "MANN-FILTER", "W 712/22 (10)", 7));
    return arrayOfItem;
  }

  public static ArrayOfItem createArrayOfItem() {
    final ArrayOfItem arrayOfItem = new ArrayOfItem();

    arrayOfItem.getItem().add(createItem("1000397663", 1, 504, "MANN-FILTER", "W 712/22", 7));
    // arrayOfItem.getItem().add(createItem("1000320790", 2, 504, "MANN-FILTER", "W 712/22 (10)",
    // 7));
    // arrayOfItem.getItem().add(createItem("1000542844", 2, 504, "MANN-FILTER", "W 712/22 (10)",
    // 7));
    // arrayOfItem.getItem().add(createItem("1000740841", 2, 504, "MANN-FILTER", "W 712/22 (10)",
    // 7));
    // arrayOfItem.getItem().add(createItem("1001261483", 1, 34, "KNECHT", "LX 2672", 8));
    // arrayOfItem.getItem().add(createItem("1001260990", 1, 204, "KNECHT", "LX 2672", 8));

    return arrayOfItem;
  }

  public static ArrayOfItem createArrayOfItemWithLockedStatusOnES() {
    final ArrayOfItem arrayOfItem = new ArrayOfItem();
    arrayOfItem.getItem()
        .add(createItem(pimIdIsLockedWithDat(), 2, 504, "MANN-FILTER", "W 712/22 (10)", 7));
    return arrayOfItem;
  }

  public static String pimIdIsLockedWithDat() {
    return "1000542844";
  }

  public static User createUser(String username, String password, String customerId, String saleId) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setCustomerId(customerId);
    user.setExternalSessionId(saleId);
    return user;
  }

  public static Item createItem(String idSagsys, int quantityNr, int supplierId, String supplier,
      String supplierArticleNr, int genArtNr) {

    Item item = new Item();
    item.setWholesalerArticleNumber(idSagsys);
    item.setSupplierName(supplier);
    item.setSupplierId(supplierId);
    item.setSupplierArticleNumber(supplierArticleNr);

    item.setTecDocTypes(createArrayOfTecDocType());

    final ArrayOfString memo = new ArrayOfString();
    memo.getString().add(StringUtils.EMPTY);
    item.setMemo(memo);

    item.setRequestedQuantity(createQuantity(quantityNr));
    item.setGenericArticles(createArrayOfGenericArticle(8));

    item.setFlagPackedForSelfService(NumberUtils.SHORT_ZERO);
    item.setFlagMaterialLabelingObligation(NumberUtils.SHORT_ZERO);
    item.setFlagExchangePart(NumberUtils.SHORT_ZERO);
    item.setArticleStatus(0);
    item.setFlagAccessory(NumberUtils.SHORT_ZERO);
    item.setHasActionPrice(false);

    return item;
  }

  public static ArrayOfTecDocType createArrayOfTecDocType() {
    ArrayOfTecDocType arrayOfTecDocType = new ArrayOfTecDocType();
    arrayOfTecDocType.getTecDocType().add(createTecDocType());
    return arrayOfTecDocType;
  }

  public static TecDocType createTecDocType() {
    TecDocType tecDocType = new TecDocType();
    tecDocType.setTecDocTypeId(4912);
    tecDocType.setTecDocModelId(0);
    tecDocType.setTecDocManufacturerId(771);
    tecDocType.setVehicleType(0);
    tecDocType.setVIN(StringUtils.EMPTY);
    tecDocType.setDescription(StringUtils.EMPTY);
    tecDocType.setFullDescription("MASERATI BITURBO (70, 77) 453..");
    return tecDocType;
  }

  public static Quantity createQuantity(int quantityNr) {
    final Quantity quantity = new Quantity();
    quantity.setValue(new BigDecimal(quantityNr));
    quantity.setMinQuantity(BigDecimal.ZERO);
    quantity.setMaxQuantity(BigDecimal.ZERO);
    quantity.setLotSize1(0);
    quantity.setLotSize2(0);
    quantity.setDivision(BigDecimal.ZERO);
    quantity.setQuantityPackingUnit(BigDecimal.ZERO);
    return quantity;
  }

  public static ArrayOfGenericArticle createArrayOfGenericArticle(Integer... genArtNrs) {
    ArrayOfGenericArticle arrayOfGenericArticle = new ArrayOfGenericArticle();
    Arrays.asList(genArtNrs).stream().forEach(
        genArtNr -> arrayOfGenericArticle.getGenericArticle().add(createGenericArticle(genArtNr)));
    return arrayOfGenericArticle;
  }

  public static GenericArticle createGenericArticle(int genArtNr) {
    final GenericArticle genericArticle = new GenericArticle();
    genericArticle.setGenericArticleId(genArtNr);
    genericArticle.setDescription("");
    return genericArticle;
  }

  public static ResponseLimit createResponseLimit() {
    final ResponseLimit responseLimit = new ResponseLimit();
    responseLimit.setIsResponseBounded(false);
    responseLimit.setNumberOfRequestedItems(1);
    return responseLimit;
  }

  public static ConnectUser createConnectUser() {
    final UserInfo user = new UserInfo();
    user.setUsername(StringUtils.EMPTY);
    user.setCompanyName(SupportedAffiliate.DERENDINGER_AT.getCompanyName());

    final Customer customer = new Customer();
    customer.setSendMethodCode("TOUR");
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);


    final ExternalUserDto externalUser = new ExternalUserDto();
    final ConnectUser connectUser = new ConnectUser(user, externalUser);

    return connectUser;
  }

  public UserInfo createUserInfo(SupportedAffiliate aff, Long id, Optional<Customer> customer) {
    UserInfo userInfo = new UserInfo();
    userInfo.setId(id);
    userInfo.setUsername("test.admin");
    userInfo.setAffiliateShortName(aff.getAffiliate());
    userInfo.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    userInfo.setCompanyName(aff.getCompanyName());

    userInfo.setCustomer(customer.orElse(null));
    OwnSettings settings = new OwnSettings();
    UserSettings uSettings = new UserSettings();
    uSettings.setId(1);
    settings.setUserSettings(uSettings);
    userInfo.setSettings(settings);
    return userInfo;
  }

}
