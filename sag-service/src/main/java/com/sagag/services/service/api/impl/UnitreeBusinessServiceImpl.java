package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeCompactDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.elasticsearch.api.UnitreeSearchService;
import com.sagag.services.service.api.UnitreeBusinessService;
import com.sagag.services.service.uniparts.unitree.TreeUnitreeBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UnitreeBusinessServiceImpl implements UnitreeBusinessService {

  @Autowired
  private UnitreeSearchService unitreeSearchService;

  @Autowired
  private TreeUnitreeBuilder treeUnitreeBuilder;

  private final String TREE_SORT_NOT_ACTIVE_STATUS = "0";

  @Override
  public Optional<UnitreeNodeDto> getUnitreeNodesByUnitreeId(String unitreeId, UserInfo userInfo) {
    Assert.hasText(unitreeId, "The given universal part tree id must not be empty");
    UnitreeNodeDto unitree = treeUnitreeBuilder
        .apply(unitreeSearchService.getUnitreeByUnitreeId(unitreeId).orElse(null), userInfo);
    return Optional.ofNullable(unitree);
  }

  @Override
  public List<UnitreeCompactDto> getAllUnitreeCompact(SupportedAffiliate affiliate) {
    return unitreeSearchService.getAllUnitreeCompact().stream()
        .map(doc -> SagBeanUtils.map(doc, UnitreeCompactDto.class))
        .map(treeUnitreeBuilder.updateTreeSortByAffiliate(affiliate)).filter(isActive())
        .sorted(Comparator.comparing(UnitreeCompactDto::getTreeSort)).collect(Collectors.toList());
  }

  @Override
  public Optional<UnitreeCompactDto> getUnitreeByLeafId(String leafId) {
    return unitreeSearchService.getUnitreeByLeafId(leafId)
        .map(doc -> SagBeanUtils.map(doc, UnitreeCompactDto.class));
  }

  private Predicate<UnitreeCompactDto> isActive() {
    return tree -> !StringUtils.equals(tree.getTreeSort(), TREE_SORT_NOT_ACTIVE_STATUS);
  }

}
