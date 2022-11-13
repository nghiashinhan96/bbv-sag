package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_RELEASE database table.
 * 
 */
@Entity
@Table(name = "ESHOP_RELEASE")
@NamedQuery(name = "EshopRelease.findAll", query = "SELECT e FROM EshopRelease e")
@Data
public class EshopRelease implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "RELEASE_BRANCH")
  private String releaseBranch;

  @Column(name = "RELEASE_BUILD")
  private String releaseBuild;

  @Column(name = "RELEASE_DATE")
  private Date releaseDate;

  @Column(name = "RELEASE_VERSION")
  private String releaseVersion;

}
