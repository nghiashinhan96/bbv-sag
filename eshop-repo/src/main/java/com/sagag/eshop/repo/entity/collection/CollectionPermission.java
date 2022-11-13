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

@Table(name = "COLLECTION_PERMISSION")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionPermission implements Serializable {

  private static final long serialVersionUID = -4861518619475973729L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ESHOP_PERMISSION_ID", nullable = false)
  private int eshopPermissionId;

  @Column(name = "COLLECTION_ID", nullable = false)
  private int collectionId;

}
