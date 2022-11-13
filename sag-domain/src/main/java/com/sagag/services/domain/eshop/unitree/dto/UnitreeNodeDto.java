package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.eshop.category.GenArtDto;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Data
@JsonPropertyOrder({ "id", "parentId", "tiles", "children", "open", "brandPrio", "filterDefault",
    "filterBar", "nodeExternalServiceAttribute", "nodeName", "filterCaid", "nodeExternalType",
    "include", "exclude", "filterSort", "sort", })
public class UnitreeNodeDto implements Serializable {

  private static final long serialVersionUID = 3885645220333825376L;

  private static final int UNITREE_BRAND_SORTING_ENABLED = 1;

  private String id;

  private String parentId;

  private PriorityQueue<TileDto> tiles;

  private PriorityQueue<UnitreeNodeDto> children;

  private String open;

  private Integer brandPrio;

  private String filterDefault;

  private String filterBar;

  private String nodeExternalServiceAttribute;

  private String nodeName;

  private String filterCaid;

  private String nodeExternalType;

  private IncludeDto include;

  private ExcludeDto exclude;

  private String filterSort;

  private String sort;

  private List<GenArtDto> genArts;

  private boolean activeLink;

  private List<FilterDto> filters;

  public UnitreeNodeDto() {
    this.children = new PriorityQueue<>(nodeDtoComparator());
  }

  /**
   * Constructs a category item from id.
   *
   * @param id the id of the item.
   */
  public UnitreeNodeDto(final String id) {
    this.children = new PriorityQueue<>(nodeDtoComparator());
    this.id = id;
  }

  /**
   * Adds a child unitree node to an parent.
   *
   * @param child the child node.
   */
  public void addChildNode(final UnitreeNodeDto child) {
    if (!this.children.contains(child)) {
      this.children.add(child);
    }
  }

  /**
   * Checks if the unitree item has childen or not.
   *
   * @return <code>true</code> if the unitree node has children. <code>false</code> otherwise.
   */
  public boolean hasChildren() {
    return !CollectionUtils.isEmpty(this.children);
  }

  private static Comparator<UnitreeNodeDto> nodeDtoComparator() {
    return (node1, node2) -> {
      if (!StringUtils.isNumeric(node1.getId()) && !StringUtils.isNumeric(node2.getId())) {
        return -1;
      }

      return NumberUtils.createInteger(node1.getId())
          .compareTo(NumberUtils.createInteger(node2.getId()));
    };
  }

  public boolean isBrandSortingEnabled() {
    return brandPrio != null && brandPrio.intValue() == UNITREE_BRAND_SORTING_ENABLED;
  }
}
