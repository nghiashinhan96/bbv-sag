package com.sagag.eshop.repo.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_ROLE_TYPE.
 */
@Entity
@Table(name = "MESSAGE_ROLE_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRoleType implements Serializable {

  private static final long serialVersionUID = 7686150628131557339L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String roleType;

  private String description;

  @OneToMany(mappedBy = "messageRoleType")
  private List<MessageAccessRight> messageAccessRights;
}
