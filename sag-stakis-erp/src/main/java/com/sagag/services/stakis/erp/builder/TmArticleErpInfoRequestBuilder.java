package com.sagag.services.stakis.erp.builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.stakis.erp.domain.TmArticlePriceAndAvailabilityRequest;
import com.sagag.services.stakis.erp.domain.TmBasketPosition;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EMasterDataType;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EntityLink;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationRequest;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationRequestBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.MasterData;

@Component
@CzProfile
public class TmArticleErpInfoRequestBuilder
  extends AbstractTmConnectRequestBuilder<TmArticlePriceAndAvailabilityRequest,
    GetErpInformationRequestBody> {

  /**
   * Builds get article ERP information request.
   *
   */
  @Override
  public GetErpInformationRequestBody buildRequest(TmUserCredentials credentials,
      TmArticlePriceAndAvailabilityRequest request, Object... additionals) {
    final GetErpInformationRequest tmRequest = objectFactory.createGetErpInformationRequest();

    // User credentials
    buildCredentials(tmRequest, credentials);
    tmRequest.setTypeId(0);

    // Article UUID Map
    final List<TmBasketPosition> positions = request.getPositions();
    final Map<String, TmBasketPosition> articleMap = buildUUIDMap(positions);

    // Vehicle UUID Map
    final List<Integer> vehicleKTypeIds = positions.stream()
        .map(TmBasketPosition::getKtType).filter(Objects::nonNull).collect(Collectors.toList());
    final Map<String, Integer> ktTypeMap = buildUUIDMap(vehicleKTypeIds);

    // Article master data
    final MasterData masterData = buildMasterData(articleMap, ktTypeMap);
    tmRequest.setMasterData(objectFactory.createMasterData(masterData));

    // ERP Information data
    final ArrayOfErpInformation erpInformation = buildArrayOfErpInformation(articleMap, ktTypeMap);
    tmRequest.setErpArticleInformation(
        objectFactory.createGetErpInformationRequestErpArticleInformation(erpInformation));

    final GetErpInformationRequestBody erpInfoRequestBody =
        objectFactory.createGetErpInformationRequestBody();

    erpInfoRequestBody.setRequest(
        objectFactory.createGetErpInformationRequestBodyRequest(tmRequest));

    return erpInfoRequestBody;
  }

  /**
   * Builds ERP information request.
   *
   */
  private ArrayOfErpInformation buildArrayOfErpInformation(
      Map<String, TmBasketPosition> articles, Map<String, Integer> typeMap) {
    final ArrayOfErpInformation arrayOfErpInfo = objectFactory.createArrayOfErpInformation();
    articles.forEach((uuid, bPosition) -> {
      final Entry<String, Integer> vehicleEntry = typeMap.entrySet().stream()
          .filter(e -> NumberUtils.compare(e.getValue(), bPosition.getKtType()) == 0)
          .findFirst().orElse(null);

      updateErpPosition(arrayOfErpInfo, uuid, bPosition, vehicleEntry);
    });

    return arrayOfErpInfo;
  }

  private void updateErpPosition(final ArrayOfErpInformation arrayOfErpInfo, final String uuid,
      final TmBasketPosition position, final Entry<String, Integer> vehicleEntry) {
    final ErpInformation erpInfo = objectFactory.createErpInformation();

    erpInfo.setGuid(UUID.randomUUID().toString());
    erpInfo.setRequestedQuantity(BigDecimal.valueOf(position.getQuantity()));

    // need to confirm about ConfirmedQuantity
    erpInfo.setConfirmedQuantity(BigDecimal.ZERO);

    final EntityLink item = objectFactory.createEntityLink();
    item.setGuid(uuid);
    item.setType(EMasterDataType.ARTICLE_TMF);

    erpInfo.setItem(objectFactory.createErpInformationItem(item));

    // Vehicle
    if (vehicleEntry != null) {
      final EntityLink vehicleItem = buildVehicle(vehicleEntry);
      erpInfo.setVehicle(objectFactory.createEntityLink(vehicleItem));
    }

    arrayOfErpInfo.getErpInformation().add(erpInfo);
  }

}
