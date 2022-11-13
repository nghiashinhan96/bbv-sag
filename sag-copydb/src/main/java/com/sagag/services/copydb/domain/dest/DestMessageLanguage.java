package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_LANGUAGE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_LANGUAGE")
@NamedQuery(name = "DestMessageLanguage.findAll", query = "SELECT m FROM DestMessageLanguage m")
@Data
public class DestMessageLanguage implements Serializable {

  private static final long serialVersionUID = 2664935006579531380L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "LANG_ISO")
  private String langIso;

  @Column(name = "MESSAGE_ID")
  private Long messageId;

}
