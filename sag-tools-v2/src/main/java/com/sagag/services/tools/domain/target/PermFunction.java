package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "PERM_FUNCTION")
@NamedQuery(name = "PermFunction.findAll", query = "SELECT p FROM PermFunction p")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermFunction implements Serializable {

  private static final long serialVersionUID = -5300926358812275907L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "PERM_ID")
  @JsonBackReference
  private EshopPermission eshopPermission;

  @ManyToOne
  @JoinColumn(name = "FUNCTION_ID")
  @JsonManagedReference
  private EshopFunction eshopFunction;

}
