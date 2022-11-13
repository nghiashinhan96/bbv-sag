package com.sagag.services.gtmotive.dto.response.gtinterface;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GtmotivePartInfoResponse implements Serializable {

  private static final long serialVersionUID = 8191984369654935827L;
  private List<GtmotivePartInfoOperation> operations;
}
