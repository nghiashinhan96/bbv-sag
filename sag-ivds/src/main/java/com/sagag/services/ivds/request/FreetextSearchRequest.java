package com.sagag.services.ivds.request;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.elasticsearch.criteria.VehicleFilteringTerms;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Class to define the freetext search request data.
 */
@Data
@Builder
public class FreetextSearchRequest implements Serializable {

  private static final long serialVersionUID = -7400604556319384428L;

  private String text;

  private UserInfo user;

  private PageRequest pageRequest;

  /** the flag indicating whether to get all information for articles from ERP. */
  private boolean isFullRequest;

  private Optional<VehicleFilteringTerms> fitering;

  private List<String> searchOptions;

}
