package com.sagag.services.tools.domain.mdm;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetCustomerSearchResults_V1")
public class DvseGetCustomerSearchResultRequest extends DvseRequest {

  private String traderId;

  private String searchId;

  private String searchString;

  @XmlElement(name = "TraderID")
  public String getTraderId() {
    return traderId;
  }

  public void setTraderId(String traderId) {
    this.traderId = traderId;
  }

  @XmlElement(name = "SearchID")
  public String getSearchId() {
    return searchId;
  }

  public void setSearchId(String searchId) {
    this.searchId = searchId;
  }

  @XmlElement(name = "SearchString")
  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }


}
