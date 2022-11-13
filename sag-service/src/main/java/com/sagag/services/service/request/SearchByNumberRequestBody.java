package com.sagag.services.service.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for searching the articles by number.
 */
@Data
public class SearchByNumberRequestBody implements Serializable {

  private static final long serialVersionUID = -1828132909578982718L;

  private int amountNumber;
  private int offset;
  private int size;
  private boolean deepLink;
}
