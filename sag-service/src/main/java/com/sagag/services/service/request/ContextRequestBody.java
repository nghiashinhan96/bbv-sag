package com.sagag.services.service.request;

import com.sagag.services.domain.eshop.dto.ContextDto;

import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for the application context.
 */
@Data
public class ContextRequestBody implements Serializable {

  private static final long serialVersionUID = 6124932967071517379L;

  private ContextDto contextDto;
  private String scope;
  private String contextId;
}
