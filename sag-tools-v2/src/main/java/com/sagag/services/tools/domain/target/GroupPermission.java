package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Customized class for user groups permissions.
 */
@Entity
@Table(name = "GROUP_PERMISSION")
@NamedQuery(name = "GroupPermission.findAll", query = "SELECT gp FROM GroupPermission gp")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermission implements Serializable {

  private static final long serialVersionUID = -7876239944847387381L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "PERM_ID")
  @JsonBackReference
  private EshopPermission eshopPermission;

  @ManyToOne
  @JoinColumn(name = "GROUP_ID")
  @JsonBackReference
  private EshopGroup eshopGroup;

  @Column(name = "ALLOWED")
  private boolean allowed;

  @Transient
  private int permId; // this field will not map to table, only on memory

  /**
   * Constructs the Group permission with permission id and the accessible flag.
   * 
   * @param id the group permission id
   * @param permId the permission id
   * @param allowed the accessible flag to the function
   */
  public GroupPermission(int id, int permId, boolean allowed) {
    this.id = id;
    this.permId = permId;
    this.allowed = allowed;
  }
}
