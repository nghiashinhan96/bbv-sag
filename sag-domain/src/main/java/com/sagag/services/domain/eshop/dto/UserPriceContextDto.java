package com.sagag.services.domain.eshop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPriceContextDto implements Serializable {

  private static final long serialVersionUID = -3886099634164436750L;

  private boolean currentStateNetPriceView;

}
