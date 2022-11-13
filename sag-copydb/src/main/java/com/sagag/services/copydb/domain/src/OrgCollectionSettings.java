package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ORG_COLLECTION_SETTINGS database table.
 * 
 */
@Entity
@Table(name = "ORG_COLLECTION_SETTINGS")
@NamedQuery(name = "OrgCollectionSettings.findAll", query = "SELECT o FROM OrgCollectionSettings o")
@Data
public class OrgCollectionSettings implements Serializable {

  private static final long serialVersionUID = 7069138936671486440L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "COLLECTION_ID")
  private Integer collectionId;

  @Column(name = "SETTING_KEY", nullable = false)
  private String settingKey;

  @Column(name = "SETTING_VALUE", nullable = false)
  private String settingValue;

}
