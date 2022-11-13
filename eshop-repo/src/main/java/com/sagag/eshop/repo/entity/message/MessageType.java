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
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_TYPE.
 */
@Entity
@Table(name = "MESSAGE_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageType implements Serializable {

  private static final long serialVersionUID = -2310017140074358119L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String type;

  private String description;
}
