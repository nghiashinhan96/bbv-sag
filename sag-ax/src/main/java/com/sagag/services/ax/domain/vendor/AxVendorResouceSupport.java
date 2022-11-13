package com.sagag.services.ax.domain.vendor;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AxVendorResouceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = 4789356557899569219L;

  private List<AxVendor> vendors;

}
