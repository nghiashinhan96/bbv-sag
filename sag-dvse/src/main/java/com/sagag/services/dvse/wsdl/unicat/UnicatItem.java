package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnicatItem", propOrder = {
    "articleId",
    "requestedQuantity"
})
public class UnicatItem {

    @XmlElement(name = "ArticleId")
    protected String articleId;

    @XmlElement(name = "RequestedQuantity")
    protected int requestedQuantity;

    public String getArticleId() {
      return articleId;
    }

    public void setArticleId(String articleId) {
      this.articleId = articleId;
    }

    public int getRequestedQuantity() {
      return requestedQuantity;
    }

    public void setRequestedQuantity(int requestedQuantity) {
      this.requestedQuantity = requestedQuantity;
    }

}
