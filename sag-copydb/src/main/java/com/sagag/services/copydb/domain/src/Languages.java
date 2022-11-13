package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the LANGUAGES database table.
 * 
 */
@Entity
@Table(name = "LANGUAGES")
@NamedQuery(name = "Languages.findAll", query = "SELECT l FROM Languages l")
@Data
public class Languages implements Serializable {

  private static final long serialVersionUID = 4239976829578056479L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "LANGCODE")
  private String langcode;

  @Column(name = "LANGISO")
  private String langiso;

}
