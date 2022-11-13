package com.sagag.services.haynespro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "job")
public class HaynesProJob {

  private String id;

  private String name;

  private String type;

  private String awNumber;

  private String oeCode;

  private String time;

  private String labourRate;
}
