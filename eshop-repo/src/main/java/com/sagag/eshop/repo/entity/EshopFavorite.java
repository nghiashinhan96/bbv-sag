package com.sagag.eshop.repo.entity;

import com.sagag.eshop.repo.enums.EshopFavoriteType;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity of ESHOP_FAVORITE table.
 *
 */
@Data
@Entity
@Table(name = "ESHOP_FAVORITE")
public class EshopFavorite implements Serializable {

  private static final long serialVersionUID = 1444460996877342827L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "COMMENT")
  private String comment;

  @Column(name = "TYPE")
  @Enumerated(EnumType.STRING)
  private EshopFavoriteType type;

  @Column(name = "VIN_ID")
  private String vinId;

  @Column(name = "VEHICLE_ID")
  private String vehicleId;

  @Column(name = "ARTICLE_ID")
  private String articleId;

  @Column(name = "TREE_ID")
  private String treeId;

  @Column(name = "LEAF_ID")
  private String leafId;

  @Column(name = "GA_ID")
  private String gaId;

  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "LAST_UPDATE")
  private Date lastUpdate;
}
