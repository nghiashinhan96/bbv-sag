package com.sagag.eshop.repo.entity.message;

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
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_HIDING.
 */
@Entity
@Table(name = "MESSAGE_HIDING")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageHiding implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "USER_ID")
  private long userId;

  @Column(name = "MESSAGE_ID")
  private long messageId;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ID", nullable = false, insertable = false, updatable = false)
  private Message message;
}
