package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the SALUTATION database table.
 * 
 */
@Entity
@Table(name = "SALUTATION")
@NamedQuery(name = "Salutation.findAll", query = "SELECT s FROM Salutation s")
@Data
public class Salutation implements Serializable {

  private static final long serialVersionUID = 7911161156251816842L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
