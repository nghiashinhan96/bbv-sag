package com.sagag.services.gtmotive.dto.request.gtinterface;

import lombok.Data;

import java.io.Serializable;

@Data
public class GtmotiveVinSearchErrorLoggingRequest implements Serializable {

  private static final long serialVersionUID = 7866195330596097541L;

  private String vin;

  private String cupi;

  private String location;

  private String umc;

  private String returnedData;

  private String type;

  private String oeNr;
}
