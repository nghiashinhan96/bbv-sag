package com.sagag.eshop.repo.entity.message;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_ACCESS_RIGHT.
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAccessRight implements Serializable {

  private static final long serialVersionUID = 7924539073061309427L;

  @Id
  private int id;

  @Column(nullable = false)
  private String userGroup;

  @Column(nullable = false)
  private String userGroupKey;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ROLE_TYPE_ID", nullable = false)
  private MessageRoleType messageRoleType;

  private String description;

  @OneToMany(mappedBy = "messageAccessRight")
  @JsonBackReference
  private List<MessageAccessRightRole> messageAccessRightRoles;

  @OneToMany(mappedBy = "messageAccessRight")
  @JsonBackReference
  private List<MessageAccessRightArea> messageAccessRightAreas;

}
