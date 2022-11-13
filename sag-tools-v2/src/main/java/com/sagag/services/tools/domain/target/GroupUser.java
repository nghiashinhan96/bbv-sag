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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "GROUP_USER")
@NamedQueries(value = {
    @NamedQuery(name = "GroupUser.findAll", query = "SELECT g FROM GroupUser g"),
    @NamedQuery(name = "GroupUser.findByEshopUser",
        query = "select g from GroupUser g where g.eshopUser= :eshopUser")
    })
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(of = { "id" })
public class GroupUser implements Serializable {

  private static final long serialVersionUID = 8587441940925790579L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "GROUP_ID")
  @JsonManagedReference
  private EshopGroup eshopGroup;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  @JsonBackReference
  private EshopUser eshopUser;

}
