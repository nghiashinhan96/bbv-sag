package com.sagag.eshop.repo.entity.message;

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
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_LOCATION_RELATION.
 */
@Entity
@Table(name = "MESSAGE_LOCATION_RELATION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLocationRelation implements Serializable{

  private static final long serialVersionUID = 3986177497717341133L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ID", nullable = false)
  private Message message;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_LOCATION_ID", nullable = false)
  private MessageLocation messageLocation;

}
