package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.service.MappingUserIdEblConnectService;
import com.sagag.services.tools.service.TargetOrganisationService;
import com.sagag.services.tools.utils.QueryUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MappingUserIdEblConnectServiceImpl implements MappingUserIdEblConnectService {

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  protected TargetOrganisationService targetOrganisationService;

  @Autowired
  private MappingUserIdEblConnectRepository mappingUserIdEblConnectRepo;

  @Override
  public Long searchUserIdByEbl(Long eblUserId) {
    return mappingUserIdEblConnectRepo.findUserIdsByEbl(eblUserId).stream()
        .sorted(Comparator.reverseOrder()).findFirst().orElse(null);
  }

  @Override
  public Optional<Long> searchEblOrgId(String customerNr) {
    if (StringUtils.isBlank(customerNr)) {
      log.warn("Not found any customer number with empty value");
      return Optional.empty();
    }

    final Optional<Organisation> organisation = orgRepo.findOneByOrgCode(customerNr);
    if (!organisation.isPresent()) {
      log.warn("Not found any existing organisation with this customer = {}", customerNr);
      return Optional.empty();
    }

    final Optional<MappingUserIdEblConnect> mappingItemOpt =
        mappingUserIdEblConnectRepo.findByConnectOrgIdIn(Arrays.asList(organisation.get().getId())).stream().findFirst();
    if (!mappingItemOpt.isPresent()) {
      log.warn("Not found any EBL customer infor from mapping table");
      return Optional.empty();
    }

    final Long eblOrgId = mappingItemOpt.get().getEblOrgId();
    return Optional.of(eblOrgId);
  }

  @Override
  public List<Long> searchVendorIds(List<String> custNrs) {
    final List<String> distincedList = QueryUtils.getDistinctTrimedValues(custNrs);

    final List<Integer> availableOrgIdList =
        targetOrganisationService.findOrganisationIdHasOfferPermission(distincedList);
    Assert.notEmpty(availableOrgIdList, "The given list of customer number is empty");
    if (CollectionUtils.isEmpty(availableOrgIdList)) {
      throw new IllegalArgumentException(
          "The given list of customer number don't have permission to use offer module function");
    }
    final List<MappingUserIdEblConnect> connects =
        mappingUserIdEblConnectRepo.findByConnectOrgIdIn(availableOrgIdList);
    return connects.stream().map(MappingUserIdEblConnect::getEblOrgId)
        .distinct().collect(Collectors.toList());
  }
}
