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
 * Entity mapping class to table MESSAGE_AREA.
 */
@Entity
@Table(name = "MESSAGE_AREA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageArea implements Serializable {

  private static final long serialVersionUID = -3092589059558642344L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String area;

  private String description;

  @OneToMany(mappedBy = "messageArea")
  private List<MessageSubArea> messageSubAreas;

  @Column(nullable = false)
  private boolean auth;
}
