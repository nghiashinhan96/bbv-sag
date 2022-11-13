package com.sagag.services.service.request;

import java.io.Serializable;

import lombok.Data;

/**
 * Request body class for JS error log
 */
@Data
public class JSLogRequestBody implements Serializable {

  private static final long serialVersionUID = 4096856765429395834L;

  private String message;

  private String url;

}
