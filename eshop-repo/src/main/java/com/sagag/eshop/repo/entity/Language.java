package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "Language.findAll", query = "SELECT o FROM Language o")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "LANGUAGES")
public class Language implements Serializable {

  private static final long serialVersionUID = 674474811659697218L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String langcode;
  private String langiso;
  private String description;
  private Integer tecDoc;
}
