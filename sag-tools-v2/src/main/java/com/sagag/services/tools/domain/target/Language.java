package com.sagag.services.tools.domain.target;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "LANGUAGES")
public class Language implements Serializable {

  private static final long serialVersionUID = 7216388562387063466L;

  @Id
  @Column(name = "ID")
  private Integer id;

  @Column(name = "LANGCODE")
  private String langCode;

  @Column(name = "LANGISO")
  private String langiso;

  @Column(name = "DESCRIPTION")
  private String description;
}
