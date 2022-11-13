package com.sagag.services.haynespro.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "jobs")
public class HaynesProJobs {

  private HaynesProJob[] job;

}
