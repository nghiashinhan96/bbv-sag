package com.sagag.services.service.request;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Request body class for freetext search.
 */
@Data
public class FreetextSearchRequestBody implements Serializable {

  private static final long serialVersionUID = -5297736258119317479L;

  private String[] options;
  private String keyword = StringUtils.EMPTY;
  private boolean fullRequest;
  private int size = 10;
  private int page = 0;
  private boolean genericSearch = false;

}
