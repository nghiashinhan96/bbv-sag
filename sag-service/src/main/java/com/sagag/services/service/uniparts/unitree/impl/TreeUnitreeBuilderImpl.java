package com.sagag.services.service.uniparts.unitree.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.utils.PermissionUtils;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.domain.eshop.category.GenArtDto;
import com.sagag.services.domain.eshop.unitree.dto.TileDto;
import com.sagag.services.domain.eshop.unitree.dto.TileExternalDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeCompactDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.elasticsearch.domain.unitree.Tile;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeNode;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.ivds.converter.unitree.UnitreeConverter;
import com.sagag.services.service.uniparts.unitree.TreeUnitreeBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class TreeUnitreeBuilderImpl implements TreeUnitreeBuilder {

  private static final char TREE_SORT_LIST_SEPARATOR = ',';

  private static final char TREE_SORT_SEPARATOR = ':';

  @Autowired
  private BrandPriorityCacheService brandPriorityCacheService;

  @Override
  public Function<UnitreeCompactDto, UnitreeCompactDto> updateTreeSortByAffiliate(
      SupportedAffiliate affiliate) {
    return tree -> {
      if (affiliate != null) {
        String treeSort =
            Arrays.stream(StringUtils.split(tree.getTreeSort(), TREE_SORT_LIST_SEPARATOR))
                .filter(treeSortOfAffiliate(affiliate)).map(getTreeSortVal()).findFirst()
                .orElse(StringUtils.EMPTY);
        tree.setTreeSort(treeSort);
      }
      return tree;
    };
  }

  private Function<String, String> getTreeSortVal() {
    return s -> {
      String[] treeSorts = StringUtils.split(s, TREE_SORT_SEPARATOR);
      return treeSorts.length > 1 ? treeSorts[1] : StringUtils.EMPTY;
    };
  }

  private Predicate<String> treeSortOfAffiliate(SupportedAffiliate affiliate) {
    return s -> StringUtils.indexOf(s, affiliate.getEsShortName()) != -1;
  }

  @Override
  public UnitreeNodeDto apply(UnitreeDoc unitreeDoc, UserInfo userInfo) {
    if (unitreeDoc == null) {
      return null;
    }
    Map<String, Boolean> shopAccessPermission =
        PermissionUtils.getPermissionShopUniparts(userInfo.getPermissions());

    final Map<String, UnitreeNodeDto> nodeByLeafIds =
        unitreeDoc.getUnitreeNodes().stream().collect(Collectors.toMap(UnitreeNode::getLeafId,
            node -> UnitreeConverter.toDto(node)));

    final Map<String, PriorityQueue<TileDto>> tileByNodeLinkIds = new HashMap<>();

    for (Tile tile : unitreeDoc.getTiles()) {
      UnitreeNode node = unitreeDoc.getUnitreeNodes().stream()
          .filter(
              unitreeNode -> StringUtils.equals(unitreeNode.getLeafId(), tile.getTileNodeLink()))
          .findFirst().orElse(null);

      if (node != null) {
        PriorityQueue<TileDto> tiles = tileByNodeLinkIds.getOrDefault(node.getLeafId(),
            new PriorityQueue<>(tileDtoComparator()));

        TileDto tileDto = SagBeanUtils.map(tile, TileDto.class);

        activeLink(tileDto.getTileExternals(), shopAccessPermission);
        tiles.add(tileDto);
        tileByNodeLinkIds.put(node.getLeafId(), tiles);
      }
    }

    return buildUnitree(nodeByLeafIds, tileByNodeLinkIds, userInfo.getSupportedAffiliate(),
        shopAccessPermission).orElse(null);
  }

  private Optional<UnitreeNodeDto> buildUnitree(final Map<String, UnitreeNodeDto> nodeByLeafIds,
      final Map<String, PriorityQueue<TileDto>> tileByNodeLinkIds, SupportedAffiliate affiliate,
      Map<String, Boolean> shopAccessPermission) {
    // Get list leafId from elastic search by categoryText
    final Map<String, UnitreeNodeDto> nodeByLeafIdsFiltered = new HashMap<>(nodeByLeafIds);

    final List<UnitreeNodeDto> unitreeNodes = new ArrayList<>(nodeByLeafIdsFiltered.values());

    Collections.sort(unitreeNodes, nodeDtoSortComparator());

    unitreeNodes.stream().forEach(node -> {
      final String parentId = node.getParentId();
      final UnitreeNodeDto child = nodeByLeafIdsFiltered.getOrDefault(
          node.getId(), new UnitreeNodeDto());
      // Add child for parent
      if (validForAddChildPredicate(nodeByLeafIdsFiltered).test(parentId, child)) {
        final UnitreeNodeDto parent = nodeByLeafIdsFiltered.get(parentId);
        parent.addChildNode(child);
        child.setParentId(parentId);
      } else {
        child.setParentId(StringUtils.EMPTY);
      }

      // Add Tiles for this node
      if (tileByNodeLinkIds.containsKey(node.getId())) {
        final PriorityQueue<TileDto> tiles = tileByNodeLinkIds.get(node.getId());
        child.setTiles(tiles);
      }
      child.setActiveLink(PermissionUtils.isActiveLink(child.getNodeExternalType(),
          child.getNodeExternalServiceAttribute(), shopAccessPermission));
    });
    setGenArtsIntoNode(nodeByLeafIdsFiltered, affiliate);

    Optional<UnitreeNodeDto> parentOpt = nodeByLeafIdsFiltered.values().stream()
        .filter(node -> StringUtils.isBlank(node.getParentId())).findFirst();

    // SORT ALL CHILDREN OF PARENT BY SORT
    if (parentOpt.isPresent()) {
      PriorityQueue<UnitreeNodeDto> childSortedBySort =
          new PriorityQueue<>(nodeDtoSortComparator());
      parentOpt.get().getChildren().stream().forEach(childSortedBySort::add);
      parentOpt.get().setChildren(childSortedBySort);
    }

    return parentOpt;
  }

  private void setGenArtsIntoNode(final Map<String, UnitreeNodeDto> nodeByLeafIdsFiltered,
      SupportedAffiliate affiliate) {
    nodeByLeafIdsFiltered.forEach((key, node) -> {
      if (brandSortingRequired(node)) {
        List<String> gaids = Lists.newArrayList(
            StringUtils.split(node.getInclude().getGenarts(), SagConstants.SEMICOLON));
        Map<String, CachedBrandPriorityDto> cachedBrandPrio =
            brandPriorityCacheService.findCachedBrandPriority(gaids);
        List<GenArtDto> genArts = cachedBrandPrio.values().stream().map(genArtConverter(affiliate))
            .collect(Collectors.toList());
        node.setGenArts(genArts);
      }
    });
  }

  private boolean brandSortingRequired(UnitreeNodeDto node) {
    return node.isBrandSortingEnabled() && nodeHasIncludeGaIds(node);
  }

  private boolean nodeHasIncludeGaIds(UnitreeNodeDto node) {
    return node.getInclude() != null && StringUtils.isNotBlank(node.getInclude().getGenarts());
  }

  private static BiPredicate<String, UnitreeNodeDto> validForAddChildPredicate(
      final Map<String, UnitreeNodeDto> nodeByLeafIdsFiltered) {
    return (parentId, child) -> child != null && !StringUtils.isBlank(parentId)
        && !parentId.equals(child.getId()) && nodeByLeafIdsFiltered.containsKey(parentId);
  }

  private Function<CachedBrandPriorityDto, GenArtDto> genArtConverter(
      SupportedAffiliate affiliate) {
    return item -> convertGenArtDto(item, affiliate);
  }

  private GenArtDto convertGenArtDto(CachedBrandPriorityDto brandPrio,
      SupportedAffiliate affiliate) {
    GenArtDto genArtDto = new GenArtDto();
    SagBeanUtils.copyProperties(brandPrio, genArtDto);
    final List<BrandDto> brandsByAffiliate = genArtDto.getBrands().stream()
        .filter(b -> !StringUtils.isBlank(b.getAffiliate())
            && StringUtils.equalsIgnoreCase(affiliate.getEsShortName(), b.getAffiliate()))
        .collect(Collectors.toList());
    genArtDto.setBrands(brandsByAffiliate);
    genArtDto.setSorts(Collections.emptyList());
    return genArtDto;
  }

  private static Comparator<UnitreeNodeDto> nodeDtoSortComparator() {
    return (node1, node2) -> {
      if (StringUtils.isBlank(node1.getSort()) || StringUtils.isBlank(node2.getSort())) {
        return -1;
      }
      return node1.getSort().compareTo(node2.getSort());
    };
  }

  private static Comparator<TileDto> tileDtoComparator() {
    return (t1, t2) -> {
      if (StringUtils.isBlank(t1.getTileSort()) || StringUtils.isBlank(t2.getTileSort())) {
        return -1;
      }
      return t1.getTileSort().compareTo(t2.getTileSort());
    };
  }

  /**
   * This function will update permission access link for tile external list
   *
   * @param tileExternals
   * @param shopAccessPermission
   */
  private void activeLink(List<TileExternalDto> tileExternals,
      Map<String, Boolean> shopAccessPermission) {
    tileExternals.stream()
        .filter(tile -> StringUtils.equals(tile.getTileLinkType(), PermissionUtils.TYPE_LINK_SHOP)
            && !StringUtils.isEmpty(tile.getTileLinkAttr()))
        .forEach(external -> external.setActiveLink(PermissionUtils.isAccess(shopAccessPermission,
            external.getTileLinkAttr().toUpperCase())));
  }
}
