package com.sagag.services.ivds.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sagag.services.common.domain.CriteriaDto;

import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@Setter
public class WSPCriteriaDto implements Serializable {

  private static final long serialVersionUID = 690991268300460639L;

  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<Integer> genArts;
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<Integer> brandIds;
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<String> articleGroups;
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private List<String> articleIds;

  private List<CriteriaDto> criterion;
  
  private String treeId;
  
  private String leafId;
}
