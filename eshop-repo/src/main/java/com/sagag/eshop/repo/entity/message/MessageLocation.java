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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_LOCATION.
 */
@Entity
@Table(name = "MESSAGE_LOCATION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLocation implements Serializable {

  private static final long serialVersionUID = -340750829787228669L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_LOCATION_TYPE_ID", nullable = false)
  private MessageLocationType messageLocationType;

  @OneToMany(mappedBy = "messageLocation")
  private List<MessageLocationRelation> messageLocationRelation;

  @Column(nullable = false)
  private String value;
}
