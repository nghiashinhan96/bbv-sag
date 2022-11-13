package com.sagag.services.ivds.converter.unitree;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.utils.PermissionUtils;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.unitree.dto.FilterDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeFreetextSearchDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeNode;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public final class UnitreeConverter {

  private static final String UNITREE_NODE_PATH_SEPARATOR = " > ";

  public List<UnitreeFreetextSearchDto> convert(final UnitreeDoc unitreeDoc, final String text,
      Map<String, Boolean> shopAccessPermission) {
    String[] splitSearchText =
        StringUtils.split(StringUtils.lowerCase(text), SagConstants.WILDCARD);
    List<UnitreeNode> nodes = unitreeDoc.getUnitreeNodes().stream()
        .filter(isNodeNameMatched(splitSearchText).or(isNodeKeyWordMatched(splitSearchText)))
        .collect(Collectors.toList());
    nodes.removeIf(UnitreeNode::isRootNode);
    final List<String> parentIds =
        nodes.stream().map(UnitreeNode::getParentId).collect(Collectors.toList());
    return nodes.stream().filter(leafNodePredicate(parentIds))
        .map(node -> UnitreeFreetextSearchDto.builder().nodeName(getNodeFullPath(node, unitreeDoc))
            .activeLink(PermissionUtils.isActiveLink(node.getNodeExternalType(),
                node.getNodeExternalServiceAttribute(), shopAccessPermission))
            .treeId(unitreeDoc.getId()).parentId(node.getParentId()).nodeId(node.getLeafId())
            .build())
        .collect(Collectors.toList());
  }

  public UnitreeNodeDto toDto(UnitreeNode node) {
    if (node == null) {
      return null;
    }
    final UnitreeNodeDto unitreeDocDto = SagBeanUtils.map(node, UnitreeNodeDto.class);
    final List<FilterDto> filters =
        CollectionUtils.emptyIfNull(node.getFilters()).stream()
            .map(filter -> SagBeanUtils.map(filter, FilterDto.class))
            .collect(Collectors.toList());
    unitreeDocDto.setFilters(filters);
    return unitreeDocDto;
  }

  private Predicate<UnitreeNode> leafNodePredicate(List<String> parentIds) {
    return node -> !parentIds.contains(node.getLeafId());
  }

  private Predicate<UnitreeNode> isNodeNameMatched(String[] splitSearchText) {
    return node -> StringUtils.containsAny(StringUtils.lowerCase(node.getNodeName()),
        splitSearchText) || ArrayUtils.isEmpty(splitSearchText);
  }

  private Predicate<UnitreeNode> isNodeKeyWordMatched(String[] splitSearchText) {
    return node -> StringUtils.containsAny(StringUtils.lowerCase(node.getNodeKeywords()),
        splitSearchText) || ArrayUtils.isEmpty(splitSearchText);
  }

  private String getNodeFullPath(UnitreeNode node, UnitreeDoc unitreeDoc) {
    if (node == null) {
      return StringUtils.EMPTY;
    }
    if (StringUtils.isBlank(node.getParentId())) {
      return node.getNodeName();
    }

    List<String> nodeNames = Lists.newArrayList(node.getNodeName());
    String currentParentId = node.getParentId();
    while (StringUtils.isNotBlank(currentParentId)) {
      Optional<UnitreeNode> parentNodeOpt = unitreeDoc.findNodeById(currentParentId);
      if (!parentNodeOpt.isPresent()) {
        break;
      }
      UnitreeNode parentNode = parentNodeOpt.get();
      currentParentId = parentNode.getParentId();
      if (parentNode.isRootNode()) {
        break;
      }
      nodeNames.add(parentNode.getNodeName());
    }
    nodeNames.add(unitreeDoc.getTreeName());
    Collections.reverse(nodeNames);
    return String.join(UNITREE_NODE_PATH_SEPARATOR, nodeNames);
  }

}
