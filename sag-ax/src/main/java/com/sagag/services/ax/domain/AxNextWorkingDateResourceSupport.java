package com.sagag.services.ax.domain;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the next working date from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxNextWorkingDateResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -3531318508711714338L;

  private String nextWorkingDate;

}
