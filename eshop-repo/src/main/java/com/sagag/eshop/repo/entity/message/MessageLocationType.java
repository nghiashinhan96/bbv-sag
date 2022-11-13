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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_LOCATION_TYPE.
 */
@Entity
@Table(name = "MESSAGE_LOCATION_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLocationType implements Serializable {

  private static final long serialVersionUID = 5188409021095833714L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String locationType;

  private String description;

  @OneToMany(mappedBy = "messageLocationType")
  @JsonBackReference
  private List<MessageLocationTypeRoleType> messageLocationTypeRoleTypes;
}
