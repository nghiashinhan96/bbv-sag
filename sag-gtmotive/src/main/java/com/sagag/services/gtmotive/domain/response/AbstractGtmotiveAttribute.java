package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AbstractGtmotiveAttribute implements Serializable {

  private static final long serialVersionUID = 353371374277053216L;

  private boolean locked;

}
