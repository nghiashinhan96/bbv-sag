package com.sagag.services.haynespro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "part")
public class HaynesProPart {

  private String genartNumber;

  private String mandatoryPart;

  private String name;

  private String quantity;

}
