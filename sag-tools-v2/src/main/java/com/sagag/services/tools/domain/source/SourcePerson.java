package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "SHOP.PERSON")
public class SourcePerson implements Serializable {

  private static final long serialVersionUID = -7934716822484544875L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "FIRSTNAME")
  private String firstName;

  @Column(name = "LASTNAME")
  private String lastName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "LANGISO")
  private String langIso;

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

}
