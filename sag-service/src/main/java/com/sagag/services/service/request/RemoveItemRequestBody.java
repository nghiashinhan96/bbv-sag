package com.sagag.services.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Request body class for removing article from shopping basket.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveItemRequestBody implements Serializable {

  private static final long serialVersionUID = -1466594457703259148L;

  private String[] cartKeys;

  private Boolean reloadAvail;
}
