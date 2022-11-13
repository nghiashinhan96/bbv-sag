package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnOrderPositionDto implements Serializable {

  private static final long serialVersionUID = -5890713372206675400L;

  private String orderNr;

  private String orderUrl;

  private Boolean quarantineOrder;

}
