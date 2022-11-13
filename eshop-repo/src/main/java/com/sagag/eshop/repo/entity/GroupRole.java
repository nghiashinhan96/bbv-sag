package com.sagag.eshop.repo.entity;

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
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "GROUP_ROLE")
@NamedQuery(name = "GroupRole.findAll", query = "SELECT g FROM GroupRole g")
@ToString(of = { "id", "eshopRole" })
public class GroupRole implements Serializable {

  private static final long serialVersionUID = -5121058735241806922L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;


  @ManyToOne
  @JoinColumn(name = "GROUP_ID")
  @JsonBackReference
  private EshopGroup eshopGroup;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID")
  @JsonManagedReference
  private EshopRole eshopRole;

}
