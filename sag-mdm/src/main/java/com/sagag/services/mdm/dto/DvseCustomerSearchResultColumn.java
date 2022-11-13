package com.sagag.services.mdm.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CustomerSearchResultColumn_V1")
public class DvseCustomerSearchResultColumn {

  private String columnID;
  private String description;
  private String sortColumn;
  private String sortNr;

  @XmlElement(name = "ColumnID")
  public String getColumnID() {
    return columnID;
  }

  public void setColumnID(String columnID) {
    this.columnID = columnID;
  }

  @XmlElement(name = "Description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlElement(name = "SortColumn")
  public String getSortColumn() {
    return sortColumn;
  }

  public void setSortColumn(String sortColumn) {
    this.sortColumn = sortColumn;
  }

  @XmlElement(name = "SortNr")
  public String getSortNr() {
    return sortNr;
  }

  public void setSortNr(String sortNr) {
    this.sortNr = sortNr;
  }


}
