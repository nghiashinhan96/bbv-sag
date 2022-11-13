package com.sagag.services.haynespro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "vehicle")
public class HaynesProVehicle {

  private String id;

  private String name;

  private String ktypnr;

  private String motnrs;

}
