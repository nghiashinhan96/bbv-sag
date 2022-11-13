package com.sagag.services.tools.domain.target;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "MIGRATION_ORGANISATION")
public class MigrationOrganisation implements Serializable {

  private static final long serialVersionUID = 5374541200236169781L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "COMPANY_NAME")
  private String companyName;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "IS_MIGRATED", nullable = false)
  private boolean isMigrated;
}
