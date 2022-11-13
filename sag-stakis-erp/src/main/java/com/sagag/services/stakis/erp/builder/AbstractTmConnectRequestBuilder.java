package com.sagag.services.stakis.erp.builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sagag.services.article.api.builder.IGetErpInformationRequestBuilder;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.stakis.erp.domain.TmBasketPosition;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.enums.StakisSendMethodType;
import com.sagag.services.stakis.erp.utils.StakisConstants;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfArticleTmf;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfDispatchType;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfEntityLink;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfSelectionList;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfVehicle;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArticleTmf;
import com.sagag.services.stakis.erp.wsdl.tmconnect.BaseRequest;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Credentials;
import com.sagag.services.stakis.erp.wsdl.tmconnect.DispatchType;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EMasterDataType;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EntityLink;
import com.sagag.services.stakis.erp.wsdl.tmconnect.MasterData;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ObjectFactory;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SelectionList;
import com.sagag.services.stakis.erp.wsdl.tmconnect.SupplierTmf;
import com.sagag.services.stakis.erp.wsdl.tmconnect.User;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Vehicle;

public abstract class AbstractTmConnectRequestBuilder<T, R>
    implements IGetErpInformationRequestBuilder<TmUserCredentials, T, R> {

  @Autowired
  @Qualifier("tmConnectObjectFactory")
  protected ObjectFactory objectFactory;

  /**
   * Builds the request credentials.
   *
   */
  protected void buildCredentials(BaseRequest request, TmUserCredentials userCredentials) {
    // User credentials
    request.setCredentials(
        objectFactory.createCredentials(credentialsRequestTransfer().apply(userCredentials)));
    request.setContextId(userCredentials.getContextId());
    request.setLanguageCodeIso6391(
        objectFactory.createBaseRequestLanguageCodeIso6391(userCredentials.getLang()));
    request.setTimeStamp(DateUtils.newXMLGregorianCalendar(Instant.now()));
  }

  protected Function<TmUserCredentials, Credentials> credentialsRequestTransfer() {
    return credentials -> {
      final Credentials tmCredentials = objectFactory.createCredentials();
      tmCredentials.setIsEncrypted(false);
      final User user = objectFactory.createUser();
      user.setCustomerId(objectFactory.createUserCustomerId(credentials.getCustomerId()));

      Optional.ofNullable(credentials.getPassword()).filter(StringUtils::isNotBlank)
          .ifPresent(password -> user.setPassword(objectFactory.createUserPassword(password)));

      Optional.ofNullable(credentials.getUsername()).filter(StringUtils::isNotBlank)
          .ifPresent(username -> user.setUsername(objectFactory.createUserUsername(username)));

      if (!StringUtils.isBlank(credentials.getSalesUsername())) {
        tmCredentials.setSalesAdvisorCredentials(createSaleUserInCaseSaleOnBehalf(
            credentials.getSaleOriginId(), credentials.getSalesUsername()));
      }

      tmCredentials
          .setCatalogUserCredentials(objectFactory.createCredentialsCatalogUserCredentials(user));
      tmCredentials.setIsEncrypted(true);
      tmCredentials.setSecurityToken(
          objectFactory.createCredentialsSecurityToken(credentials.getSecurityToken()));
      return tmCredentials;
    };
  }

  private JAXBElement<User> createSaleUserInCaseSaleOnBehalf(String originSaleId,
      String salesUsername) {
    final User saleUser = objectFactory.createUser();
    saleUser.setMandatorId(objectFactory.createUserMandatorId(
        StakisConstants.CZ_SALES_MADATORY_ID));
    saleUser.setUsername(objectFactory.createUserUsername(salesUsername));
    saleUser.setCustomerId(objectFactory.createUserCustomerId(originSaleId));
    return objectFactory.createCredentialsSalesAdvisorCredentials(saleUser);
  }

  protected MasterData buildMasterData(Map<String, TmBasketPosition> positionMap,
      Map<String, Integer> typeMap) {
    return buildMasterData(positionMap, typeMap, Collections.emptyMap());
  }

  /**
   * Builds request master data.
   *
   */
  protected MasterData buildMasterData(Map<String, TmBasketPosition> positionMap,
      Map<String, Integer> typeMap, Map<String, String> deliveryMap) {

    final ArrayOfArticleTmf arrayOfArticleTmf = objectFactory.createArrayOfArticleTmf();
    positionMap.forEach(updateMasterArticles(arrayOfArticleTmf));

    final MasterData masterData = objectFactory.createMasterData();
    masterData.setArticleTmfs(objectFactory.createMasterDataArticleTmfs(arrayOfArticleTmf));

    // Vehicles
    if (!MapUtils.isEmpty(typeMap)) {
      final ArrayOfVehicle arrayOfVehicle = buildMasterVehicles(typeMap);
      masterData.setVehicles(objectFactory.createMasterDataVehicles(arrayOfVehicle));
    }

    // DispatchTypes
    if (!MapUtils.isEmpty(deliveryMap)) {
      final ArrayOfDispatchType arrayOfDispatchType = buildMasterDelivery(deliveryMap);
      masterData.setDispatchTypes(objectFactory.createMasterDataDispatchTypes(arrayOfDispatchType));
    }
    return masterData;
  }

  private BiConsumer<String, BasketPosition> updateMasterArticles(
      ArrayOfArticleTmf arrayOfArticleTmf) {
    return (uuid, position) -> {
      final ArticleTmf artTmf = objectFactory.createArticleTmf();
      artTmf.setGuid(uuid);

      artTmf.setArticleIdErp(
          objectFactory.createArticleArticleIdErp(String.valueOf(position.getArticleId())));
      // need to confirm about typeId
      artTmf.setType(TYPE_ID_NORMAL);
      artTmf.setArticleIdSupplier(
          objectFactory.createArticleTmfArticleIdSupplier(position.getBrand()));

      final SupplierTmf supplierTmf = objectFactory.createSupplierTmf();
      if (position.getBrandId() != null) {
        supplierTmf.setSupplierId(position.getBrandId().intValue());
      }

      // Need to confirm about poolId
      supplierTmf.setPoolId(POOL_ID_TECDOC);
      artTmf.setSupplier(objectFactory.createArticleTmfSupplier(supplierTmf));
      arrayOfArticleTmf.getArticleTmf().add(artTmf);
    };
  }

  private ArrayOfVehicle buildMasterVehicles(Map<String, Integer> typeMap) {
    final List<Vehicle> vehicles = typeMap.entrySet().stream().map(entry -> {
      final Vehicle veh = objectFactory.createVehicle();
      veh.setGuid(entry.getKey());
      veh.setKTypeId(entry.getValue());
      return veh;
    }).collect(Collectors.toList());
    final ArrayOfVehicle arrayOfVehicle = objectFactory.createArrayOfVehicle();
    arrayOfVehicle.getVehicle().addAll(vehicles);
    return arrayOfVehicle;
  }

  private ArrayOfDispatchType buildMasterDelivery(Map<String, String> deliveryMap) {
    final Function<Entry<String, String>, DispatchType> mappingFunction = entry -> {
      final DispatchType dispatchType = objectFactory.createDispatchType();
      dispatchType.setGuid(entry.getKey());
      // cz #3482
      int idOfStakisSendMethodType =
          StakisSendMethodType.findBySendMethod(SendMethodType.valueOf(entry.getValue())).getId();
      dispatchType.setText(
          objectFactory.createSelectionListItemText(String.valueOf(idOfStakisSendMethodType)));
      dispatchType.setId(idOfStakisSendMethodType);

      dispatchType.setIsExpressDelivery(false);
      dispatchType.setCosts(BigDecimal.ZERO);
      return dispatchType;
    };

    final List<DispatchType> dispatchTypes =
        deliveryMap.entrySet().stream().map(mappingFunction).collect(Collectors.toList());

    final ArrayOfDispatchType arrayOfDispatchType = objectFactory.createArrayOfDispatchType();
    arrayOfDispatchType.getDispatchType().addAll(dispatchTypes);
    return arrayOfDispatchType;
  }

  /**
   * Builds vehicle entity link request item.
   *
   */
  protected EntityLink buildVehicle(Entry<String, Integer> vehicleEntry) {
    final EntityLink vehicleItem = objectFactory.createEntityLink();
    vehicleItem.setGuid(vehicleEntry.getKey());
    vehicleItem.setType(EMasterDataType.VEHICLE);
    return vehicleItem;
  }

  /**
   * Builds delivery entity link request item.
   *
   */
  protected ArrayOfSelectionList buildDelivery(Map<String, String> deliveryMap) {
    final ArrayOfEntityLink arrayOfEntityLink = objectFactory.createArrayOfEntityLink();
    deliveryMap.forEach((uuid, deliveryInfo) -> {
      final EntityLink deliveryEntityLink = objectFactory.createEntityLink();
      deliveryEntityLink.setGuid(uuid);
      deliveryEntityLink.setType(EMasterDataType.DISPATCH_TYPE);
      arrayOfEntityLink.getEntityLink().add(deliveryEntityLink);
    });

    final SelectionList deliverySelectionList = objectFactory.createSelectionList();
    deliverySelectionList.setGuid(UUID.randomUUID().toString());
    deliverySelectionList.setType(1);
    deliverySelectionList.setItems(objectFactory.createSelectionListItems(arrayOfEntityLink));

    final ArrayOfSelectionList arrayOfSelectionList = objectFactory.createArrayOfSelectionList();
    arrayOfSelectionList.getSelectionList().add(deliverySelectionList);
    return arrayOfSelectionList;
  }

}
