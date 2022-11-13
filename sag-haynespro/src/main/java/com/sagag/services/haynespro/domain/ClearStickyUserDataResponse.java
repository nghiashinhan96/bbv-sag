package com.sagag.services.haynespro.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clearStickyUserDataResponse", propOrder = { "clearStickyUserDataReturn" })
@Data
public class ClearStickyUserDataResponse {

  protected boolean clearStickyUserDataReturn;

}
