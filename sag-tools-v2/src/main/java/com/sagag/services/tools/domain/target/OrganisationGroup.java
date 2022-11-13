package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@Table(name = "ORGANISATION_GROUP")
@NamedQuery(name = "OrganisationGroup.findAll", query = "SELECT o FROM OrganisationGroup o")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(of = { "id" })
public class OrganisationGroup implements Serializable {

  private static final long serialVersionUID = 4128752069952450075L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "GROUP_ID")
  @JsonBackReference
  private EshopGroup eshopGroup;

  @ManyToOne
  @JoinColumn(name = "ORGANISATION_ID")
  @JsonManagedReference
  private Organisation organisation;

}
