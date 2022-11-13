package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "V_COLLECTION_SEARCH")
public class VCollectionSearch {

  @Id
  private String collectionShortName;

  private String collectionName;

  private String affiliate;

  private String customerNr;

}
