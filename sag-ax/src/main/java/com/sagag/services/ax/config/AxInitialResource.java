package com.sagag.services.ax.config;

import lombok.Data;

/**
 * Ax initial resources.
 */
@Data
public class AxInitialResource {

  private String accessToken;

  private boolean needRetry;
}
