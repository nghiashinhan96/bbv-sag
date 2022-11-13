package com.sagag.services.tools.domain.target;

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
@NamedQuery(name = "Salutation.findAll", query = "SELECT o FROM Salutation o")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "SALUTATION")
public class Salutation implements Serializable {

  private static final long serialVersionUID = 7996141635809788015L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String code;

  private String description;

  private String type;
}
