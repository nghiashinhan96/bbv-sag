package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "WssCustomerArticleGroup.findAll",
    query = "SELECT o FROM WssCustomerArticleGroup o")
@Data
@Builder
@Table(name = "WSS_CUSTOMER_ARTICLE_GROUP")
@NoArgsConstructor
@AllArgsConstructor
public class WssCustomerArticleGroup implements Serializable {

  private static final long serialVersionUID = 6689859130915610470L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "WSS_ARTICLE_GROUP_ID", nullable = false)
  private Integer wssArticleGroupId;

  @Column(name = "LANGUAGE")
  private String language;

  @Column(name = "CUSTOMER_ART_GROUP_TEXT")
  private String customerArticleGroupText;

  @ManyToOne
  @JoinColumn(name = "WSS_ARTICLE_GROUP_ID", updatable = false, insertable = false)
  @JsonBackReference
  private WssMarginsArticleGroup wssMarginsArticleGroup;
}
