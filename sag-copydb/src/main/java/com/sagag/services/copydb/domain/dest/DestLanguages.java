package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestLanguages.findAll", query = "SELECT l FROM DestLanguages l")
@Data
public class DestLanguages implements Serializable {

  private static final long serialVersionUID = -4021943992826913369L;

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
