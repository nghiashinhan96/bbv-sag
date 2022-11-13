package com.sagag.services.haynespro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "parts")
public class HaynesProParts {

  private HaynesProPart[] part;

}
