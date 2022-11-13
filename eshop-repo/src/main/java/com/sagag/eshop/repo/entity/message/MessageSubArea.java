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
 * Entity mapping class to table MESSAGE_SUB_AREA.
 */
@Entity
@Table(name = "MESSAGE_SUB_AREA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSubArea implements Serializable {

  private static final long serialVersionUID = 1645524487967414057L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String subArea;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_AREA_ID", nullable = false)
  private MessageArea messageArea;

  @Column(nullable = false)
  private int sort;

  private String description;
}
