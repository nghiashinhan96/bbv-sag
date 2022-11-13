package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Item", propOrder = {
    "articleId",
    "prices",
    "quantities",
    "alternativeItems",
    "availState"
})
public class Item {

    @XmlElement(name = "ArticleId")
    protected String articleId;
    @XmlElement(name = "Prices")
    protected ArrayOfPrice prices;
    @XmlElement(name = "Quantities")
    protected ArrayOfQuantity quantities;
    @XmlElement(name = "AlternativeItems")
    protected ArrayOfItem alternativeItems;
    @XmlElement(name = "AvailState")
    protected AvailableState availState;
    public ArrayOfPrice getPrices() {
      return prices;
    }
    public void setPrices(ArrayOfPrice prices) {
      this.prices = prices;
    }
    public ArrayOfQuantity getQuantities() {
      return quantities;
    }
    public void setQuantities(ArrayOfQuantity quantities) {
      this.quantities = quantities;
    }
    public ArrayOfItem getAlternativeItems() {
      return alternativeItems;
    }
    public void setAlternativeItems(ArrayOfItem alternativeItems) {
      this.alternativeItems = alternativeItems;
    }

    public AvailableState getAvailState() {
      return availState;
    }
    public void setAvailState(AvailableState availState) {
      this.availState = availState;
    }
    public String getArticleId() {
      return articleId;
    }
    public void setArticleId(String articleId) {
      this.articleId = articleId;
    }

}
