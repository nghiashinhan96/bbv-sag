package com.sagag.eshop.repo.entity.collection;

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
import javax.persistence.Table;

@Table(name = "ORG_COLLECTION_SETTINGS")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationCollectionsSettings implements Serializable {

  private static final long serialVersionUID = 7069138936671486440L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private Integer collectionId;

  @Column(name = "SETTING_KEY", nullable = false)
  private String settingKey;

  @Column(name = "SETTING_VALUE", nullable = false)
  private String settingValue;

}
