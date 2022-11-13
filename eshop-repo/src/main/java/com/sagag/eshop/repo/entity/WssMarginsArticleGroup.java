package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "WssMarginsArticleGroup.findAll", query = "SELECT o FROM WssMarginsArticleGroup o")
@Data
@Builder
@Table(name = "WSS_MARGINS_ARTICLE_GROUP")
@NoArgsConstructor
@AllArgsConstructor
public class WssMarginsArticleGroup implements Serializable {

  private static final long serialVersionUID = 280425649995134357L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "ORG_ID", nullable = false)
  private Integer orgId;

  @Column(name = "SAG_ART_GROUP_ID")
  private String sagArtGroup;

  @Column(name = "PARENT_ID")
  private Integer parentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID", insertable = false,
      updatable = false)
  @JsonIgnore
  @Getter(value = AccessLevel.NONE)
  @Setter(value = AccessLevel.NONE)
  private WssMarginsArticleGroup parent;

  @OneToMany(mappedBy = "parent")
  @JsonBackReference
  private List<WssMarginsArticleGroup> wssMarginsArticleGroup;

  @Column(name = "GROUP_LEVEL")
  private Integer groupLevel;

  @Column(name = "SAG_ART_GROUP_TEXT")
  private String sagArticleGroupDesc;

  @Column(name = "CUSTOMER_ART_GROUP_ID")
  private String customArticleGroup;

  @Column(name = "CUSTOMER_ART_GROUP_TEXT")
  private String customArticleGroupDesc;

  @Column(name = "MARGIN_1")
  private Double margin1;

  @Column(name = "MARGIN_2")
  private Double margin2;

  @Column(name = "MARGIN_3")
  private Double margin3;

  @Column(name = "MARGIN_4")
  private Double margin4;

  @Column(name = "MARGIN_5")
  private Double margin5;

  @Column(name = "MARGIN_6")
  private Double margin6;

  @Column(name = "MARGIN_7")
  private Double margin7;

  @Column(name = "IS_MAPPED", nullable = false)
  private boolean isMapped;

  @Column(name = "LEAF_ID")
  private String leafId;

  @Column(name = "PARENT_LEAF_ID")
  private String parentLeafId;

  @Column(name = "IS_DEFAULT", nullable = false)
  private boolean isDefault;
}
