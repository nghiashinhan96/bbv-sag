package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GtmotiveOperation implements Serializable {

  private static final long serialVersionUID = 2252678689520633218L;
  private String shortNumber;
  private GtmotiveSimpleAttribute description;
  private String reference;
  private String type;
  private GtmotiveSimpleAttribute action;
  private String operationState;
  private String preventiveType;
  private String partNumber;
  private String materialType;
  private OperationPrecalculation precalculation;
  private List<GtmotiveOperation> childs; // not consider this time
  private boolean viewPrecalculation;
  private Integer depreciation;
  private boolean multiReference;

}
