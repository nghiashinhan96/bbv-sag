package com.sagag.services.ax.exception;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo implements Serializable {

  private static final long serialVersionUID = -2781092546293437630L;

  private String errorOrigin;

  private String errorMessage;

  public boolean hasData() {
    return !StringUtils.isBlank(this.errorOrigin) && !StringUtils.isBlank(this.errorMessage);
  }
}
