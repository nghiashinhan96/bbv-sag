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
 * Entity mapping class to table MESSAGE_ACCESS_RIGHT_AREA.
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT_AREA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAccessRightArea implements Serializable {

  private static final long serialVersionUID = 6396239206857109957L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ACCESS_RIGHT_ID", nullable = false)
  private MessageAccessRight messageAccessRight;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_AREA_ID", nullable = false)
  private MessageArea messageArea;
}
