package com.sagag.eshop.service.dto.offer;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupedOfferPositionKey implements Serializable {

  private static final long serialVersionUID = -2777557267691632844L;

  private final String vehicleBomDescription;

  private final String catalogPath;
}
