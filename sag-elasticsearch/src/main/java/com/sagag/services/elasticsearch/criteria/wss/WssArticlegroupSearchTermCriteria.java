package com.sagag.services.elasticsearch.criteria.wss;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WssArticlegroupSearchTermCriteria {

  private String articleGroup;

  private String articleGroupDesc;

  private String leafId;

}
