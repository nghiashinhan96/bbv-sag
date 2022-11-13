package com.sagag.services.ax.domain.returnorder;

import lombok.Data;

import java.io.Serializable;

@Data
public class AxReturnOrderPosition implements Serializable {

  private static final long serialVersionUID = -5890713372206675400L;

  private String orderNr;

  private String orderUrl;

  private Boolean quarantineOrder;

}

