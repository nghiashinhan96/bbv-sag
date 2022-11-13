package com.sagag.services.tools.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class TargetBaseObject implements Serializable {

  private static final long serialVersionUID = 2527282667184824672L;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "VERSION")
  private Integer version;
}
