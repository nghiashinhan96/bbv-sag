package com.sagag.services.tools.domain.source;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SHOP.ORGANISATION")
public class SourceOrganisation implements Serializable {

  private static final long serialVersionUID = -1192548006964131080L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "ERPNUMBER")
  private Long erpNumber;

  @Column(name = "ERPINSTANCE")
  private String erpInstance;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "ORGANISATIONID")
  private String organisationId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DISTRIBUTIONSET")
  private Long distributionSet;

  @Column(name = "UC_ID")
  private Long userCreatedId;

  @Column(name = "DC")
  private Date dateCreated;

  @Column(name = "UM_ID")
  private Long userModifiedId;

  @Column(name = "DM")
  private Date dateModified;

  @Column(name = "VERSION")
  private Integer version;

  @Column(name = "TECSTATE")
  private String tecstate;

  @Column(name = "COUNTRYISO")
  private String countryIso;

  public SourceOrganisation(Long id, String name) {
    this.id = id;
    this.name = name;
  }

}
