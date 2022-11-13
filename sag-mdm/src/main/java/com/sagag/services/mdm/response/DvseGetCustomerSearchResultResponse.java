package com.sagag.services.mdm.response;

import com.sagag.services.mdm.dto.DvseCustomerSearchResult;
import com.sagag.services.mdm.dto.DvseCustomerSearchResultColumn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ResponseDataOfGetCustomerSearchResults_V1")
public class DvseGetCustomerSearchResultResponse {

  @XmlElement(name = "Data")
  private GetCustomerSearchResultData data;

  public List<DvseCustomerSearchResult> getCustomerSearchResults(){
    return data.getDvseCustomerSearchResults();
  }
}

@XmlRootElement(name = "Data")
class GetCustomerSearchResultData {

  private List<DvseCustomerSearchResultColumn> customerSearchResultColumns;
  private List<DvseCustomerSearchResult> customerSearchResults;

  @XmlElementWrapper(name = "SearchResults")
  @XmlElement(name = "CustomerSearchResult_V1")
  public List<DvseCustomerSearchResult> getDvseCustomerSearchResults() {
    if (customerSearchResults == null) {
      customerSearchResults = new ArrayList<>();
    }
    return customerSearchResults;
  }

  public void setCustomerSearchResults(List<DvseCustomerSearchResult> customerSearchResults) {
    this.customerSearchResults = customerSearchResults;
  }

  @XmlElementWrapper(name = "ResultColumnsArray")
  @XmlElement(name = "CustomerSearchResultColumn_V1")
  public List<DvseCustomerSearchResultColumn> getCustomerSearchResultColumns() {
    if (customerSearchResultColumns == null) {
      customerSearchResultColumns = new ArrayList<>();
    }
    return customerSearchResultColumns;
  }

  public void setCustomerSearchResultColumns(List<DvseCustomerSearchResultColumn> customerSearchResultColumns) {
    this.customerSearchResultColumns = customerSearchResultColumns;
  }

}
