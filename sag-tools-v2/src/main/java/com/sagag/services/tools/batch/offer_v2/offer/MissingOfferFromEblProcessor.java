package com.sagag.services.tools.batch.offer_v2.offer;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.repository.source.SourceOfferRepository;
import com.sagag.services.tools.repository.target.TargetOfferRepository;
import com.sagag.services.tools.service.MappingUserIdEblConnectService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@OracleProfile
@Slf4j
public class MissingOfferFromEblProcessor {

  @Autowired(required=false)
  private SourceOfferRepository sourceOfferRepo;

  @Autowired
  private TargetOfferRepository targetOfferRepo;

  @Autowired
  private MappingUserIdEblConnectService mappingUserIdEblConnectService;

  public List<SourceOffer> process(String customerNr) {
    final Optional<Long> eblOrgIdOpt = mappingUserIdEblConnectService.searchEblOrgId(customerNr);
    if (!eblOrgIdOpt.isPresent()) {
      log.warn("Not found any EBL customer infor from mapping table");
      return Collections.emptyList();
    }

    final Long eblOrgId = eblOrgIdOpt.get();
    final List<SourceOffer> sourceOffers = sourceOfferRepo.findByVendorId(eblOrgId);
    if (CollectionUtils.isEmpty(sourceOffers)) {
      log.warn("Not found any offer information of EBL vendor id = {}", eblOrgId);
      return Collections.emptyList();
    }
    final List<Long> offerIds = sourceOffers.stream().map(SourceOffer::getId)
      .collect(Collectors.toList());
    final List<TargetOffer> targetOffers = targetOfferRepo.findByIdIn(offerIds);
    if (CollectionUtils.isEmpty(targetOffers)) {
      log.warn("Not found any offers at Connect site with EBL offer ids = {}", offerIds);
      return Collections.emptyList();
    }
    final List<Long> existedOfferIds = targetOffers.stream().map(TargetOffer::getId)
      .collect(Collectors.toList());
    final List<SourceOffer> notFoundOffersAtConnect = new ArrayList<>(sourceOffers);
    notFoundOffersAtConnect.removeIf(offer -> existedOfferIds.contains(offer.getId()));
    return notFoundOffersAtConnect;

  }
}
