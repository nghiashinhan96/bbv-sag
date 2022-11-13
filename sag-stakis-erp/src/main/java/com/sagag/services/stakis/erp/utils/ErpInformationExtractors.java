package com.sagag.services.stakis.erp.utils;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfArticleTmf;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfAvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArticleTmf;
import com.sagag.services.stakis.erp.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationReply;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.LinkedItemsCollection;
import com.sagag.services.stakis.erp.wsdl.tmconnect.MasterData;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErpInformationExtractors {

  public List<ErpInformation> extractErpInformationList(GetErpInformationResponseBody result) {
    if (result == null) {
      return Collections.emptyList();
    }
    final Optional<GetErpInformationReply> getErpInformationReplyType =
        getValueOpt(result.getGetErpInformationResult());

    if (!getErpInformationReplyType.isPresent()) {
      return Collections.emptyList();
    }

    final Optional<ArrayOfErpInformation> erpArticleInformations =
        getValueOpt(getErpInformationReplyType.get().getErpArticleInformation());
    if (!erpArticleInformations.isPresent()) {
      return Collections.emptyList();
    }

    return erpArticleInformations.map(ArrayOfErpInformation::getErpInformation)
        .orElse(Collections.emptyList());
  }

  public List<ErpInformation> extractErpInformationList(LinkedItemsCollection result) {
    if (result == null) {
      return Collections.emptyList();
    }
    return getValueOpt(result.getLinkedItems()).map(ArrayOfErpInformation::getErpInformation)
        .orElse(Collections.emptyList());
  }

  public Optional<MasterData> extractMasterData(GetErpInformationResponseBody result) {
    if (result == null) {
      return Optional.empty();
    }
    final Optional<GetErpInformationReply> erpInformationReply =
        getValueOpt(result.getGetErpInformationResult());
    if (!erpInformationReply.isPresent()) {
      return Optional.empty();
    }
    return getValueOpt(erpInformationReply.get().getMasterData());
  }

  public Map<String, String> extractArticleIdUUIDMap(MasterData masterData) {
    if (masterData == null) {
      return Collections.emptyMap();
    }
    final Optional<ArrayOfArticleTmf> arrayOfArticleTmfType =
        getValueOpt(masterData.getArticleTmfs());
    if (!arrayOfArticleTmfType.isPresent()) {
      return Collections.emptyMap();
    }

    final List<ArticleTmf> articleTmfTypes = arrayOfArticleTmfType
        .map(ArrayOfArticleTmf::getArticleTmf).orElse(Collections.emptyList());
    if (CollectionUtils.isEmpty(articleTmfTypes)) {
      return Collections.emptyMap();
    }

    return articleTmfTypes.stream()
        .filter(item -> Objects.nonNull(item.getArticleIdErp()))
        .collect(Collectors.toMap(
            i -> getValueOpt(i.getArticleIdErp()).orElse(StringUtils.EMPTY),
            ArticleTmf::getGuid, articleIdMerger()));
  }

  private BinaryOperator<String> articleIdMerger() {
    return (artId1, artId2) -> artId2;
  }

  public List<AvailabilityState> extractAvailabilityStates(MasterData masterData) {
    if (masterData == null) {
      return Collections.emptyList();
    }
    return getValueOpt(masterData.getAvailabilityStates())
        .map(ArrayOfAvailabilityState::getAvailabilityState)
        .orElse(Collections.emptyList());
  }

  public Map<String, AvailabilityState> extractAvailabilityStateMap(MasterData masterData) {
    return extractAvailabilityStates(masterData).stream()
        .collect(Collectors.toMap(AvailabilityState::getGuid, Function.identity()));
  }

}
