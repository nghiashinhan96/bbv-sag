package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "ESHOP_FUNCTION")
@Entity
@NamedQuery(name = "EshopFunction.findAll", query = "SELECT e FROM EshopFunction e")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@ToString(of = { "id", "description", "functionName" })
public class EshopFunction implements Serializable {

  private static final long serialVersionUID = 7333900380439068440L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private String relativeUrl;
  
  @Column
  private String functionName;

  @OneToMany(mappedBy = "eshopFunction")
  @JsonBackReference
  private List<PermFunction> permFunctions;

}
