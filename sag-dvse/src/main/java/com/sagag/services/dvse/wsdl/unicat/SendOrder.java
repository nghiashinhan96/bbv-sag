package com.sagag.services.dvse.wsdl.unicat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "",
propOrder = {
    "articleId",
    "requestedQuantity",
    "sid"
    })
@XmlRootElement(name = "SendOrder")
public class SendOrder {

  @XmlElement(name = "ArticleId")
  private String articleId;

  @XmlElement(name = "RequestedQuantity")
  private int requestedQuantity;

  @XmlElement(name = "Sid")
  private String sid;

  public String getArticleId() {
    return articleId;
  }

  public void setArticleId(String articleId) {
    this.articleId = articleId;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public int getRequestedQuantity() {
    return requestedQuantity;
  }

  public void setRequestedQuantity(int requestedQuantity) {
    this.requestedQuantity = requestedQuantity;
  }


}
