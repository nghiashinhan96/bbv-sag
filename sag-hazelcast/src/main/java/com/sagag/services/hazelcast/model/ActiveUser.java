package com.sagag.services.hazelcast.model;

import com.sagag.services.common.enums.LoginMode;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class ActiveUser implements Serializable {

  private static final long serialVersionUID = -191724591701633850L;

  private long id;

  private long customerNr;

  private long loginTime;

  private LoginMode loginMode;

}