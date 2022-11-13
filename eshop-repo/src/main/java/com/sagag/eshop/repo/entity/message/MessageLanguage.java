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
 * Entity mapping class to table MESSAGE_MULTILINGUALISM.
 */
@Entity
@Table(name = "MESSAGE_LANGUAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLanguage implements Serializable {

  private static final long serialVersionUID = 3794691210066053613L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ID", nullable = false)
  private Message message;

  @Column(nullable = false)
  private String langIso;

  @Column(nullable = false)
  private String content;
}
