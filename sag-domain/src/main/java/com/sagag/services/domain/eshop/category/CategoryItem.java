package com.sagag.services.domain.eshop.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.common.domain.CriteriaDto;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "description", "sagCode", "children", "parentId", "genArt",
    "rootDescription", "sort", "open", "qcol", "qrow", "qflag", "qlevel", "qsort", "qshow",
    "qfold", "qfold_show", "classicCol" })
public class CategoryItem implements Serializable {

  private static final long serialVersionUID = -1148450302434835031L;

  private String parentId;

  private List<GenArtDto> genArts;

  private List<CategoryItem> children;

  private String id;

  private String description;

  private String sagCode;

  private String rootDescription;

  private String sort;

  private String open;

  private String idProductBrand;

  private String service;

  private Integer qCol;

  private Integer qRow;

  private Integer qFlag;

  private Integer qLevel;

  private Integer qSort;

  private boolean isQShow;

  private boolean isQFold;

  private boolean isQFoldShow;

  private String classicCol;

  private boolean ignoredOpen;

  @JsonProperty("check")
  private boolean check;

  private String link;

  @JsonProperty("isOilCate")
  private boolean isOilCate;

  public CategoryItem() {
    this.children = new ArrayList<>();
  }

  /**
   * Constructs a category item from id.
   *
   * @param id the id of the item.
   */
  public CategoryItem(final String id) {
    this.children = new ArrayList<>();
    this.id = id;
  }

  /**
   * Adds a child category item to an parent item.
   *
   * @param child the child item.
   */
  public void addChildItem(final CategoryItem child) {
    if (!this.children.contains(child)) {
      this.children.add(child);
    }
  }

  /**
   * Checks if the category item has childen or not.
   *
   * @return <code>true</code> if the category has children. <code>false</code> otherwise.
   */
  public boolean hasChildren() {
    return !CollectionUtils.isEmpty(this.children);
  }

  /**
   * Returns all the generic article ids of the category.
   *
   * @return all generic article ids.
   */
  public String getBelongedGaIds() {
    if (!hasGaIds()) {
      return StringUtils.EMPTY;
    }
    return genArts.stream().map(GenArtDto::getGaid).collect(Collectors.joining(","));
  }

  /**
   * Returns all criterias from all generic articles.
   *
   * @return all criterias for this category from all generic articles
   */
  public List<CriteriaDto> getCriterias() {
    if (!hasGaIds()) {
      return Collections.emptyList();
    }
    return genArts.stream().flatMap(ga -> ga.getCriteria().stream()).collect(Collectors.toList());
  }

  @JsonIgnore
  public boolean hasGaIds() {
    return CollectionUtils.isNotEmpty(genArts);
  }

  @JsonIgnore
  public boolean hasLink() {
    return !StringUtils.isBlank(this.link);
  }

  @JsonIgnore
  public boolean isValidForSearching() {
    return hasGaIds() || hasLink();
  }

  @JsonIgnore
  public int getDefaultQRow() {
    return qRow == null ? Integer.MAX_VALUE : qRow.intValue();
  }

  @JsonIgnore
  public int getDefaultQSort() {
    return qSort == null ? Integer.MAX_VALUE : qSort.intValue();
  }

  /**
   * Returns all filters from all generic articles.
   *
   * @return all filters for this category from all generic articles
   */
  public List<FilterBarDto> getFilters() {
    if (!hasGaIds()) {
      return Collections.emptyList();
    }
    return genArts.stream().flatMap(ga -> CollectionUtils.emptyIfNull(ga.getFilters()).stream())
        .collect(Collectors.toList());
  }
}
