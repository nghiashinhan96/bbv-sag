package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "LOGIN_ROLEASSIGNMENT", schema = "SHOP")
public class SourceLoginRoleAssignment implements Serializable {

  private static final long serialVersionUID = -7541448914316341344L;

  @EmbeddedId
  @Column(name = "ROLE")
  private String role;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "ORGANISATION_ID")
  private Long orgId;

  @Column(name = "LOGIN_ID")
  private Long loginId;
}
