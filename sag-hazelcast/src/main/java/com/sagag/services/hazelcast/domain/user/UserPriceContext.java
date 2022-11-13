package com.sagag.services.hazelcast.domain.user;

import lombok.Data;

import java.io.Serializable;

@Data
public final class UserPriceContext implements Serializable {

  /**
   * User context regarding to User settings which are affect to whole application
   */
  private static final long serialVersionUID = 910952226491854265L;

  private boolean currentStateNetPriceView;
}
