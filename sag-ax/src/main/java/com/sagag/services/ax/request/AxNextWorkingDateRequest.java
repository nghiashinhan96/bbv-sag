package com.sagag.services.ax.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.Data;

/**
 * The class to build next working date request.
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxNextWorkingDateRequest implements Serializable {

  private static final long serialVersionUID = 9220450060032442288L;

  private final String branchId;

  private final String date;

  @Override
  public String toString() {
    return SagJSONUtil.convertObjectToJson(this);
  }

}
