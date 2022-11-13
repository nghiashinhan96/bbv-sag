package com.sagag.services.ax.domain;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the release note info from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxReleaseNoteResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -6008670738102000696L;

  private String releaseBuild;

  private String releaseVersion;

  private String releaseDate;

}
