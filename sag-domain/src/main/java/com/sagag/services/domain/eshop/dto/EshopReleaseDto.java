package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EshopReleaseDto implements Serializable {

  private static final long serialVersionUID = 3243709764127533768L;

  private Integer id;

  private String releaseBuild;

  private String releaseVersion;

  private Date releaseDate;

  private String releaseBranch;

}
