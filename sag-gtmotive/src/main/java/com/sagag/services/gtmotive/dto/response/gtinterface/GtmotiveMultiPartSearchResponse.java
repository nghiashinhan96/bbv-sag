package com.sagag.services.gtmotive.dto.response.gtinterface;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GtmotiveMultiPartSearchResponse implements Serializable {

  private static final long serialVersionUID = -1794393738088353500L;

  private List<GtmotivePartInfoOperation> operations;
}
