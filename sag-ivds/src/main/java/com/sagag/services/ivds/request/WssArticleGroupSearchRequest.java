package com.sagag.services.ivds.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.domain.PageRequest;

import java.io.Serializable;

/**
 * Class to define the WSS Article Group search request data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WssArticleGroupSearchRequest implements Serializable {

  private static final long serialVersionUID = -5274712902992269022L;

  private String articleGroup;

  private String articleGroupDesc;

  @JsonIgnore
  private PageRequest pageRequest;

}
